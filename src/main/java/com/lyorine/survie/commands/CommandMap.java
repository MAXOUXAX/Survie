package com.lyorine.survie.commands;

import com.lyorine.survie.Main;
import com.lyorine.survie.utils.core.TaskRenderImage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMap implements CommandExecutor {

    private Main main;

    public CommandMap(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("render")) {
                        p.sendMessage("Rendering your image...");
                        String path = args[1];
                        new TaskRenderImage(main, p, path).runTask(main);
                    }
                } else {
                    p.sendMessage("§7» §c/map render <link>");
                }
            }
        }
        return false;
    }

}
