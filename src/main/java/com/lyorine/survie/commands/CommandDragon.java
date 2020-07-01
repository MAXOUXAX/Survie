package com.lyorine.survie.commands;

import com.lyorine.survie.Main;
import com.lyorine.survie.schedulers.DragonParticles;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class CommandDragon implements CommandExecutor {

    private Main main;
    private BukkitTask dragonParticles;

    public CommandDragon(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player) || !sender.isOp())return false;
        Player p = (Player) sender;
        if(args.length == 0){
            p.sendMessage("/dragon start|warn|health|particles");
        }else if(args.length == 1){
            String arg1 = args[0];
            if(arg1.equalsIgnoreCase("start")){
                Location loc = new Location(p.getWorld(), 1050.5, 27, 1209.5, -180, -0);
                Bukkit.getOnlinePlayers().forEach(o -> {
                    o.teleport(loc);
                    o.setMaxHealth(30);
                });
            }else if(arg1.equalsIgnoreCase("warn")){
                Bukkit.broadcastMessage("§c§lATTENTION !");
                Bukkit.broadcastMessage("§7» Le dragon a été §e§lMODIFIÉ §r§7afin d'être plus difficile");
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage("§7• §7Les §dEndCrystal §7ont été §a§lTOUS protégés§r§7, ils seront donc plus compliqués à détruire. Les détruire est nécessaire afin d'éviter que le dragon régénère sa vie");
                Bukkit.broadcastMessage("§7• §7Le dragon a §a§l20% de chance de vous attaquer après que vous l'aillez frapper");
                Bukkit.broadcastMessage("§7• §7Vous faites §c§l2 FOIS MOINS DE DÉGÂTS §7au dragon, peu importe l'item que vous utilisez !");
                Bukkit.broadcastMessage("§7• §7Le dragon a §a§lDEUX FOIS PLUS DE VIE§7, passant de §c200PV §7à §a§l400PV §r§7!");
                Bukkit.broadcastMessage("§7• §7Le dragon va §6§lCRACHER DE LA LAVE §r§7lorsqu'il s'approchera de son portail !");
                Bukkit.broadcastMessage("§7• §7Vous aurez §a§lTOUS 5 COEURS SUPPLÉMENTAIRES §r§7!");
                Bukkit.broadcastMessage("§7• §a§lVOUS NE PERDEREZ AUCUN ITEM, LORS DE VOTRE MORT, VOUS GARDEREZ VOTRE STUFF !");
                Bukkit.broadcastMessage("§7• §7Lors de sa mort, §a§lle dragon lâchera beaucoup plus d'XP§r§7 que la normale !");
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage("§a§lBonne chance à tous !");
            }else if(arg1.equalsIgnoreCase("health")){
                EnderDragon enderDragon = null;
                for (Entity nearbyEntity : p.getNearbyEntities(50, 50, 50)) {
                    if(nearbyEntity instanceof EnderDragon){
                        enderDragon = (EnderDragon) nearbyEntity;
                    }
                }
                if(enderDragon == null){
                    p.sendMessage("J'ai pas trouvé de dragon à côté de toi frérot");
                }else{
                    if(enderDragon.getMaxHealth() == 200){
                        enderDragon.setMaxHealth(400);
                    }
                    Bukkit.broadcastMessage("§d§lDragon §r§7» §7Le dragon a §e"+enderDragon.getHealth()+"PV§8/§a"+enderDragon.getMaxHealth()+"PV §7!");
                }
            }else if(arg1.equalsIgnoreCase("particles")){
                EnderDragon enderDragon = null;
                for (Entity nearbyEntity : p.getNearbyEntities(50, 50, 50)) {
                    if(nearbyEntity instanceof EnderDragon){
                        enderDragon = (EnderDragon) nearbyEntity;
                    }
                }
                if(enderDragon == null){
                    p.sendMessage("J'ai pas trouvé de dragon à côté de toi frérot");
                }else{
                    if(dragonParticles == null) {
                        dragonParticles = Bukkit.getScheduler().runTaskTimer(main, new DragonParticles(enderDragon), 20, 20);
                        p.sendMessage("launched");
                    }else{
                        p.sendMessage("Already launched");
                    }
                }
            }else if(arg1.equalsIgnoreCase("stopparticles")){
                dragonParticles.cancel();
                p.sendMessage("stopped");
            }
        }
        return false;
    }

    public BukkitTask getDragonParticles() {
        return dragonParticles;
    }
}
