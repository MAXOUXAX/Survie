package com.lyorine.survie.commands;

import com.lyorine.survie.utils.ItemBuilder;
import com.lyorine.survie.utils.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class CommandMenu implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;

            Inventory inv = Bukkit.createInventory(null, 9*1, Reference.INVSURVIE.getName());
            inv.addItem(new ItemBuilder(Material.ORANGE_BED).setName(Reference.INVSURVIE_SPAWN.getName()).setLore(Reference.INVSURVIE_SPAWNLORE.getName()).toItemStack());
            inv.addItem(new ItemBuilder(Material.REDSTONE_TORCH).setName(Reference.INVSURVIE_INFO.getName()).setLore(Reference.INVSURVIE_INFOLORE.getName()).toItemStack());
            //inv.addItem(new ItemBuilder(Material.EMERALD).setName(Reference.INVSURVIE_BOUTIQUE.getName()).setLore(Reference.INVSURVIE_BOUTIQUELORE.getName()).toItemStack());
            inv.addItem(new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName(Reference.INVSURVIE_EXP.getName()).setLore(Reference.INVSURVIE_EXPLORE.getNames()).toItemStack());
            inv.addItem(new ItemBuilder(Material.MAP).setName(Reference.INVSURVIE_RTP.getName()).setLore(Reference.INVSURVIE_RTPLORE.getNames()).toItemStack());
            //inv.addItem(new ItemBuilder(Material.END_PORTAL_FRAME).setName(Reference.INVSURVIE_END.getName()).setLore(Reference.INVSURVIE_ENDLORE.getName()).toItemStack());

            p.openInventory(inv);
        }
        return false;
    }

}
