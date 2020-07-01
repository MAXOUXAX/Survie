package com.lyorine.survie.managers;

import com.lyorine.survie.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicReference;

public class LagManager {

    private final Main main;
    private boolean startupLagging;
    private final int stableAfterSeconds;
    AtomicReference<Integer> iteration = new AtomicReference<>(0);
    private BukkitTask laggingTask;

    public LagManager(Main main) {
        this.main = main;
        this.startupLagging = true;
        this.stableAfterSeconds = Integer.parseInt(main.getConfig().getString("stableAfterSeconds"));
        startTask();
    }

    public boolean isStartupLagging() {
        return startupLagging;
    }

    public AtomicReference<Integer> getIteration() {
        return iteration;
    }

    public int getStableAfterSeconds() {
        return stableAfterSeconds;
    }

    public Main getMain() {
        return main;
    }

    public boolean resetLag() {
        if(!this.startupLagging) {
            this.startupLagging = true;
            startTask();
            return true;
        }
        return false;
    }

    public void startTask(){
        laggingTask = Bukkit.getScheduler().runTaskTimer(main, () -> {
            if(iteration.get() < stableAfterSeconds){
                iteration.set(iteration.get()+1);
            }else{
                startupLagging = false;
                iteration.set(0);
                laggingTask.cancel();
            }
        }, 20, 20);
    }
}
