package dev.rythem.ambition.detection.check;

import com.google.gson.JsonObject;

import dev.rythem.ambition.detection.check.api.CheckInfo;
import dev.rythem.ambition.detection.check.api.CheckType;
import dev.rythem.ambition.detection.data.PlayerData;
import dev.rythem.ambition.detection.data.packet.PlayerPacket;
import net.minecraft.client.Minecraft;

public abstract class Check {

	protected static Minecraft mc = Minecraft.getMinecraft();
	
	private final CheckInfo info;
	private PlayerData playerData;
	private JsonObject config;
	
	protected int vl, maxVl;
	protected long timeSinceFlag = 0, timeFlagged = System.currentTimeMillis(), timeLastPacket = System.currentTimeMillis();
	
	public Check()
	{
		final Class c = this.getClass();
        if (c.isAnnotationPresent(CheckInfo.class))
        {
            info = (CheckInfo) c.getAnnotation(CheckInfo.class);
            maxVl = info.vl();
        }
        else
            throw new RuntimeException("Couldn't find info annotation for " + c.getName());
	}
	
	public void setPlayerData(final PlayerData playerData) {this.playerData = playerData;}
	public void setConfig(final JsonObject config) {this.config = config;}
	
	public void handleCheck(final PlayerPacket packet)
	{
		this.timeSinceFlag = (System.currentTimeMillis() - timeFlagged);
		this.timeLastPacket = System.currentTimeMillis();
		
		handle(packet);
	}
	
	public abstract void handle(final PlayerPacket packet);
	
	public void fail(final String verbose)
	{
		this.vl++;
		
		mc.thePlayer.sendRawClientMessage("§8§l[§c§l!§8§l] §7The Player §e" + this.playerData.getPlayer().getName() + "§7 has failed §e" + this.getName() + "§7[" + this.getType() + "]" + " §8[§bVL: " + this.vl + "§8]");
		this.timeFlagged = System.currentTimeMillis();
		
		if(this.vl > this.maxVl)
		{
			// Do action here
			
			this.vl = 0;
		}
	}
	
	public String getName() {return info.name();}
	public CheckType getCategory() {return info.category();}
	public String getType() {return info.type();}
	public JsonObject getConfig() {return this.config;}
	public int getVl() {return vl;}
	public int getMaxVl() {return maxVl;}
	
}
