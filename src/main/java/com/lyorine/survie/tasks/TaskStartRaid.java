package com.lyorine.survie.tasks;

import com.lyorine.survie.utils.raids.Raid;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskStartRaid extends BukkitRunnable {

    private int iteration = 0;
    private Raid raid;

    public TaskStartRaid(Raid raid) {
        this.raid = raid;
    }

    @Override
    public void run() {
        if(iteration == 200){
            raid.startRaid();
            this.cancel();
        }else{
            iteration++;
            raid.getBossBar().setTitle("§cRAID §7- §eDémarrage du RAID");
            raid.getBossBar().setProgress(raid.scale(iteration, 0, 200, 0, 1));
        }
    }

}
