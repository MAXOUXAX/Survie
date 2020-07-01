package com.lyorine.survie.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item != null && item.getItemMeta() != null) {
            String name = item.getItemMeta().getDisplayName();
            if (name.startsWith("§eExpérience §7» §a")) {
                Action a = e.getAction();
                if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                    e.setCancelled(true);
                    ItemStack itemToRemove = item.clone();
                    itemToRemove.setAmount(1);
                    p.getInventory().removeItem(itemToRemove);
                    int xpBottle = Integer.parseInt(name.replace("§eExpérience §7» §a", "").replace("xp", ""));
                    ExperienceOrb orb = (ExperienceOrb) p.getWorld().spawnEntity(p.getLocation(), EntityType.EXPERIENCE_ORB);
                    orb.setExperience(xpBottle);
                    p.sendMessage("§7» §7Et voilà ! Vous venez de récupérer §a"+xpBottle+"xp §7!");
                }
            }
        }
    }

}
