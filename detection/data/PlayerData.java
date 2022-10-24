package dev.rythem.ambition.detection.data;

import java.util.UUID;

import dev.rythem.ambition.Ambition;
import dev.rythem.ambition.detection.check.CheckLoader;
import dev.rythem.ambition.detection.data.processor.impl.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldSettings.GameType;

public class PlayerData {

	protected static Minecraft mc = Minecraft.getMinecraft();
	
	private final CheckLoader checkLoader = new CheckLoader(Ambition.getCore().getDetectionHandler().getConfig());
	
	private final MovementProcessor movementProcessor = new MovementProcessor();
	private final RotationProcessor rotationProcessor = new RotationProcessor();
	private final ActionProcessor actionProcessor = new ActionProcessor();
	
	private final UUID uuid;
	private final int entityId;
	
	public PlayerData(final UUID uuid, final int entityId)
	{
		this.uuid = uuid;
		this.entityId = entityId;
		
		this.checkLoader.getChecks().forEach(c -> {
			c.setPlayerData(this);	
		});
	}
	
	public MovementProcessor getMovementProcessor() {return this.movementProcessor;}
	public RotationProcessor getRotationProcessor() {return this.rotationProcessor;}
	public ActionProcessor getActionProcessor() {return this.actionProcessor;}
	
	public CheckLoader getCheckLoader() {return this.checkLoader;}
	
	public UUID getUUID() {return this.uuid;}
	public int getEntityId() {return this.entityId;}
	
	public EntityPlayer getPlayer()
	{
		return mc.thePlayer.getEntityWorld().getPlayerEntityByUUID(uuid);
	}
	
	public boolean isExcluded()
	{
		final NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(uuid);
		final EntityPlayer player = getPlayer();
		
		if(player.getName().startsWith("CIT-"))
			return true;
		
		if(player.isFake() || player.isBot())
			return true;
		
		if(playerInfo != null && playerInfo.getGameType() != null && playerInfo.getGameType() == GameType.CREATIVE)
			return true;
		
		return false;
	}
	
}
