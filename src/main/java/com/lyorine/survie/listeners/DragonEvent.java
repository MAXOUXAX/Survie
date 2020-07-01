package com.lyorine.survie.listeners;

import com.lyorine.survie.Main;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class DragonEvent implements Listener {

    /*private Main main;

    public DragonEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof EnderDragon){
            e.setDamage(e.getDamage()/2);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof EnderDragon){
            EnderDragon enderDragon = (EnderDragon) e.getEntity();
            main.getCommandDragon().getDragonParticles().cancel();
            Location loc = enderDragon.getLocation();
            Bukkit.getOnlinePlayers().forEach(o -> {
                o.resetMaxHealth();
            });
            Bukkit.broadcastMessage("§d§lDragon §r§7» §a§lBien joué ! §r§aVous avez vaincu le dragon !");
            for (int i = 0; i < 30; i++) {
                ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc.clone().add(0, i*2, 0), EntityType.EXPERIENCE_ORB);
                orb.setExperience(50);
            }
        }
    }

    @EventHandler
    public void onDragonPhase(EnderDragonChangePhaseEvent e) {
        if (e.getNewPhase().equals(EnderDragon.Phase.LAND_ON_PORTAL)) {
            EnderDragon enderDragon = e.getEntity();
            Bukkit.broadcastMessage("§d§lDragon §r§7» §6Lancement de la lave");
            for (int i = 0; i < 5; i++) {
                enderDragon.getWorld().getPlayers().forEach(o -> {
                    enderDragon.getWorld().spawnParticle(Particle.DRIP_LAVA, o.getLocation(), 200, 1, 1,1 ,0);
                    enderDragon.getWorld().playSound(enderDragon.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2000, 1);
                    FallingBlock fallingLave = enderDragon.getWorld().spawnFallingBlock(enderDragon.getLocation(), Material.LAVA, (byte)0);
                    Vector from = enderDragon.getLocation().toVector();
                    Vector to = o.getLocation().toVector();
                    Vector direction = to.subtract(from);
                    direction.normalize();
                    direction.multiply(2);
                    fallingLave.setVelocity(direction);
                });
            }
        }
    }*/

}
