package com.lyorine.survie.listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BreakEvent implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block b = e.getBlock();
        ItemStack item = p.getInventory().getItemInMainHand();
        if(item.getItemMeta() != null && item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() >= 2) {
            String loreSecondLine = item.getItemMeta().getLore().get(1);
            if(loreSecondLine == null)return;


            if (loreSecondLine.equalsIgnoreCase("§7Pioche en §0§lcharbon")) {
                if (b.getType() == Material.COAL_ORE) {
                    e.setDropItems(false);
                    setDurability(item, getDurability(item)+1);
                    breakBlocks(item, Collections.singletonList(Material.COAL_ORE), b, new HashSet<>());
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 20, 1);
                } else {
                    e.setCancelled(true);
                    p.sendMessage("§7» §cVous pouvez utiliser cette pioche UNIQUEMENT pour casser du charbon !");
                }
            }else if(loreSecondLine.equalsIgnoreCase("§7Pioche en §9§llapis")){
                if (b.getType() == Material.IRON_ORE
                        || b.getType() == Material.LAPIS_ORE) {
                    e.setDropItems(false);
                    setDurability(item, getDurability(item)+1);
                    breakBlocks(item, Arrays.asList(Material.IRON_ORE,
                            Material.LAPIS_ORE), b, new HashSet<>());
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 20, 1);
                } else {
                    e.setCancelled(true);
                    p.sendMessage("§7» §cVous pouvez utiliser cette pioche UNIQUEMENT pour casser du fer ou du lapis !");
                }
            }else if(loreSecondLine.equalsIgnoreCase("§7Pioche en §a§lémeraude")){
                if (b.getType() == Material.DIAMOND_ORE
                        || b.getType() == Material.EMERALD_ORE
                        || b.getType() == Material.IRON_ORE
                        || b.getType() == Material.LAPIS_ORE
                        || b.getType() == Material.COAL_ORE
                        || b.getType() == Material.GOLD_ORE
                        || b.getType() == Material.REDSTONE_ORE
                        || b.getType() == Material.NETHER_QUARTZ_ORE) {
                    e.setDropItems(false);
                    setDurability(item, getDurability(item)+1);
                    breakBlocks(item, Arrays.asList(Material.DIAMOND_ORE,
                            Material.EMERALD_ORE,
                            Material.IRON_ORE,
                            Material.LAPIS_ORE,
                            Material.COAL_ORE,
                            Material.GOLD_ORE,
                            Material.REDSTONE_ORE,
                            Material.NETHER_QUARTZ_ORE), b, new HashSet<>());
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 20, 1);
                } else {
                    e.setCancelled(true);
                    p.sendMessage("§7» §cVous pouvez utiliser cette pioche UNIQUEMENT pour casser des minerais (diamants, émeraudes, fer, lapis, charbon, or, redstone, quartz) !");
                }
            }
        }
    }

    public void breakBlocks(ItemStack itemStackOfPlayer, List<Material> materials, Block anchor, HashSet<Block> collected){
        if(!materials.contains(anchor.getType())) return;
        if(collected.contains(anchor)) return;
        if(anchor.getDrops(itemStackOfPlayer).stream().noneMatch(itemStack -> itemStack.getType() == anchor.getType())){
            ExperienceOrb orb = (ExperienceOrb) anchor.getWorld().spawnEntity(anchor.getLocation(), EntityType.EXPERIENCE_ORB);
            orb.setExperience(collected.size()*5);
        }
        collected.add(anchor);
        anchor.breakNaturally(itemStackOfPlayer);
        anchor.getWorld().spawnParticle(Particle.CLOUD, anchor.getLocation(), 15, 0, 0, 0, 0.05);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.NORTH), collected);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.DOWN), collected);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.EAST), collected);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.WEST), collected);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.UP), collected);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.SOUTH), collected);
    }

    public ItemStack setDurability(ItemStack item, int durability){
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            ((Damageable) meta).setDamage(durability);
            item.setItemMeta(meta);
        }
        return item;
    }

    public int getDurability(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            return ((Damageable) meta).getDamage();
        }
        return item.getType().getMaxDurability();
    }

}
