package dev.rythem.ambition.detection.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class MoveUtil {

	protected static Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean isNearBlock(final double x, final double y, final double z, final int hRadius, final int vRadius)
	{
		for(int xOffset = -hRadius; xOffset < hRadius; xOffset++)
		{
			for(int yOffset = -vRadius; yOffset < vRadius; yOffset++)
			{
				for(int zOffset = -hRadius; zOffset < hRadius; zOffset++)
				{
					final BlockPos pos = new BlockPos(x + xOffset, y + yOffset, z + zOffset);
					if(!mc.theWorld.isAirBlock(pos))
						return true;
				}
			}
		}
		
		return false;
	}
	
}
