package dev.rythem.ambition.detection.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import dev.rythem.ambition.detection.check.Check;
import dev.rythem.ambition.detection.data.packet.PlayerPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;

public class PlayerHandler {

	private final Map<UUID, PlayerData> players = new ConcurrentHashMap<UUID, PlayerData>();
	
	public Map<UUID, PlayerData> getPlayers()
	{
		return this.players;
	}
	
	public void putUser(final UUID uuid, final PlayerData data)
	{
		this.players.put(uuid, data);
	}
	
	public void removeUser(final UUID uuid)
	{
		this.players.remove(uuid);
	}

	public void process(final Packet<?> packet)
	{
		for(final PlayerData data : this.players.values())
		{
			for(final Check check : data.getCheckLoader().getChecks())
			{
				if(data != null && data.getPlayer() != null && !data.isExcluded())
					check.handleCheck(new PlayerPacket(data, packet));
			}
		}
	}
	
}
