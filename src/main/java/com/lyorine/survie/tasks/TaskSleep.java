package com.lyorine.survie.tasks;

import com.lyorine.survie.managers.SleepManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class TaskSleep implements Runnable {

    private int ticks = 0;
    private int ticksDuration;
    private int ticksPerTicks;
    private SleepManager sleepManager;
    private World world;

    public TaskSleep(int ticksDuration, World world, SleepManager sleepManager) {
        this.ticksDuration = ticksDuration;
        this.world = world;
        this.ticksPerTicks = Math.toIntExact((24000 - world.getTime()) / ticksDuration);
        this.sleepManager = sleepManager;
    }

    @Override
    public void run() {
        if(ticks <= ticksDuration){
            world.setFullTime(world.getFullTime()+ticksPerTicks);
            ticks++;
        }else{
            endTask();
        }
    }

    private void endTask() {
        sleepManager.endTask();
    }

}
