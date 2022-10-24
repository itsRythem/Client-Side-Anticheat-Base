package dev.rythem.ambition.detection.check.impl.move;

import dev.rythem.ambition.detection.check.Check;
import dev.rythem.ambition.detection.check.api.CheckInfo;
import dev.rythem.ambition.detection.check.api.CheckType;
import dev.rythem.ambition.detection.data.PlayerData;
import dev.rythem.ambition.detection.data.packet.PlayerPacket;
import dev.rythem.ambition.detection.data.processor.impl.*;
import dev.rythem.ambition.detection.util.MoveUtil;
import net.minecraft.util.BlockPos;

@CheckInfo(name = "Flight", type = "A", vl = 10, category = CheckType.MOVEMENT)
public final class FlightA extends Check {

	@Override
	public void handle(final PlayerPacket packet)
	{
		final MovementProcessor movement = packet.getPlayerData().getMovementProcessor();
		final RotationProcessor rotation = packet.getPlayerData().getRotationProcessor();
		
		if(packet.isMovement() || packet.isFlying() || packet.isRotation())
		{
			final boolean isNearBlock = MoveUtil.isNearBlock(movement.getX(), movement.getY(), movement.getZ(), 3, 2);
			final double deltaY = Math.abs(movement.getDeltaY());
			
			if(deltaY < 1.0E-7f && !isNearBlock)
			{
				fail("§7Suspicious Delta Y §e" + deltaY);
				System.out.println(movement.getX() + " " + movement.getY() + " " + movement.getZ());
			}
			else if(timeSinceFlag > 1000 && vl > 0) vl--;
		}
	}

}
