package com.lyorine.survie.utils.core;

import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.UUID;

public class ImageMap {

    private UUID uuid;
    private String path;
    private ArrayList<Integer> mapIds;

    public ImageMap(UUID uuid, String path, ArrayList<Integer> mapIds) {
        this.uuid = uuid;
        this.path = path;
        this.mapIds = mapIds;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPath() {
        return path;
    }

    public ArrayList<Integer> getMapIds() {
        return mapIds;
    }
}
