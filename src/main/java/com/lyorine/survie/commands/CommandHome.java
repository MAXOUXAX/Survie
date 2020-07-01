package com.lyorine.survie.commands;

import com.lyorine.survie.utils.Home;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import com.lyorine.survie.Main;
import com.lyorine.survie.utils.ItemBuilder;
import com.lyorine.survie.utils.Reference;

import java.util.ArrayList;

public class CommandHome implements CommandExecutor {

    private Main main;

    public CommandHome(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            ArrayList<Home> playerHomes = main.getHomeManager().getHomes(p);
            if (playerHomes.isEmpty()) {
                p.sendMessage("§7» Vous n'avez §caucun home §7!");
            }
            Inventory inv = Bukkit.createInventory(null, main.getInventoryUtils().getSizeOfInventoryFromSlotsNeeded(playerHomes.size() + 1), Reference.INVHOME.getName());
            playerHomes.forEach(home -> {
                inv.setItem(home.getSlot(), new ItemBuilder(home.getMaterial()).setName(Reference.INVHOME_HOME.getName() + home.getName()).setLore("§7Cliquez ici pour rejoindre votre home \"§6" + home.getName() + "§7\" !").toItemStack());
            });
            inv.addItem(new ItemBuilder(Material.CAMPFIRE).setName(Reference.INVHOME_MANAGE.getName()).setLore(Reference.INVHOME_MANAGELORE.getName()).toItemStack());
            p.openInventory(inv);
        }
        return false;
    }

}