package io.github.jloehr.wolfcraft;

import org.bukkit.plugin.java.JavaPlugin;
import io.github.jloehr.wolfcraft.ServerSettings;

public final class WolfCraft extends JavaPlugin {

	ServerSettings ServerSettings = new ServerSettings(this);
	WerewolfGame Game = new WerewolfGame(this);
	
	@Override
	public void onEnable() {
		this.getCommand("Setup").setExecutor(ServerSettings);
		this.getCommand("start").setExecutor(Game);
		this.getCommand("resume").setExecutor(Game);
		this.getCommand("whatami").setExecutor(Game);
		
		getServer().getPluginManager().registerEvents(Game, this);
		
		ServerSettings.Setup();
	}

	@Override
	public void onDisable() {
	}
}
