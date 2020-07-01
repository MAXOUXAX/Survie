package com.lyorine.survie.schedulers;

import com.lyorine.survie.Main;
import com.lyorine.survie.utils.EasyLocation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class AFKMonitor implements Runnable{

    private Main main;
    private HashMap<UUID, EasyLocation> locationHashMap = new HashMap<>();
    private HashMap<UUID, Integer> iterationHashMap = new HashMap<>();
    private final int secondsToBeAfk = 90;

    public AFKMonitor(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        locationHashMap.forEach((uuid, easyLocation) -> {
            Player p = Bukkit.getPlayer(uuid);
            if(new EasyLocation(p.getLocation()).toString().equalsIgnoreCase(easyLocation.toString())){
                if(main.getAfkManager().isAfk(p)){
                    p.sendTitle("§cAFK", "§7» §cVous êtes considéré(e) comme AFK.", 0, 40, 20);
                }else {
                    if(iterationHashMap.containsKey(uuid)){
                        int i = iterationHashMap.get(uuid);
                        if(i > secondsToBeAfk){
                            main.getAfkManager().setAfk(p, true);
                            iterationHashMap.remove(uuid);
                        }else{
                            iterationHashMap.put(uuid, i+1);
                        }
                    }else{
                        iterationHashMap.put(uuid, 1);
                    }
                }
            }else {
                if (main.getAfkManager().isAfk(p)) {
                    main.getAfkManager().setAfk(p, false);
                } else {
                    locationHashMap.put(uuid, new EasyLocation(p.getLocation()));
                    iterationHashMap.remove(uuid);
                }
            }
        });

    }

    public HashMap<UUID, EasyLocation> getLocationHashMap() {
        return locationHashMap;
    }

    public HashMap<UUID, Integer> getIterationHashMap() {
        return iterationHashMap;
    }
}
