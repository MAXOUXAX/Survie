package com.lyorine.survie.listeners;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class GrapinEvent implements Listener {

    public ArrayList<UUID> noFallDamage = new ArrayList<>();

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        Player p = e.getPlayer();
        PlayerFishEvent.State state = e.getState();
        FishHook hook = e.getHook();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (isGrapin(item) || isGrapin(p.getInventory().getItemInOffHand())) {
            if (state == PlayerFishEvent.State.IN_GROUND) {
                if(!noFallDamage.contains(p.getUniqueId()))noFallDamage.add(p.getUniqueId());
                Location hookLocation = hook.getLocation();
                Vector from = p.getLocation().toVector();
                Vector to = hookLocation.toVector();
                Vector direction = to.subtract(from);
                direction.add(new Vector(0, 5, 0));
                direction.normalize();
                direction.multiply(2);
                p.setVelocity(direction);
                p.playSound(hookLocation, Sound.ENTITY_MAGMA_CUBE_JUMP, 200, 1);
                hookLocation.getWorld().spawnParticle(Particle.CLOUD, hookLocation, 50, 1, 1, 1, 0.1);
            } else if (state == PlayerFishEvent.State.FISHING) {
                hook.setVelocity(hook.getVelocity().multiply(2));
                p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_ELYTRA, 200, 1);
            } else {
                e.setCancelled(true);
                e.getHook().remove();
                p.sendActionBar("§7» §cVous devez poser le bout du §6§lgrapin §csur un block !");
                p.playSound(p.getLocation(), Sound.ENTITY_WANDERING_TRADER_NO, 20000, 1);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntityType() == EntityType.PLAYER && e.getCause() == EntityDamageEvent.DamageCause.FALL){
            Player p = (Player) e.getEntity();
            if(noFallDamage.contains(p.getUniqueId())){
                e.setCancelled(true);
                noFallDamage.remove(p.getUniqueId());
            }
        }
    }

    public boolean isGrapin(ItemStack item){
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            String loreSecondLine = item.getItemMeta().getLore().get(1);
            if (loreSecondLine == null) return false;
            if (loreSecondLine.equalsIgnoreCase("§6§lGrapin")) {
                return true;
            }
        }
        return false;
    }

}
