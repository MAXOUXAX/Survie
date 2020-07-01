package com.lyorine.survie.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class EasyLocation {

    private World world;
    private double x;
    private double y;
    private double z;

    public EasyLocation(Location loc) {
        this.world = loc.getWorld();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
    }

    public String toString(){
        return world.getName()+x+y+z;
    }

}