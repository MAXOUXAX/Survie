package com.lyorine.survie.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import com.lyorine.survie.Main;

public class AFKParticules implements Runnable{

    private Main main;

    public AFKParticules(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        main.getAfkManager().getAfkPlayers().iterator().forEachRemaining(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation().clone().add(0, 2.5, 0), 10, 0, 0, 0, 0.01);
        });
    }
}
