package io.github.jloehr.wolfcraft;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import io.github.jloehr.wolfcraft.SetupCommandExecutor;

public final class WolfCraft extends JavaPlugin implements Listener {

	Player Werewolf = null;
	
	@Override
	public void onEnable() {
		this.getCommand("Setup").setExecutor(new SetupCommandExecutor(this));
	}

	@Override
	public void onDisable() {
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("start"))
		{
			Player[] PlayerList = Bukkit.getServer().getOnlinePlayers();
			int RandomIndex = new Random().nextInt(PlayerList.length);
			Werewolf = PlayerList[RandomIndex];
			
			//Messages
			SendSelectedNotifiation();
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("resume"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("This command can only be run by a player.");
				return false;
			}
			else
			{
				Werewolf = (Player)sender;
				
				//Messages
				SendSelectedNotifiation();
				
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("whatami"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage("This command can only be run by a player.");
				return false;
			}
			else
			{
				
				Player Sender = (Player)sender;
				
				if(Werewolf == null)
				{
					Sender.sendMessage("Round hasn't started yet");
				}
				else
				{
				
					String Answer = (Sender == Werewolf) ? "You are the " + ChatColor.RED + "Werewolf!" : "You are " + ChatColor.GREEN + "innocent!";
					Sender.sendMessage(Answer);
				}
				
				return true;
			}
			
		}
		
		return false; 
	}
	
	private void SendSelectedNotifiation()
	{

		for(Player tmp : Bukkit.getServer().getOnlinePlayers())
		{
			if(tmp == Werewolf)
			{
				tmp.sendMessage("You are the " + ChatColor.RED + "Werewolf!");
			}
			else
			{
				tmp.sendMessage(ChatColor.GREEN + "The Werewolf has been selected");
			}
		}
	}
	
	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent Event)
	{
		if(Werewolf == null)
		{
			return;
		}
		
		if(Event.getEntity() == Werewolf)
		{
			Player Killed = (Player)Event.getEntity();

			//Werewolf dead
			for(Player tmp : Bukkit.getServer().getOnlinePlayers())
			{
				tmp.sendMessage("The " + ChatColor.RED + "Werewolf" + ChatColor.GOLD + "(" + Killed.getDisplayName() + ")" + ChatColor.WHITE + " is no more! Victory for the innocent.");
			}
			
			Werewolf = null;
		}
		else
		{
			Event.setDeathMessage("");
		}
	}
}
