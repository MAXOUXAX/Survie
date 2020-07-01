package com.lyorine.survie.utils.raids;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Vague {

    private int currentMobCount;
    private int mobCount;
    private List<EntityType> mobs = new ArrayList<>();
    private List<Entity> currentEntities = new ArrayList<>();

    public Vague(int mobCount) {
        this.mobCount = mobCount;
    }

    public List<EntityType> getMobs() {
        return mobs;
    }

    public void craftVague(){
        for (int i = 0; i < mobCount; i++) {
            int rdm = new Random().nextInt(11);
            if(rdm == 1) {
                mobs.add(EntityType.PILLAGER);
            }else if(rdm == 2){
                mobs.add(EntityType.RAVAGER);
            }else if(rdm == 3){
                mobs.add(EntityType.VINDICATOR);
            }else if(rdm == 4){
                mobs.add(EntityType.EVOKER);
            }else if(rdm == 5){
                mobs.add(EntityType.WITCH);
            }else if(rdm == 6){
                mobs.add(EntityType.ZOMBIE);
            }else if(rdm == 7){
                mobs.add(EntityType.SKELETON);
            }else if(rdm == 8){
                mobs.add(EntityType.WITHER_SKELETON);
            }else if(rdm == 9){
                mobs.add(EntityType.SPIDER);
            }else if(rdm == 10){
                mobs.add(EntityType.ZOMBIFIED_PIGLIN);
            }
        }
    }

    public void spawnMonsters(Location loc){
        mobs.forEach(entity -> {
            Entity temp = loc.getWorld().spawnEntity(loc, entity);
            ((LivingEntity)temp).setRemoveWhenFarAway(false);
            temp.setCustomName("§cRAID");
            temp.setCustomNameVisible(false);
            ((LivingEntity)temp).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(500);
            this.currentEntities.add(temp);
            ((LivingEntity)temp).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*2000, 20, false, false));
        });
        this.mobs = null;
        this.setCurrentMobCount(this.currentEntities.size());
    }

    public boolean isFinished(){
        return currentMobCount == 0;
    }

    public void hasBeenKilled(Entity entity) {
        this.currentMobCount--;
        this.currentEntities.remove(entity);
    }

    public int getCurrentMobCount() {
        return currentMobCount;
    }

    public int getMobCount() {
        return mobCount;
    }

    public List<Entity> getCurrentEntities() {
        return currentEntities;
    }

    public void setCurrentMobCount(int currentMobCount) {
        this.currentMobCount = currentMobCount;
    }

    public void setMobCount(int mobCount) {
        this.mobCount = mobCount;
    }

    public void setMobs(List<EntityType> mobs) {
        this.mobs = mobs;
    }

    public void setCurrentEntities(List<Entity> currentEntities) {
        this.currentEntities = currentEntities;
    }
}