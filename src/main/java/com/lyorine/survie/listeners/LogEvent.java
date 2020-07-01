package com.lyorine.survie.listeners;

import com.lyorine.survie.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public class LogEvent implements Listener {

    private Main main;

    public LogEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onChestOpen(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if(b != null) {
            if (b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST)){
                main.getSurvieLogger().log(e, "Coffre ouvert par "+p.getDisplayName()+" en x:"+b.getLocation().getX()+", y:"+b.getLocation().getY()+", z:"+b.getLocation().getZ());
            }
        }
    }

    @EventHandler
    public void onChestClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
        InventoryHolder holder = e.getInventory().getHolder();
        if(holder instanceof Chest){
            Chest c = (Chest)holder;
            main.getSurvieLogger().log(e, "Coffre fermé par "+p.getDisplayName()+" en x:"+c.getLocation().getX()+", y:"+c.getLocation().getY()+", z:"+c.getLocation().getZ());
        }
        if(holder instanceof DoubleChest){
            DoubleChest c = (DoubleChest)holder;
            Chest left = (Chest)c.getLeftSide();
            main.getSurvieLogger().log(e, "Double-coffre fermé par "+p.getDisplayName()+" en x:"+c.getLocation().getX()+", y:"+c.getLocation().getY()+", z:"+c.getLocation().getZ());
        }
    }

}
