package com.lyorine.survie.managers;

import com.lyorine.survie.Main;
import com.lyorine.survie.tasks.TaskTeleportDontMove;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class TeleportationManager {

    private Main main;
    private ArrayList<UUID> uuidsInTeleportation = new ArrayList<>();

    public TeleportationManager(Main main) {
        this.main = main;
    }

    public void teleportPlayerWithoutMoving(Player p, Location location, int seconds){
        if(uuidsInTeleportation.contains(p.getUniqueId())){
            p.sendMessage("§7» §cUne téléportation est déjà en cours...");
            return;
        }
        Bukkit.getScheduler().runTaskLater(main, () -> main.getChunkWithinRadius(location.getChunk(), 3).forEach(chunk -> {
            main.loadChunk(p, chunk);
        }), 20*seconds/2);
        p.sendMessage("§7» §eVous allez être téléporté dans §a"+seconds+" seconde(s) §e!");
        uuidsInTeleportation.add(p.getUniqueId());
        new TaskTeleportDontMove(p.getLocation(), location, p, main).runTaskLater(main, seconds*20);
    }

    public void hasBeenTeleported(Player player){
        uuidsInTeleportation.remove(player.getUniqueId());
    }

}
