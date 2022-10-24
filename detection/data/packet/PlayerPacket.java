package dev.rythem.ambition.detection.data.packet;

import dev.rythem.ambition.detection.data.PlayerData;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S19PacketEntityStatus;

public class PlayerPacket {
	
	private boolean swing, use, status,
		movement, rotation, flying;
	
	private final Packet<?> packet;
	private final PlayerData playerData;
	
	public PlayerPacket(final PlayerData playerData, final Packet<?> packet)
	{
		this.playerData = playerData;
		this.packet = packet;
	
		if(packet instanceof S14PacketEntity)
		{
			final S14PacketEntity s14 = (S14PacketEntity)packet;
			
			if(s14.getEntityID() == playerData.getEntityId())
			{
				if(s14 instanceof S14PacketEntity.S15PacketEntityRelMove)
					this.flying = this.movement = true;
				else if(s14 instanceof S14PacketEntity.S16PacketEntityLook)
					this.flying = this.rotation = true;
				else if(s14 instanceof S14PacketEntity.S17PacketEntityLookMove)
					this.flying = this.movement = this.rotation = true;
				else
					this.flying = true;
			}
		}
		else if(packet instanceof S19PacketEntityStatus)
		{
			final S19PacketEntityStatus s19 = (S19PacketEntityStatus)packet;
			
			if(s19.getEntityId() == playerData.getEntityId())
			{
				
			}
			status = true;
		}
		else if(packet instanceof S0BPacketAnimation)
		{
			
		}
		
		if(this.movement || flying) playerData.getMovementProcessor().handle(playerData.getPlayer());
		if(this.rotation) playerData.getRotationProcessor().handle(playerData.getPlayer());
		if(this.swing || this.use || status) playerData.getActionProcessor().handle(playerData.getPlayer());
	}
	
	public PlayerData getPlayerData()
	{
		return this.playerData;
	}
	
	public Packet<?> getPacket()
	{
		return this.packet;
	}
	
	public boolean isFlying()
	{
		return this.flying;
	}
	
	public boolean isMovement()
	{
		return this.movement;
	}
	
	public boolean isRotation()
	{
		return this.rotation;
	}
	
	public boolean isUse()
	{
		return this.use;
	}
	
	public boolean isStatus()
	{
		return this.status;
	}
	
	public boolean isSwing()
	{
		return this.swing;
	}
	
}
