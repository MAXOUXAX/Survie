package com.lyorine.survie.schedulers;

import com.lyorine.survie.Main;
import org.bukkit.Bukkit;

import java.text.DecimalFormat;

public class TPSMonitor implements Runnable{

    private Main main;
    private boolean isLagging;
    private int lagTime;
    private double lowerTps;

    public TPSMonitor(Main main) {
        this.main = main;
        this.isLagging = false;
        this.lagTime = 0;
        this.lowerTps = 20d;
    }

    @Override
    public void run() {
        double actualTps = main.getServer().getTPS()[0];
        if(isLagging) {
            if (actualTps >= 18) {
                Bukkit.broadcastMessage("§7» §aNous sommes remontés au delà de §e18TPS §aaprès §e"+lagTime*5+" secondes §aet avec un §eTPS minimum §aà §e"+new DecimalFormat("##.##").format(lowerTps)+"TPS §a! §cSortie du mode lag §7("+new DecimalFormat("##.##").format(actualTps)+")");
                isLagging = false;
                lagTime = 0;
                lowerTps = 20d;
            }else{
                Bukkit.getOnlinePlayers().forEach(o -> {
                    o.sendActionBar("§cTPS actuel§f: §7"+new DecimalFormat("##.##").format(actualTps));
                });
                lagTime++;
                if(lowerTps > actualTps){
                    lowerTps = actualTps;
                }
            }
        }else{
            if(actualTps <= 18) {
                Bukkit.broadcastMessage("§c⚠ §c§lALERTE ROUGE §r§c⚠");
                Bukkit.broadcastMessage("§7» §cNous sommes désormais en train de laguer ! Nous sommes actuellement à §e" + new DecimalFormat("##.##").format(actualTps) + "TPS §c! §aEntrée en mode lag");
                isLagging = true;
                lowerTps = actualTps;
            }
        }
    }

}
