package dev.rythem.ambition.detection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dev.rythem.ambition.Ambition;
import dev.rythem.ambition.detection.check.Check;
import dev.rythem.ambition.detection.check.CheckLoader;
import dev.rythem.ambition.detection.data.PlayerData;
import dev.rythem.ambition.detection.data.PlayerHandler;
import dev.rythem.ambition.event.EventHook;
import dev.rythem.ambition.event.impl.PacketEvent;
import dev.rythem.ambition.event.model.EventSide;
import dev.rythem.ambition.event.model.EventType;
import dev.rythem.ambition.helper.FileHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S13PacketDestroyEntities;

public class DetectionHandler {
	
	private final PlayerHandler playerHandler = new PlayerHandler();
	private boolean listening = false;
	private JsonObject config;
	
	public DetectionHandler(final File configFile)
	{
		// Ensure the config is empty
		if(configFile.length() <= 0)
		{
			config = new JsonObject();
			try
			{
				// Writing the config
				writeConfig(configFile);
			}
			catch(final Exception e)
			{
				e.printStackTrace();
			}
		}
		// Attempt to parse the config or rewrite it, if its missing checks
		else
		{
			try
			{
				// Parsing the config
				this.config = JsonParser.parseReader(new FileReader(configFile)).getAsJsonObject();
				
				// Looping through static instance of checks
				for(final Class<?> c : CheckLoader.checkList)
				{
					final Check check = (Check)c.newInstance();
					// Checking if the config does not have this check
					if(!this.config.has(check.getName() + "_" + check.getType()))
					{
						// ReWriting the config
						writeConfig(configFile);
						break;
					}
				}
			}
			catch(final Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	// Enables the cheat detector and listens on packets
	public void listen()
	{
		// Ensures the detector is not listening before registering it
		if(!this.listening)
		{
			this.listening = true;
			
			// Register this class to your event handler
			Ambition.getCore().getEventHandler().register(this);
			
			// Adding players that already exist
			for(final EntityPlayer player : mc.thePlayer.getEntityWorld().playerEntities)
				playerHandler.putUser(player.getUniqueID(), new PlayerData(player.getUniqueID(), player.getEntityId()));
			
			System.out.println("Cheat Detection started");
		}
	}
	
	// Stops the cheat detector
	public void stop()
	{
		// Ensures the detector is listening before unregistering it
		if(this.listening)
		{
			this.listening = false;
			
			// Unregister your class from the event handler
			Ambition.getCore().getEventHandler().unregister(this);
			
			System.err.println("Cheat Detection ended");
		}
	}
	
	@EventHook
	public void onPacket(final PacketEvent event)
	{
		// Ensure the packet recieved is from the server. (it would be a good idea to only trigger this on post recieve, duplicates will be removed)
		if(event.getSide() == EventSide.SERVER)
		{
			final Packet<?> packet = event.getPacket();
			
			// Gets new players and adds them to the player handler
			if(packet instanceof S0CPacketSpawnPlayer)
			{
				final S0CPacketSpawnPlayer s0c = (S0CPacketSpawnPlayer)packet;
				
				// Adds the entity
				playerHandler.putUser(s0c.getPlayer(), new PlayerData(s0c.getPlayer(), s0c.getEntityID()));
			}
			// Gets removed players and removes them from the player handler (if the entity becomes null without this it will also be removed)
			else if(packet instanceof S13PacketDestroyEntities)
			{
				final S13PacketDestroyEntities s13 = (S13PacketDestroyEntities)packet;
				
				for(final int id : s13.getEntityIDs())
				{
					for(final PlayerData data : this.playerHandler.getPlayers().values())
					{
						if(data.getEntityId() == id)
						{
							// Removes the entity
							playerHandler.removeUser(data.getUUID());
						}
					}
				}
			}
			else
				// Processes the server packet
				playerHandler.process(packet);
		}
	}
	
	// used to write or rewrite the config incase there is missing checks
	private void writeConfig(final File configFile) throws Exception
	{
		// message to send when a check is alerted
		
		config = new JsonObject();
		config.addProperty("command", ".notification %player% failed %name% %type%");
		
		for(final Class<?> c : CheckLoader.checkList)
		{
			final JsonObject cObj = new JsonObject();
			
			final Check check = (Check)c.newInstance();
			cObj.addProperty("enabled", true);
			cObj.addProperty("max-vl", check.getMaxVl());
			
			config.add(check.getName() + "_" + check.getType(), cObj);
		}
		
		final FileWriter writer = new FileWriter(configFile);
		writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
		writer.flush();
		writer.close();
	}

	public JsonObject getConfig() {return this.config;}
	
}
