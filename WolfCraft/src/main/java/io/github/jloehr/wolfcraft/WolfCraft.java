package io.github.jloehr.wolfcraft;

import org.bukkit.plugin.java.JavaPlugin;
import io.github.jloehr.wolfcraft.SetupCommandExecutor;

public final class WolfCraft extends JavaPlugin {

	@Override
	public void onEnable() {
		this.getCommand("Setup").setExecutor(new SetupCommandExecutor(this));
	}

	@Override
	public void onDisable() {
	}

}
