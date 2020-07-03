package com.lyorine.survie.listeners;

import com.lyorine.survie.utils.BlockUtils;
import com.lyorine.survie.utils.Reference;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TimberAxeEvent implements Listener {

    private Set<Material> woods = EnumSet.of(Material.ACACIA_LOG,
            Material.BIRCH_LOG,
            Material.DARK_OAK_LOG,
            Material.JUNGLE_LOG,
            Material.OAK_LOG,
            Material.SPRUCE_LOG,
            Material.WARPED_STEM,
            Material.CRIMSON_STEM
            );//ui mais apres c pas grave tant que ca casse les tronc comme les gros arbre

    @EventHandler
    public void onAxeTimberUsed(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (isTimberAxe(p.getInventory().getItemInMainHand())) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            Block b = e.getBlock();
            if (woods.contains(e.getBlock().getType())) {
                BlockUtils.breakBlocks(item, new ArrayList<>(woods), b, new HashSet<>(), false);
            }
        }
    }

    public boolean isTimberAxe(ItemStack item){
        if (item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
            String loreSecondLine = item.getItemMeta().getLore().get(1);
            if (loreSecondLine == null) return false;
            if (loreSecondLine.equalsIgnoreCase(Reference.TIMBERAXE_LORE.getName())) {
                return true;
            }
        }
        return false;
    }
}

