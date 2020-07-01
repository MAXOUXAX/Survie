package com.lyorine.survie.managers;

import com.lyorine.survie.Main;
import com.lyorine.survie.tasks.TaskSleep;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SleepManager {

    private ArrayList<Player> playersInBed = new ArrayList<>();
    private String prefixWithoutArrow = "§9§lSommeil";
    private String prefix = prefixWithoutArrow+" §r§7» ";
    private double percentToSleep;
    private BukkitTask sleepTask;
    private Main main;

    public SleepManager(Main main) {
        this.main = main;
        this.percentToSleep = main.getConfig().getDouble("percentToSleep");
    }

    public ArrayList<Player> getPlayersInBed() {
        return playersInBed;
    }

    public void setPlayersInBed(ArrayList<Player> playersInBed) {
        this.playersInBed = playersInBed;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public double getPercentToSleep() {
        return percentToSleep;
    }

    public void setPercentToSleep(double percentToSleep) {
        this.percentToSleep = percentToSleep;
    }

    public BukkitTask getSleepTask() {
        return sleepTask;
    }

    public void setSleepTask(BukkitTask sleepTask) {
        this.sleepTask = sleepTask;
    }

    public void leaveBed(Player p, boolean quit) {
        if (quit) {
            if (!playersInBed.contains(p)) {
                return;
            /*}else{
                leaveBed(p, false);
            */}
        }
        playersInBed.remove(p);

        double playerInBedCounter = playersInBed.size();
        double playersOnline = Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR && player.getGameMode() != GameMode.CREATIVE).count();
        double percent = playerInBedCounter / playersOnline * 100;

        if (sleepTask != null) {
            if (percent >= percentToSleep) {
                Bukkit.broadcastMessage(prefix + "§c" + p.getName() + " §7a quitté son lit §7(§e" + new DecimalFormat("##").format(percent) + "% => " + new DecimalFormat("##").format(percentToSleep) + "%§7)");
            } else {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendTitle(prefixWithoutArrow, "§e"+p.getDisplayName()+" §7a revéillé tout le monde", 20, 40, 20);
                });
                cancelTask();
                Bukkit.broadcastMessage(prefix + "§c" + p.getName() + " §7a quitté son lit §7(§e" + new DecimalFormat("##").format(percent) + "% => " + new DecimalFormat("##").format(percentToSleep) + "%§7)");
            }
        } else {
            Bukkit.broadcastMessage(prefix + "§c" + p.getName() + " §7a quitté son lit §7(§e" + new DecimalFormat("##").format(percent) + "% => " + new DecimalFormat("##").format(percentToSleep) + "%§7)");
        }
    }

    public void joinBed(Player p) {
        playersInBed.add(p);
        if (playersInBed.size() == 1) {
            Bukkit.getOnlinePlayers().forEach(o -> {
                o.sendActionBar("§a" + p.getName() + " §eest fatigué(e) !");
            });
        }
        double playerInBedCounter = playersInBed.size();
        double playersOnline = Bukkit.getOnlinePlayers().stream().filter(player -> player.getGameMode() != GameMode.SPECTATOR && player.getGameMode() != GameMode.CREATIVE).count();

        double percent = playerInBedCounter / playersOnline * 100;
        Bukkit.broadcastMessage(prefix + "§a" + p.getName() + " §7a rejoint son lit §7(§e" + new DecimalFormat("##").format(percent) + "% => " + new DecimalFormat("##").format(percentToSleep) + "%§7)");
        if (percent >= percentToSleep) {
            if(sleepTask == null) {
                Bukkit.broadcastMessage(prefix + "§e" + new DecimalFormat("##").format(percent) + "% §adu serveur s'est couché ! "+ ChatColor.of("#3498db")+"Bonne nuit tout le monde !");

                playersInBed.forEach(player -> player.sendTitle("§9Bonne nuit...", "§8ZzZzZzZzZzZzZzZzZz.....", 10, 30, 10));

                World world = Bukkit.getWorld(main.getConfig().getString("worldName"));
                sleepTask = Bukkit.getScheduler().runTaskTimer(main, new TaskSleep(80, world, this), 1, 1);
            }
        }
    }

    public void endTask() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle(prefixWithoutArrow, "§aLe soleil se lève...", 20, 30, 60);
        });
        sleepTask.cancel();
        sleepTask = null;
        playersInBed.clear();
    }

    public void cancelTask(){
        sleepTask.cancel();
        sleepTask = null;
        playersInBed.forEach(Entity::leaveVehicle);
        Bukkit.getWorld(main.getConfig().getString("worldName")).setTime(13000);
    }
}
