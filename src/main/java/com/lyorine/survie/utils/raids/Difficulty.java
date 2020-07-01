package com.lyorine.survie.utils.raids;

import org.bukkit.Material;

public enum Difficulty {

    FACILE("§a§lFACILE", 1.5f, Material.LIME_WOOL),
    NORMAL("§e§lNORMAL", 2f, Material.YELLOW_WOOL),
    MOYEN("§6§lMOYEN", 3f, Material.ORANGE_WOOL),
    HARDCORE("§c§lHARDCORE", 5f, Material.RED_WOOL),
    EXTREME("§8§lEXTREME", 10f, Material.BLACK_WOOL),

    ;

    private String displayName;
    private float mobMultiplier;
    private Material material;

    Difficulty(String displayName, float mobMultiplier, Material material) {
        this.displayName = displayName;
        this.mobMultiplier = mobMultiplier;
        this.material = material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getMobMultiplier() {
        return mobMultiplier;
    }

    public Material getMaterial() {
        return material;
    }
}
