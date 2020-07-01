package com.lyorine.survie.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import com.lyorine.survie.Main;
import com.lyorine.survie.utils.Errors;

import java.util.ArrayList;
import java.util.UUID;

public class CommandAccess implements CommandExecutor {

    private Main main;

    public CommandAccess(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(p.isOp()) {
                if (args.length == 0) {
                    boolean isPrivate = main.getAccessManager().isPrivate();
                    TextComponent textComponent = new TextComponent("§7Le serveur est actuellement en mode§f: " + (isPrivate ? "§c§lPRIVÉ" : "§a§lLIVE"));
                    TextComponent textComponent1 = new TextComponent("§7Voici deux boutons vous permettants de choisir le mode du serveur§f: ");
                    TextComponent liveT = new TextComponent("§a§l[LIVE]");
                    liveT.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fCliquez ici pour mettre le serveur en mode §a§lLIVE").create()));
                    liveT.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/switch live"));
                    TextComponent privateT = new TextComponent("§c§l[PRIVÉ]");
                    privateT.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fCliquez ici pour mettre le serveur en mode §c§lPRIVÉ").create()));
                    privateT.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/switch private"));
                    ComponentBuilder compt = new ComponentBuilder(textComponent);
                    compt.append("\n").append(textComponent1).append("\n").append(liveT).append(" | ").append(privateT);
                    p.spigot().sendMessage(compt.create());
                }else{
                    String arg1 = args[0];
                    if(arg1.equalsIgnoreCase("live")){
                        if(main.getAccessManager().isPrivate()){
                            main.getAccessManager().setPrivate(false);
                            p.sendMessage("§7Le serveur est maintenant en mode §a§lLIVE");
                        }else{
                            p.sendMessage("§7Le serveur est déjà en mode §a§lLIVE");
                        }

                    }else if(arg1.equalsIgnoreCase("private")){
                        if(main.getAccessManager().isPrivate()){
                            p.sendMessage("§7Le serveur est déjà en mode §c§lPRIVATE");
                        }else{
                            main.getAccessManager().setPrivate(true);
                            p.sendMessage("§7Le serveur est maintenant en mode §c§lPRIVATE");
                        }
                    }
                    if(args.length == 1){
                        if(arg1.equalsIgnoreCase("add")){
                            p.sendMessage("§7/switch add <player>");
                        }else if(arg1.equalsIgnoreCase("remove")){
                            p.sendMessage("§7/switch remove <player>");
                        }else if(arg1.equalsIgnoreCase("list")){
                            ArrayList<UUID> uuids = main.getAccessManager().getUuidsAuthorized();
                            ArrayList<OfflinePlayer> ofPl = new ArrayList<>();
                            uuids.forEach(uuid -> {
                                ofPl.add(Bukkit.getOfflinePlayer(uuid));
                            });
                            p.sendMessage("§7Voici la liste des personnes qui ont §aaccès au serveur§7:");
                            StringBuilder str = new StringBuilder();
                            ofPl.forEach(offlinePlayer -> {
                                str.append("    §7- §a").append(offlinePlayer.getName()).append(" §7| §8").append(offlinePlayer.getUniqueId()).append("\n");
                            });
                            p.sendMessage(str.toString());
                        }
                    }else if(args.length == 2){
                        if(arg1.equalsIgnoreCase("add")){
                            String arg2 = args[1];
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arg2);
                            UUID uuid = offlinePlayer.getUniqueId();
                            Errors errors = main.getAccessManager().addAuthorizedPlayer(uuid);
                            if(errors == null){
                                p.sendMessage("§aJoueur bien ajouté !\n§7Pseudo§f: §a"+offlinePlayer.getName()+"\n§7UUID§f: §a"+uuid);
                            }else if(errors == Errors.ALREADYCONTAINS) {
                                p.sendMessage("§cCe joueur était déjà dans la liste autorisée !");
                            }
                        }else if(arg1.equalsIgnoreCase("remove")){
                            String arg2 = args[1];
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arg2);
                            UUID uuid = offlinePlayer.getUniqueId();
                            Errors errors = main.getAccessManager().removeAuthorizedPlayer(uuid);
                            if(errors == null){
                                p.sendMessage("§aJoueur bien retiré !\n§7Pseudo§f: §a"+offlinePlayer.getName()+"\n§7UUID§f: §a"+uuid);
                            }else if(errors == Errors.NOTCONTAINS) {
                                p.sendMessage("§cCe joueur n'est pas dans la liste autorisée !");
                            }
                        }
                    }
                }
            }
        }else{
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            if (args.length == 0) {
                boolean isPrivate = main.getAccessManager().isPrivate();
                console.sendMessage("Le serveur est actuellement en mode: " + (isPrivate ? "PRIVÉ" : "LIVE"));
                console.sendMessage("> /switch live pour mettre le serveur en mode LIVE");
                console.sendMessage("> /switch private pour mettre le serveur en mode PRIVÉ");
            }else{
                String arg1 = args[0];
                if(arg1.equalsIgnoreCase("live")){
                    if(main.getAccessManager().isPrivate()){
                        main.getAccessManager().setPrivate(false);
                        console.sendMessage("§7Le serveur est maintenant en mode §a§lLIVE");
                    }else{
                        console.sendMessage("§7Le serveur est déjà en mode §a§lLIVE");
                    }

                }else if(arg1.equalsIgnoreCase("private")){
                    if(main.getAccessManager().isPrivate()){
                        console.sendMessage("§7Le serveur est déjà en mode §c§lPRIVATE");
                    }else{
                        main.getAccessManager().setPrivate(true);
                        console.sendMessage("§7Le serveur est maintenant en mode §a§lLIVE");
                    }
                }

                if(args.length == 1){
                    if(arg1.equalsIgnoreCase("add")){
                        console.sendMessage("/switch add <player>");
                    }else if(arg1.equalsIgnoreCase("remove")){
                        console.sendMessage("/switch remove <player>");
                    }else if(arg1.equalsIgnoreCase("list")){
                        ArrayList<UUID> uuids = main.getAccessManager().getUuidsAuthorized();
                        ArrayList<OfflinePlayer> ofPl = new ArrayList<>();
                        uuids.forEach(uuid -> {
                            ofPl.add(Bukkit.getOfflinePlayer(uuid));
                        });
                        console.sendMessage("§7Voici la liste des personnes qui ont §aaccès au serveur§7:");
                        StringBuilder str = new StringBuilder();
                        ofPl.forEach(offlinePlayer -> {
                            str.append("    §7- §a").append(offlinePlayer.getName()).append(" §7| §8").append(offlinePlayer.getUniqueId()).append("\n");
                        });
                        console.sendMessage(str.toString());
                    }
                }else if(args.length == 2){
                    if(arg1.equalsIgnoreCase("add")){
                        String arg2 = args[1];
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arg2);
                        UUID uuid = offlinePlayer.getUniqueId();
                        Errors errors = main.getAccessManager().addAuthorizedPlayer(uuid);
                        if(errors == null){
                            console.sendMessage("Joueur bien ajouté !\nPseudo: "+offlinePlayer.getName()+"\nUUID: "+uuid);
                        }else if(errors == Errors.ALREADYCONTAINS) {
                            console.sendMessage("Ce joueur était déjà dans la liste autorisée !");
                        }
                    }else if(arg1.equalsIgnoreCase("remove")){
                        String arg2 = args[1];
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arg2);
                        UUID uuid = offlinePlayer.getUniqueId();
                        Errors errors = main.getAccessManager().removeAuthorizedPlayer(uuid);
                        if(errors == null){
                            console.sendMessage("§Joueur bien retiré !\nPseudo: "+offlinePlayer.getName()+"\nUUID: "+uuid);
                        }else if(errors == Errors.NOTCONTAINS) {
                            console.sendMessage("§Ce joueur n'est pas dans la liste autorisée !");
                        }
                    }
                }
            }
        }
        return true;
    }

}
