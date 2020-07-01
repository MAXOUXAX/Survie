package com.lyorine.survie.managers;

import com.lyorine.survie.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class AFKManager {

    private Main main;
    private ArrayList<UUID> afkPlayers = new ArrayList<>();

    public AFKManager(Main main) {
        this.main = main;
    }

    public ArrayList<UUID> getAfkPlayers() {
        return afkPlayers;
    }

    public boolean isAfk(Player p){
        return afkPlayers.contains(p.getUniqueId());
    }

    public void setAfk(Player p, boolean isAfk){
        if(isAfk) {
            afkPlayers.add(p.getUniqueId());
            away(p);
        }else{
            afkPlayers.remove(p.getUniqueId());
            back(p);
        }
    }

    public void resetAfks(){
        afkPlayers.forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            back(p);
        });
    }

    private void back(Player p) {
        p.sendTitle("§aPrésent", "§7Vous avez quitté(e) le mode AFK.", 10, 20, 10);
        p.playSound(p.getLocation(), Sound.ENTITY_FOX_SNIFF, 10f, 1f);
        p.setPlayerListName(null);
        p.setGlowing(false);
        Bukkit.broadcastMessage("§7» §e"+p.getDisplayName()+" §aest revenu(e) !");
    }

    private void away(Player p) {
        p.sendTitle("§cAFK", "§7Vous êtes entré(e) en mode AFK.", 20, 40, 20);
        p.playSound(p.getLocation(), Sound.ENTITY_FOX_SLEEP, 10f, 1f);
        p.setPlayerListName("§8§o"+p.getName());
        p.setGlowing(true);
        Bukkit.broadcastMessage("§7» §e"+p.getDisplayName()+" §cest désormais absent(e) !");
    }


}
