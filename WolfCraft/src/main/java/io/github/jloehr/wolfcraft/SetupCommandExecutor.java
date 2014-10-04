package io.github.jloehr.wolfcraft;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class SetupCommandExecutor implements CommandExecutor {
	
	@SuppressWarnings("unused")
	private final WolfCraft Plugin;
	
	public SetupCommandExecutor(WolfCraft Plugin)
	{
		this.Plugin = Plugin;
	}
	
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) 
	{
		SetupScoreboards();
		return true;
	}

	
	protected void SetupScoreboards()
	{
		String HealthObjective = "Health";
		
		Scoreboard Board = Bukkit.getScoreboardManager().getMainScoreboard();
		if(Board.getObjective(HealthObjective) == null)
		{
			Objective Health = Board.registerNewObjective(HealthObjective, "health");
			Health.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
	}
}
