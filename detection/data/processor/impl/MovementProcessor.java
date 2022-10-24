package dev.rythem.ambition.detection.data.processor.impl;

import dev.rythem.ambition.detection.data.PlayerData;
import dev.rythem.ambition.detection.data.processor.Processor;
import net.minecraft.entity.player.EntityPlayer;

public final class MovementProcessor implements Processor {
	
	private double x, y, z,
			prevX, prevY, prevZ,
			deltaX, deltaY, deltaZ,
			prevDeltaX, prevDeltaY, prevDeltaZ;
	
	private boolean onGround, prevOnGround;
	
	@Override
	public void handle(final EntityPlayer player)
	{
		this.prevX = this.x;
		this.x = player.posX;
		
		this.prevY = this.y;
		this.y = player.posY;
		
		this.prevZ = this.z;
		this.z = player.posZ;
		
		this.prevDeltaX = this.deltaX;
		this.deltaX = this.x - this.prevX;
		
		this.prevDeltaY = this.deltaY;
		this.deltaY = this.y - this.prevY;
		
		this.prevDeltaZ = this.deltaZ;
		this.deltaZ = this.z - this.prevZ;
		
		this.prevOnGround = this.onGround;
		this.onGround = player.onGround;
	}

	public double getX() {return x;}
	public double getY() {return y;}
	public double getZ() {return z;}
	public double getPrevX() {return prevX;}
	public double getPrevY() {return prevY;}
	public double getPrevZ() {return prevZ;}
	public double getDeltaX() {return deltaX;}
	public double getDeltaY() {return deltaY;}
	public double getDeltaZ() {return deltaZ;}
	public double getPrevDeltaX() {return prevDeltaX;}
	public double getPrevDeltaY() {return prevDeltaY;}
	public double getPrevDeltaZ() {return prevDeltaZ;}
	public boolean isOnGround() {return onGround;}
	public boolean isPrevOnGround() {return prevOnGround;}
	
}
