package com.lyorine.survie.schedulers;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class FireworkParticle implements Runnable{

    private World world;
    private Location loc;

    public FireworkParticle(World world, Location loc) {
        this.world = world;
        this.loc = loc;
    }

    @Override
    public void run() {
        world.spawnParticle(Particle.FIREWORKS_SPARK, loc, 5000, 80, 10, 80, 0);
    }

}
