package com.lyorine.survie.commands;

import com.lyorine.survie.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandSurvie implements CommandExecutor {

    private final Main main;

    public CommandSurvie(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp())return false;
        if(args.length == 0){
            sendHelp(sender);
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){
                main.reloadConfig();
                sender.sendMessage("§aLa configuration a été rechargée !");
            }else if(args[0].equalsIgnoreCase("resetstartuplag")){
                if(main.getLagManager().resetLag()){
                    sender.sendMessage("§aLag de démarrage correctement réinitialisé");
                }else{
                    sender.sendMessage("§cImpossible de réinitialiser le lag de démarrage car il est déjà en cours...");
                }
            }
        }
        return false;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("/survie reload|resetstartuplag");
    }

}
