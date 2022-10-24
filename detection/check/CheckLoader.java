package dev.rythem.ambition.detection.check;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import dev.rythem.ambition.detection.check.impl.exploit.*;
import dev.rythem.ambition.detection.check.impl.move.FlightA;

public class CheckLoader {

	private Map<Class<? extends Check>, Check> checks = new HashMap<Class<? extends Check>, Check>();
	public static Class<?>[] checkList = {FlightA.class};
	
	private final JsonObject config;
	public CheckLoader(final JsonObject config)
	{
		this.config = config;
		
		for(final Class<?> c : checkList)
		{
			register((Class<? extends Check>) c);
		}
	}
	
	public Collection<Check> getChecks()
	{
		return checks.values();
	}
	
	public void register(final Class<? extends Check> c)
	{
		try
		{
			final Check check = c.newInstance();
			check.setConfig(this.config.get(check.getName() + "_" + check.getType()).getAsJsonObject());
			
			checks.put(c, check);
			System.out.println("Loaded check " + c.getName());
		}
		catch(final Exception e)
		{
			System.err.println("Failed to load check " + c.getName());
		}
	}
	
}
