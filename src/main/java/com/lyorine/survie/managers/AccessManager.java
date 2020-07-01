package com.lyorine.survie.managers;

import org.bukkit.entity.Player;
import com.lyorine.survie.Main;
import com.lyorine.survie.utils.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccessManager {

    private Main main;
    private boolean isPrivate;
    private ArrayList<UUID> uuidsAuthorized = new ArrayList<>();

    public AccessManager(Main main) {
        this.main = main;
        this.isPrivate = main.getConfig().getBoolean("private");
        main.getConfig().getStringList("authorizedUuids").forEach(s -> uuidsAuthorized.add(UUID.fromString(s)));
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isAuthorized(Player p){
        return uuidsAuthorized.contains(p.getUniqueId());
    }

    public Errors addAuthorizedPlayer(UUID uuid){
        if(uuidsAuthorized.contains(uuid)){
            return Errors.ALREADYCONTAINS;
        }else{
            uuidsAuthorized.add(uuid);
            List<String> stringsUuids = new ArrayList<>();
            uuidsAuthorized.forEach(uuid1 -> stringsUuids.add(uuid1.toString()));
            main.getConfig().set("authorizedUuids", stringsUuids);
            return null;
        }
    }

    public Errors removeAuthorizedPlayer(UUID uuid){
        if(!uuidsAuthorized.contains(uuid)){
            return Errors.NOTCONTAINS;
        }else{
            uuidsAuthorized.remove(uuid);
            List<String> stringsUuids = new ArrayList<>();
            uuidsAuthorized.forEach(uuid1 -> stringsUuids.add(uuid1.toString()));
            main.getConfig().set("authorizedUuids", stringsUuids);
            return null;
        }
    }

    public ArrayList<UUID> getUuidsAuthorized() {
        return uuidsAuthorized;
    }

    public void setPrivate(boolean isPrivate){
        this.isPrivate = isPrivate;
        main.getConfig().set("private", this.isPrivate);
    }
}
