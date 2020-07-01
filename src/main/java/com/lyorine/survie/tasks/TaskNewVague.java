package com.lyorine.survie.tasks;

import com.lyorine.survie.utils.raids.Raid;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskNewVague extends BukkitRunnable {

    private int iteration = 0;
    private Raid raid;

    public TaskNewVague(Raid raid) {
        this.raid = raid;
    }

    @Override
    public void run() {
        if(iteration == 100){
            raid.startNewVague();
            this.cancel();
        }else{
            iteration++;
            raid.getBossBar().setTitle("§cRAID §7- §eChargement de la §avague "+(raid.getCurrentVagueCount()+1));
            raid.getBossBar().setProgress(raid.scale(iteration, 0, 100, 0, 1));
        }
    }

}
