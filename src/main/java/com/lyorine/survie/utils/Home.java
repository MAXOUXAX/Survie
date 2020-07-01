package com.lyorine.survie.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.lyorine.survie.Main;

public class Home {

    private Location location;
    private Material material;
    private String name;
    private int slot;
    private Main main;

    public Home(Location location, Material material, String name, Main main, int slot) {
        this.location = location;
        this.material = material;
        this.name = name;
        this.main = main;
        this.slot = slot;
    }

    public Home(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public void rename(String newName){
        this.name = newName;
    }

    public void teleport(Player p) {
        main.getTeleportationManager().teleportPlayerWithoutMoving(p, getLocation(), 5);
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
