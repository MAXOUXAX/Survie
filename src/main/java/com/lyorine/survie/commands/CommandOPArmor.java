package com.lyorine.survie.commands;

import com.lyorine.survie.Main;
import com.lyorine.survie.schedulers.OPArmorChanger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOPArmor implements CommandExecutor {

    private Main main;

    public CommandOPArmor(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player) || !sender.isOp())return false;
        Player p = (Player) sender;
        OPArmorChanger opArmorChanger = main.getOpArmorChanger();
        if(opArmorChanger.getArmorUuids().contains(p.getUniqueId())){
            opArmorChanger.removeArmorPlayer(p);
            p.sendMessage("§7» §cArmure désactivée !");
        }else{
            opArmorChanger.addArmorPlayer(p);
            p.sendMessage("§7» §aArmure activée !");
        }
        return false;
    }

}
