package com.lyorine.survie.schedulers;

import org.bukkit.Particle;
import org.bukkit.entity.EnderDragon;

public class DragonParticles implements Runnable{

    EnderDragon enderDragon = null;

    public DragonParticles(EnderDragon enderDragon) {
        this.enderDragon = enderDragon;
    }

    public void setEnderDragon(EnderDragon enderDragon) {
        this.enderDragon = enderDragon;
    }

    @Override
    public void run() {
        if(enderDragon != null){
            enderDragon.getWorld().spawnParticle(Particle.FLAME, enderDragon.getLocation(), 50, 3, 3, 3 ,0.2);
        }else{
        }
    }

}
