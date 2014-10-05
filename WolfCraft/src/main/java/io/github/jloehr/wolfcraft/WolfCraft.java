package io.github.jloehr.wolfcraft;

import org.bukkit.plugin.java.JavaPlugin;
import io.github.jloehr.wolfcraft.ServerSettings;

public final class WolfCraft extends JavaPlugin {

	ServerSettings ServerSettings = new ServerSettings(this);
	WerewolfGame Game = new WerewolfGame(this);
	
	@Override
	public void onEnable() {
		this.getCommand("Setup").setExecutor(ServerSettings);
		
		ServerSettings.Setup();
		Game.onEnable();
	}

	@Override
	public void onDisable() {
	}
}
