package com.lyorine.survie.listeners;

import com.lyorine.survie.Main;
import com.lyorine.survie.managers.SleepManager;
import com.lyorine.survie.tasks.TaskSleep;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BedEvent implements Listener {

    private Main main;
    private SleepManager sleepManager;

    public BedEvent(Main main) {
        this.main = main;
        this.sleepManager = new SleepManager(main);
    }

    @EventHandler
    public void onGoToBed(PlayerBedEnterEvent e) {
        Player p = e.getPlayer();
        if (e.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            sleepManager.joinBed(p);
        }
    }

    @EventHandler
    public void onLeaveBed(PlayerBedLeaveEvent e) {
        Player p = e.getPlayer();
        if (!p.getWorld().isDayTime()) {
            sleepManager.leaveBed(p, false);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        sleepManager.leaveBed(p, true);
    }

}
