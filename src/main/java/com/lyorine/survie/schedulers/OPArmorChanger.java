package com.lyorine.survie.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class OPArmorChanger implements Runnable{

    private ArrayList<UUID> armorUuids = new ArrayList<>();
    private HashMap<UUID, ArrayList<Color>> currentColors = new HashMap<>();

    @Override
    public void run() {
        armorUuids.forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            ArrayList<Color> colors = currentColors.get(p.getUniqueId());
            if(colors.isEmpty()){
                for (int i = 0; i < 4; i++) {
                    colors.add(randomColor());
                }
            }
            ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
            Color helmetColor = randomColor();
            colors.set(0, helmetColor);
            helmet.setItemMeta(colorArmor(helmet, helmetColor));

            ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            Color chestplateColor = colors.get(1);
            chestplate.setItemMeta(colorArmor(chestplate, chestplateColor));

            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
            Color leggingsColor = colors.get(2);
            leggings.setItemMeta(colorArmor(leggings, leggingsColor));

            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
            Color bootsColor = colors.get(3);
            boots.setItemMeta(colorArmor(boots, bootsColor));

            for (int i = 3; i > 0; i--) {
                colors.set(i, colors.get(i-1));
            }

            p.getEquipment().setHelmet(helmet);
            p.getEquipment().setChestplate(chestplate);
            p.getEquipment().setLeggings(leggings);
            p.getEquipment().setBoots(boots);
        });
    }

    public Color randomColor(){
        Random rand = new Random();
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        return Color.fromBGR(r, g, b);
    }

    public LeatherArmorMeta colorArmor(ItemStack item, Color color){
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) item.getItemMeta();
        leatherArmorMeta.setColor(color);
        return leatherArmorMeta;
    }

    public ArrayList<UUID> getArmorUuids() {
        return armorUuids;
    }

    public void removeArmorPlayer(Player p){
        armorUuids.remove(p.getUniqueId());
        currentColors.remove(p.getUniqueId());
        p.getEquipment().setHelmet(new ItemStack(Material.AIR));
        p.getEquipment().setChestplate(new ItemStack(Material.AIR));
        p.getEquipment().setLeggings(new ItemStack(Material.AIR));
        p.getEquipment().setBoots(new ItemStack(Material.AIR));
    }

    public void addArmorPlayer(Player p){
        armorUuids.add(p.getUniqueId());
        currentColors.put(p.getUniqueId(), new ArrayList<>());
    }
}
