package com.lyorine.survie.commands;

import com.lyorine.survie.Main;
import com.lyorine.survie.utils.ItemBuilder;
import com.lyorine.survie.utils.Reference;
import com.lyorine.survie.utils.raids.Difficulty;
import com.lyorine.survie.utils.raids.Raid;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class CommandRaid implements CommandExecutor, Listener {

    private Main main;

    public CommandRaid(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player) || !sender.isOp()){
            return false;
        }
        Raid raid = main.getRaidManager().getRaid();
        Player p = (Player) sender;
        if(args.length == 0){
            sendHelp(p);
        }else if(args.length == 1){
            String arg1 = args[0];
            if(arg1.equalsIgnoreCase("help")){
                sendHelp(p);
            }else if(arg1.equalsIgnoreCase("config")){
                openConfigMenu(p);
            }else if(arg1.equalsIgnoreCase("stop")){
                if(raid != null && raid.isStarted()) {
                    raid.stop();
                    main.getRaidManager().setRaid(null);
                    p.sendMessage("§c§lRaid §r§7» §aLe raid en cours a été arrêté !");
                }else{
                    p.sendMessage("§c§lRaid §r§7» §cAucun raid n'est lancé !");
                }
            }
        }
        return false;
    }

    public void openConfigMenu(Player p) {
        Raid raid = main.getRaidManager().getRaid();
        if(!raid.isStarted()) {

            Inventory inv = Bukkit.createInventory(null, 9, Reference.INVRAID.getName());

            inv.addItem(new ItemBuilder(raid.getDifficulty().getMaterial())
                    .setName(Reference.INVRAID_DIFFICULTY.getName() + raid.getDifficulty().getDisplayName())
                    .setLore(Reference.INVRAID_DIFFICULTYLORE.getNames())
                    .toItemStack());
            ItemStack it = new ItemBuilder(Material.ZOMBIE_SPAWN_EGG)
                    .setName(Reference.INVRAID_VAGUECOUNT.getName() + raid.getVagueCount())
                    .setLore(Reference.INVRAID_VAGUECOUNTLORE.getName())
                    .toItemStack();
            it.setAmount(raid.getVagueCount());
            inv.addItem(it);

            inv.setItem(8, new ItemBuilder(Material.LIME_CONCRETE)
                    .setName(Reference.INVRAID_START.getName())
                    .setLore(Reference.INVRAID_STARTLORE.getName())
                    .toItemStack());

            p.openInventory(inv);
        }else{
            p.sendMessage("§7» §cIl faut d'abord arrêter le RAID !");
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){

    }

    private void sendHelp(Player p) {
        p.sendMessage("§7§m------------------------------------");
        p.sendMessage("");
        p.sendMessage("§aRAID §7• §e© all rights reserved");
        p.sendMessage("");
        p.sendMessage("§7» §c/raid help §7- §ePermet d'afficher cette aide");
        p.sendMessage("§7» §c/raid config §7- §ePermet de configurer le raid (et le démarrer)");
        p.sendMessage("§7» §c/raid stop §7- §eAnnuler un raid en cours");
        p.sendMessage("");
        p.sendMessage("§7§m------------------------------------");
    }

}
