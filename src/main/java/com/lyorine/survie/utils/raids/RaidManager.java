package com.lyorine.survie.utils.raids;

import com.lyorine.survie.Main;
import com.lyorine.survie.tasks.TaskNewVague;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

public class RaidManager implements Listener {

    private Raid raid;
    private Main main;

    public RaidManager(Main main) {
        this.main = main;
    }

    public void setRaid(Raid raid) {
        this.raid = raid;
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e){
        Entity entity = e.getEntity();
        if(raid != null && raid.isStarted()) {
            Vague vague = raid.getCurrentVague();
            if (entity.getCustomName() != null) {
                if (entity.getCustomName().equalsIgnoreCase("§cRAID")) {
                    vague.hasBeenKilled(entity);
                    Bukkit.getOnlinePlayers().forEach(o -> o.removePotionEffect(PotionEffectType.BAD_OMEN));
                    entity.getLocation().getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 200, 1, 1, 1, 0.1);
                    raid.updateBossBar();
                    if (vague.isFinished()) {
                        raid.getStartLocation().getWorld().getNearbyEntities(raid.getStartLocation(), 200,200,200).stream().filter(entity1 -> entity1.getType() == EntityType.VEX).forEach(Entity::remove);
                        if((raid.getCurrentVagueCount()+1) <= raid.getVagueCount()) {
                            new TaskNewVague(raid).runTaskTimer(main, 1, 1);
                        }else{
                            raid.victory();
                        }
                    }
                }
            }
        }
    }

    public Raid getRaid() {
        if(raid == null) {
            this.raid = new Raid(main);
            this.raid.setVagueCount(5);
            this.raid.setStartTime(System.currentTimeMillis());
            this.raid.setDifficulty(Difficulty.FACILE);
        }
        return raid;
    }
}
