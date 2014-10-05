package io.github.jloehr.wolfcraft;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class GhostFilter implements Listener {
	
	private final WerewolfGame Game;
	
	public GhostFilter(WerewolfGame Game)
	{
		this.Game = Game;
	}
	
	public void MakeGhost(Player Player)
	{
		//Hide from all
		for(Player tmp : Bukkit.getServer().getOnlinePlayers())
		{
			if(Game.IsAlive(tmp))
			{
				tmp.hidePlayer(Player);
				Player.hidePlayer(tmp);
			}
			else
			{
				tmp.showPlayer(Player);
				Player.showPlayer(tmp);
			}
		}
		
		//GameMode
		Player.setGameMode(GameMode.ADVENTURE);
		
		//SetFly and food
		Player.setAllowFlight(true);
		Player.setFoodLevel(20);
	}
	
	public void UnghostAll()
	{
		for(Player tmp : Bukkit.getServer().getOnlinePlayers())
		{
			Unghost(tmp);
		}
	}
	
	public void Unghost(Player Player)
	{
		//Show all
		for(Player tmp : Bukkit.getServer().getOnlinePlayers())
		{
			if(Game.IsAlive(tmp))
			{
				tmp.showPlayer(Player);
				Player.showPlayer(tmp);
			}
			else
			{
				tmp.hidePlayer(Player);
				Player.hidePlayer(tmp);
			}
		}
		
		
		//GameMode
		Player.setGameMode(GameMode.SURVIVAL);
		
		//UnsetFly and food
		Player.setAllowFlight(false);
	}

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent Event)
	{
		if(!Game.IsAlive(Event.getPlayer()))
		{
			//Make Ghost
			MakeGhost(Event.getPlayer());
		}
		else
		{
			Unghost(Event.getPlayer());
		}
	}
	
	@EventHandler
	protected void OnBlockCanBuild(BlockCanBuildEvent Event) {
		if(Event.isBuildable())
		{
			return;
		}
		
		//Check if ghost is blocking
		Location Block = new Location(null, 0, 0, 0);
		boolean isBuildable = false;
		
		for(Player Player : Bukkit.getServer().getOnlinePlayers())
		{
			Event.getBlock().getLocation(Block);
			
			Block.subtract(Player.getLocation());
			
			if((Block.getBlockX() != 0) || (Block.getBlockZ() != 0))
			{
				continue;
			}
			
			if((Block.getBlockY() != 0) && (Block.getBlockY() != 1))
			{
				continue;
			}
			
			if(Game.IsAlive(Player))
			{
				isBuildable = false;
				break;
			}
			else
			{
				isBuildable = true;
			}
		}
		
		Event.setBuildable(isBuildable);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	protected void OnBlockPlace(BlockPlaceEvent Event) 
	{	
		if(!Game.IsAlive(Event.getPlayer()))
		{
			Event.setCancelled(true);
		}
	}
	
	@EventHandler
	protected void OnEntityDamageByEntity(EntityDamageByEntityEvent Event) 
	{
		if(Event.getDamager() instanceof Player)
		{
			Player Damager = (Player)Event.getDamager();
			if(!Game.IsAlive(Damager))
			{
				Event.setCancelled(true);
			}
		}
		
		if(Event.getEntity() instanceof Player)
		{
			Player Reciever = (Player)Event.getEntity();
			if(!Game.IsAlive(Reciever))
			{
				Event.setCancelled(true);
			}
		}
	}
	
	
	@EventHandler
	protected void OnBlockBreak(BlockBreakEvent Event) 
	{
		if(!Game.IsAlive(Event.getPlayer()))
		{
			Event.setCancelled(true);
		}
	}
	
	@EventHandler
	protected void OnPlayerDropItem(PlayerDropItemEvent Event) 
	{
		if(!Game.IsAlive(Event.getPlayer()))
		{
			Event.setCancelled(true);
		}	
	}
	
	@EventHandler
	protected void OnPlayerPickupItem(PlayerPickupItemEvent Event) 
	{
		if(!Game.IsAlive(Event.getPlayer()))
		{
			Event.setCancelled(true);
		}	
	}
	
	@EventHandler
	protected void OnEntityTarget(EntityTargetEvent Event)
	{
		if((Event.getTarget() == null) || (!(Event.getTarget() instanceof Player)))
		{
			return;
		}
		
		Player Target = (Player)Event.getTarget();
		if(!Game.IsAlive(Target))
		{
			Event.setCancelled(true);
		}	
		
	}
}
