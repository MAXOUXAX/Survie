package com.lyorine.survie.managers;

import com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import com.lyorine.survie.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

public class VanishManager implements Listener {

    private final Main main;
    private ArrayList<UUID> vanishedPlayers = new ArrayList<>();

    public VanishManager(Main main) {
        this.main = main;
    }

    public void vanishPlayer(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, false, false));
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.hidePlayer(main, p);
        });
        vanishedPlayers.add(p.getUniqueId());
    }

    public void unvanishPlayer(Player p){
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.showPlayer(main, p);
        });
        vanishedPlayers.remove(p.getUniqueId());
    }

    public boolean isVanished(Player p){
        return vanishedPlayers.contains(p.getUniqueId());
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        vanishedPlayers.forEach(uuid -> {
            p.hidePlayer(main, Bukkit.getPlayer(uuid));
        });
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(isVanished(p)){
            unvanishPlayer(p);
        }else{
            vanishedPlayers.forEach(uuid -> {
                p.showPlayer(main, Bukkit.getPlayer(uuid));
            });
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e){
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (isVanished(p)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onOrbPickup(PlayerPickupExperienceEvent e){
        Player p = e.getPlayer();
        if(isVanished(p)){
            e.setCancelled(true);
        }
    }

}
