package com.lyorine.survie.utils.raids;

import com.lyorine.survie.Main;
import com.lyorine.survie.tasks.TaskStartRaid;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Raid {

    public Long startTime;
    public Vague currentVague;
    public int currentVagueCount = 0;
    public int vagueCount = 0;
    public Difficulty difficulty;
    public Location startLocation;
    public BossBar bossBar;
    public boolean started;
    public Main main;

    public Raid(Main main) {
        this.main = main;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setCurrentVague(Vague currentVague) {
        this.currentVague = currentVague;
    }

    public void setCurrentVagueCount(int currentVagueCount) {
        this.currentVagueCount = currentVagueCount;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Vague getCurrentVague() {
        return currentVague;
    }

    public int getCurrentVagueCount() {
        return currentVagueCount;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public int getVagueCount() {
        return vagueCount;
    }

    public void setVagueCount(int vagueCount) {
        this.vagueCount = vagueCount;
    }

    public void loadRaid(){
        this.started = true;
        this.bossBar = Bukkit.createBossBar("RAID", BarColor.BLUE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
        this.bossBar.setVisible(true);
        Bukkit.getOnlinePlayers().forEach(o -> {
            this.bossBar.addPlayer(o);
            o.playSound(o.getLocation(), Sound.EVENT_RAID_HORN, 2000, 1);
            o.sendTitle("§c§lRaid", "§eDémarrage...", 20,60,20);
        });
        Bukkit.broadcastMessage("§c§lRaid §r§7» §aDémarrage d'un RAID en §edifficulté \""+getDifficulty().getDisplayName()+"§e\" §aet avec §e"+getVagueCount()+" vagues §a!");
        new TaskStartRaid(this).runTaskTimer(main, 1, 1);
    }

    public void startRaid(){
        this.getStartLocation().getWorld().setTime(18000);
        startNewVague();
    }

    public void startNewVague() {
        this.currentVagueCount++;
        this.currentVague = new Vague(Math.round(currentVagueCount *difficulty.getMobMultiplier()));
        this.currentVague.craftVague();
        Bukkit.broadcastMessage("§c§lRaid §r§7» §eLancement de la §a"+ currentVagueCount +"eme vague §esur §a"+vagueCount+" §e!");

        int rX = new Random().nextInt(50);
        int rZ = new Random().nextInt(50);
        int rXMinus = startLocation.clone().add(-rX,0,0).getBlockX();
        int rXPlus = startLocation.clone().add(rX,0,0).getBlockX();
        int rZMinus = startLocation.clone().add(0,0,-rZ).getBlockZ();
        int rZPlus = startLocation.clone().add(0,0,rZ).getBlockZ();
        int x = ThreadLocalRandom.current().nextInt(rXMinus, rXPlus+1);
        int z = ThreadLocalRandom.current().nextInt(rZMinus, rZPlus+1);
        int y = startLocation.getWorld().getHighestBlockYAt(x, z);

        Location loc = new Location(startLocation.getWorld(), x,y,z);

        currentVague.spawnMonsters(loc);
        Bukkit.broadcastMessage("§c§lRaid §r§7» §aLes monstres sont apparus au nombre de §e"+currentVague.getCurrentMobCount()+" §e!");
        this.updateBossBar();
        Bukkit.getOnlinePlayers().forEach(o -> {
            o.teleport(loc);
            o.playSound(o.getLocation(), Sound.EVENT_RAID_HORN, 2000, 1);
        });
    }

    public void victory() {
        this.getCurrentVague().getCurrentEntities().forEach(Entity::remove);
        this.getBossBar().removeAll();
        this.bossBar = null;
        this.currentVague = null;
        this.started = false;
        long ms = System.currentTimeMillis() - this.getStartTime();
        double s = ms/1000D;
        Bukkit.broadcastMessage("§c§lRaid §r§7» §6§lVous avez gagné après "+new DecimalFormat("##.##").format(s)+" seconde(s) !");
        Bukkit.getOnlinePlayers().forEach(o -> {
            o.playSound(o.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2000, 1);
            o.sendTitle("§c§lRaid", "§aVictoire", 20,40,20);
        });
        this.getStartLocation().getWorld().setTime(2000);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void updateBossBar() {
        int max = this.currentVague.getMobCount();
        int current = this.currentVague.getCurrentMobCount();
        this.bossBar.setTitle("§cRAID §7- §eVague "+this.currentVagueCount +" §7- §eMob(s) restant(s): "+this.currentVague.getCurrentMobCount());
        this.bossBar.setProgress(scale(current, 0, max, 0, 1));
        if(max <= 6){
            this.bossBar.setStyle(BarStyle.SEGMENTED_6);
        }else if(max <= 10){
            this.bossBar.setStyle(BarStyle.SEGMENTED_10);
        }else if(max <= 12){
            this.bossBar.setStyle(BarStyle.SEGMENTED_12);
        }else if(max <= 20){
            this.bossBar.setStyle(BarStyle.SEGMENTED_20);
        }else{
            this.bossBar.setStyle(BarStyle.SOLID);
        }
    }

    public double scale(double num, double in_min, double in_max, double out_min, double out_max){
        return (num - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public boolean isStarted() {
        return started;
    }

    public void stop() {
        this.getCurrentVague().getCurrentEntities().forEach(Entity::remove);
        this.getBossBar().removeAll();
        this.bossBar = null;
        this.currentVague = null;
        this.started = false;
        Bukkit.broadcastMessage("§c§lRaid §r§7» §cRaid annulé !");
        this.getStartLocation().getWorld().setTime(2000);
    }

}