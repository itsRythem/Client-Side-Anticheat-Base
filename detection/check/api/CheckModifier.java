package dev.rythem.ambition.detection.check.api;

public enum CheckModifier {

	EXPIREMENTAL("Expiremental", "§a"),
	BETA("Beta", "§d");
	
	private final String name, color;
	CheckModifier(final String name, final String color)
	{
		this.name = name;
		this.color = color;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getColor()
	{
		return this.color;
	}
	
}