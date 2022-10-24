package dev.rythem.ambition.detection.data.processor.impl;

import dev.rythem.ambition.detection.data.PlayerData;
import dev.rythem.ambition.detection.data.processor.Processor;
import net.minecraft.entity.player.EntityPlayer;

public final class RotationProcessor implements Processor {
	
	private float yaw, pitch,
			prevYaw, prevPitch,
			deltaYaw, deltaPitch,
			prevDeltaYaw, prevDeltaPitch;
	
	@Override
	public void handle(final EntityPlayer player)
	{
		this.prevYaw = this.yaw;
		this.yaw = player.rotationYaw;
		
		this.prevPitch = this.pitch;
		this.pitch = player.rotationPitch;
		
		this.prevDeltaYaw = this.deltaYaw;
		this.deltaYaw = this.yaw - this.prevYaw;
		
		this.prevDeltaPitch = this.deltaPitch;
		this.deltaPitch = this.pitch - this.prevPitch;
	}

	public double getYaw() {return yaw;}
	public double getPitch() {return pitch;}
	public double getPrevYaw() {return prevYaw;}
	public double getPrevPitch() {return prevPitch;}
	public double getDeltaYaw() {return deltaYaw;}
	public double getDeltaPitch() {return deltaPitch;}
	public double getPrevDeltaYaw() {return prevDeltaYaw;}
	public double getPrevDeltaPitch() {return prevDeltaPitch;}
	
}
