package com.lyorine.survie.commands;

import com.lyorine.survie.managers.VanishManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandVanish implements CommandExecutor {

    private final VanishManager vanishManager;

    public CommandVanish(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player) || !sender.isOp())return false;
        Player p = (Player) sender;
        if(vanishManager.isVanished(p)){
            vanishManager.unvanishPlayer(p);
            p.sendMessage("§7» §cVous n'êtes plus invisible aux yeux des autres joueurs !");
        }else{
            vanishManager.vanishPlayer(p);
            p.sendMessage("§7» §aVous êtes invisible aux yeux des autres joueurs !");
        }
        return false;
    }

}
