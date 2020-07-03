package com.lyorine.survie.utils;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;

public class BlockUtils {

    public static void breakBlocks(ItemStack itemStackOfPlayer, List<Material> materials, Block anchor, HashSet<Block> collected, boolean dropXp){
        if(!materials.contains(anchor.getType())) return;
        if(collected.contains(anchor)) return;

        if(dropXp && anchor.getDrops(itemStackOfPlayer).stream().noneMatch(itemStack -> itemStack.getType() == anchor.getType())){
            ExperienceOrb orb = (ExperienceOrb) anchor.getWorld().spawnEntity(anchor.getLocation(), EntityType.EXPERIENCE_ORB);
            orb.setExperience(collected.size()*5);
        }
        collected.add(anchor);
        anchor.breakNaturally(itemStackOfPlayer);
        anchor.getWorld().spawnParticle(Particle.CLOUD, anchor.getLocation(), 15, 0, 0, 0, 0.05);

        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.NORTH), collected, dropXp);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.DOWN), collected, dropXp);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.EAST), collected, dropXp);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.WEST), collected, dropXp);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.UP), collected, dropXp);
        breakBlocks(itemStackOfPlayer, materials, anchor.getRelative(BlockFace.SOUTH), collected, dropXp);
    }

}
