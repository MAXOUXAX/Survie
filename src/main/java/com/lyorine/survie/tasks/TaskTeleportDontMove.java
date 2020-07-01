package com.lyorine.survie.tasks;

import com.lyorine.survie.Main;
import com.lyorine.survie.utils.raids.Raid;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskTeleportDontMove extends BukkitRunnable {

    private Location currentLocation;
    private Location teleportLocation;
    private Player player;
    private Main main;

    public TaskTeleportDontMove(Location currentLocation, Location teleportLocation, Player player, Main main) {
        this.currentLocation = currentLocation;
        this.teleportLocation = teleportLocation;
        this.player = player;
        this.main = main;
    }

    @Override
    public void run() {
        main.getTeleportationManager().hasBeenTeleported(player);
        if(main.equivalentLocation(currentLocation, player.getLocation())){
            player.teleport(teleportLocation);
            player.sendMessage("§7» §aVous avez bien été téléporté !");
        }else{
            player.sendMessage("§7» §cTéléportation annulée, vous vous êtes déplacé !");
        }
    }

}
