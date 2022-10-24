package dev.rythem.ambition.detection.data.processor;

import dev.rythem.ambition.detection.data.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public interface Processor {

	static Minecraft mc = Minecraft.getMinecraft();
	
	public void handle(final EntityPlayer data);
	
}
