package io.github.jloehr.wolfcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class WerewolfGame  implements CommandExecutor, Listener {

	private final WolfCraft Plugin;
	private GhostFilter GhostFilter = new GhostFilter(this);
	private List<Player> AlivePlayers = new ArrayList<Player>();
	private Player Werewolf = null;
	
	public WerewolfGame(WolfCraft Plugin)
	{
		this.Plugin = Plugin;
	}
	
	public void onEnable()
	{
		Plugin.getCommand("start").setExecutor(this);
		Plugin.getCommand("resume").setExecutor(this);
		Plugin.getCommand("whatami").setExecutor(this);
		Plugin.getCommand("ghost").setExecutor(this);
		Plugin.getCommand("unghost").setExecutor(this);
		
		Plugin.getServer().getPluginManager().registerEvents(this, Plugin);
		Plugin.getServer().getPluginManager().registerEvents(GhostFilter, Plugin);
	}

	public boolean onCommand(CommandSender Sender, Command Cmd, String arg2,
			String[] arg3) {
		if(Cmd.getName().equalsIgnoreCase("start"))
		{
			return ProcessStartCommand();
		} 
		else if(Cmd.getName().equalsIgnoreCase("resume"))
		{
			return ProcessResumeCommand(Sender);
		} 
		if(Cmd.getName().equalsIgnoreCase("whatami"))
		{
			return ProcessWhatAmICommand(Sender);
		} 	
		if(Cmd.getName().equalsIgnoreCase("ghost"))
		{
			if(Sender instanceof Player)
			{
				GhostFilter.MakeGhost((Player)Sender);
				return true;
			}
		} 	
		if(Cmd.getName().equalsIgnoreCase("unghost"))
		{
			if(Sender instanceof Player)
			{
				GhostFilter.Unghost((Player)Sender);
				return true;
			}
		} 	
		
		return false;
	}

	private boolean ProcessStartCommand()
	{
		StartGame(null);
		return true;
	}
	
	private boolean ProcessResumeCommand(CommandSender Sender)
	{
		if(Sender instanceof Player)
		{
			StartGame((Player)Sender);
			return true;
		}
		else
		{
			Sender.sendMessage("This command can only be run by a player.");
			return false;
		}
	}

	private boolean ProcessWhatAmICommand(CommandSender Sender)
	{
		if(!(Sender instanceof Player))
		{
			Sender.sendMessage("This command can only be run by a player.");
			return false;
		}
		
		Player Player = (Player)Sender;
		
		if(!HasStarted())
		{
			Player.sendMessage("Round hasn't started yet");
			return true;
		}
		
		if(!AlivePlayers.contains(Player))
		{
			Player.sendMessage("You are dead.");
		}
		
		String Answer = (Player == Werewolf) ? "You are the " + ChatColor.RED + "Werewolf" + ChatColor.WHITE + "!" : "You are " + ChatColor.GREEN + "villager" + ChatColor.WHITE + "!";
		Player.sendMessage(Answer);
		return true;
	}
	
	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent Event)
	{
		if(!HasStarted())
		{
			return;
		}
		
		Player Killed = Event.getEntity();
		
		//Player is Ghost
		if(!(AlivePlayers.contains(Killed)))
		{
			Event.setDeathMessage("");
			return;
		}
		
		//Killed is Werewolf
		if(Killed == Werewolf)
		{
			//EndGame
			NotifyAll("The " + ChatColor.RED + "Werewolf" + ChatColor.GOLD + "(" + Killed.getDisplayName() + ")" + ChatColor.WHITE + " is no more! Victory for the " + ChatColor.GREEN + "villagers" + ChatColor.WHITE + ".");
			StopGame();
		}
		else
		{
			AlivePlayers.remove(Killed);
			
			if(AlivePlayers.size() == 1)
			{
				//EndGame
				NotifyAll("The " + ChatColor.RED + "Werewolf" + ChatColor.GOLD + "(" + Killed.getDisplayName() + ")" + ChatColor.WHITE + " has slain all villagers! Victory for the " + ChatColor.RED + "Werewolf" + ChatColor.WHITE + ".");
				StopGame();
			}
			else
			{
				//Hide DeathMessage
				Event.setDeathMessage("");
				GhostFilter.MakeGhost(Killed);
			}
		}
	}

	protected void StartGame(Player Werewolf)
	{
		AlivePlayers.addAll(Arrays.asList(Bukkit.getServer().getOnlinePlayers()));
		
		if(Werewolf == null)
		{
			this.Werewolf = GetRandomPlayer();
		}
		else
		{
			this.Werewolf = Werewolf;
		}
		
		for(Player tmp : AlivePlayers)
		{
			if(tmp == this.Werewolf)
			{
				tmp.sendMessage("You are the " + ChatColor.RED + "Werewolf" + ChatColor.WHITE + "!");
			}
			else
			{
				tmp.sendMessage(ChatColor.GREEN + "The Werewolf has been selected");
			}
		}
	}

	protected void StopGame()
	{
		AlivePlayers.clear();
		Werewolf = null;
		
		GhostFilter.UnghostAll();
	}
	
	protected Player GetRandomPlayer()
	{
		int RandomIndex = new Random().nextInt(AlivePlayers.size());
		return AlivePlayers.get(RandomIndex);
	}
	
	public boolean HasStarted()
	{
		return (Werewolf != null);
	}
	
	public boolean IsAlive(Player Player)
	{
		if(!HasStarted())
		{
			return true;
		}
		
		return AlivePlayers.contains(Player);
	}
	
	private void NotifyAll(String Message)
	{
		for(Player tmp : Bukkit.getServer().getOnlinePlayers())
		{
			tmp.sendMessage(Message);
		}
	}	
}
