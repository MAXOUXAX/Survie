package com.lyorine.survie.schedulers;

import com.lyorine.survie.Main;
import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class TablistRunnable implements Runnable{

    private Main main;
    private Runtime runtime;

    public TablistRunnable(Main main, Runtime runtime) {
        this.main = main;
        this.runtime = runtime;
    }

    @Override
    public void run() {
        updateTabLists();
    }

    public int getPing(Player who){
        return ((CraftPlayer)who).getHandle().ping;
    }

    public String getColorFromValue(double value, double max, boolean reversed){
        double percent = value / max * 100;
        if(reversed){
            percent = 100-percent;
        }
        if(percent < 15){
            return "§2";
        }
        if(percent <= 25){
            return "§a";
        }
        if(percent <= 50){
            return "§e";
        }
        if(percent <= 75){
            return "§6";
        }
        if(percent <= 100){
            return "§c";
        }else{
            return "§4";
        }
    }

    public void updateTabLists() {
        long maxMem = runtime.maxMemory() / 1048576L; //TOTAL
        long usedMem = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L; //USED

        double cpuUsage = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad()*100;

        double tps = Bukkit.getTPS()[0];

        Bukkit.getOnlinePlayers().forEach(o -> {
            int ping = getPing(o);

            o.setPlayerListHeaderFooter("\n" +
                    "§a§lSurvie §ev" + main.getVersion() + "" +
                    "\n" +
                    "\n§7En ligne: §e" + Bukkit.getOnlinePlayers().size() + "" +
                    "\n§7Votre ping: "+getColorFromValue(ping, 1000, false) + ping + "ms" +
                    "\n§7Processeur: "+getColorFromValue(cpuUsage, 100, false) + new DecimalFormat("##.##").format(cpuUsage) + "%" +
                    "\n§7TPS du serveur: "+getColorFromValue(tps, 20, true) + new DecimalFormat("##.##").format(tps) + "" +
                    "\n§7Mémoire: "+getColorFromValue(usedMem, maxMem, false) + new DecimalFormat("##.##").format(usedMem) + "§7MB§8/§a" + new DecimalFormat("##.##").format(maxMem) + "§7MB" +
                    "\n ", "" +
                    "\n§ego.lyorine.com/roadmap" +
                    "\n§7Serveur développé par §cMaxouxax");
        });
    }
}
