package com.lyorine.survie.commands;

import com.lyorine.survie.Main;
import com.lyorine.survie.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;
import com.lyorine.survie.schedulers.FireworkParticle;

public class CommandFirework implements CommandExecutor {

    private Main main;
    private BukkitTask bukkitTask;

    public CommandFirework(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.isOp() && p.getUniqueId().toString().equalsIgnoreCase("f829cd4a-ae28-4c19-999e-91dc6439f3c3")){
                p.sendMessage("§7» §7Tu as été identifié !");
                if(args.length == 0) {
                    openFireworkMenu(p);
                }else{
                    if(args[0].equalsIgnoreCase("add")){
                        Location addLoc = p.getLocation().clone().add(0, -1, 0);
                        main.getFireworkManager().addLoc(addLoc);
                        p.sendMessage("§7» Location §aajoutée §7!\n§7x: "+addLoc.getX()+", y:"+addLoc.getY()+", z:"+addLoc.getZ());
                    }else if(args[0].equalsIgnoreCase("clear")){
                        main.getFireworkManager().getLocations().clear();
                        p.sendMessage("§7» Locations §csupprimées §7!");
                    }else if(args[0].equalsIgnoreCase("reload")){
                        main.getFireworkManager().reloadLocations();
                        p.sendMessage("§7» Locations §csupprimées §7!");
                    }else if(args[0].equalsIgnoreCase("10")){
                        Bukkit.getOnlinePlayers().forEach(o -> {
                            o.sendTitle("§cAttention", "§7Dans §a10 minutes§7, le §afeu d'artifice §7débutera !", 60, 20, 60);
                        });
                    }else if(args[0].equalsIgnoreCase("5")){
                        Bukkit.getOnlinePlayers().forEach(o -> {
                            o.sendTitle("§cAttention", "§7Dans §a5 minutes§7, le §afeu d'artifice §7débutera !", 60, 20, 60);
                        });
                    }else if(args[0].equalsIgnoreCase("1")){
                        Bukkit.getOnlinePlayers().forEach(o -> {
                            o.sendTitle("§cAttention", "§7Dans §a1 minute§7, le §afeu d'artifice §7débutera !", 60, 20, 60);
                        });
                    }else if(args[0].equalsIgnoreCase("30s")){
                        Bukkit.getOnlinePlayers().forEach(o -> {
                            o.sendTitle("§cAttention", "§7Dans §a30 secondes§7, le §afeu d'artifice §7débutera !", 60, 20, 60);
                        });
                    }else if(args[0].equalsIgnoreCase("10s")){
                        Bukkit.getOnlinePlayers().forEach(o -> {
                            o.sendTitle("§e10 secondes", "§7Dans §a10 secondes§7, le §afeu d'artifice §7débutera !", 40, 20, 40);
                        });
                    }else if(args[0].equalsIgnoreCase("go")){
                        Bukkit.getOnlinePlayers().forEach(o -> {
                            o.sendTitle("§aC'est parti !", "§aLe feu d'artifice débute !", 20, 30, 20);
                        });
                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 10000f, 1f);
                        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(main, new FireworkParticle(p.getWorld(), p.getLocation()), 10, 10);
                    }else if(args[0].equalsIgnoreCase("warn")){
                        Bukkit.getOnlinePlayers().forEach(o -> {
                            o.sendMessage("§7» §7Merci de §cne pas §7rejoindre la base de tir sous peine d'un §cbannissement !");
                        });
                    }else if(args[0].equalsIgnoreCase("stop")){
                        Bukkit.getOnlinePlayers().forEach(o -> {
                            o.sendTitle("§cFin", "§cLe feu d'artifice est terminé !", 20, 60, 20);
                        });
                        p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation(), 10000, 80, 10, 80, 1);
                        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> p.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, p.getLocation(), 2000, 80, 10, 80, 0), 60);
                        bukkitTask.cancel();
                    }
                }
            }
        }
        return false;
    }

    private void openFireworkMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9*6, "§7» §eFeu d'artifice");

        inv.addItem(new ItemBuilder(Material.MAP).setName("§7All left & all right").toItemStack());
        inv.addItem(new ItemBuilder(Material.BLUE_DYE).setName("§7AL §9Bleu").toItemStack());
        inv.addItem(new ItemBuilder(Material.WHITE_DYE).setName("§7AL §fBlanc").toItemStack());
        inv.addItem(new ItemBuilder(Material.RED_DYE).setName("§7AL §cRouge").toItemStack());
        inv.addItem(new ItemBuilder(Material.ORANGE_DYE).setName("§7AL §6Orange").toItemStack());
        inv.addItem(new ItemBuilder(Material.LIME_DYE).setName("§7AL §aVert").toItemStack());
        inv.addItem(new ItemBuilder(Material.YELLOW_DYE).setName("§7AL §eJaune").toItemStack());
        inv.addItem(new ItemBuilder(Material.PINK_DYE).setName("§7AL §dRose").toItemStack());
        inv.addItem(new ItemBuilder(Material.GRAY_DYE).setName("§7AL §aC§co§9l§6o§br§df§eu§2l").toItemStack());

        inv.addItem(new ItemBuilder(Material.SPECTRAL_ARROW).setName("§7Middle burst").toItemStack());
        inv.addItem(new ItemBuilder(Material.BLUE_DYE).setName("§7M §9Bleu").toItemStack());
        inv.addItem(new ItemBuilder(Material.WHITE_DYE).setName("§7M §fBlanc").toItemStack());
        inv.addItem(new ItemBuilder(Material.RED_DYE).setName("§7M §cRouge").toItemStack());
        inv.addItem(new ItemBuilder(Material.ORANGE_DYE).setName("§7M §6Orange").toItemStack());
        inv.addItem(new ItemBuilder(Material.LIME_DYE).setName("§7M §aVert").toItemStack());
        inv.addItem(new ItemBuilder(Material.YELLOW_DYE).setName("§7M §eJaune").toItemStack());
        inv.addItem(new ItemBuilder(Material.PINK_DYE).setName("§7M §dRose").toItemStack());
        inv.addItem(new ItemBuilder(Material.GRAY_DYE).setName("§7M §aC§co§9l§6o§br§df§eu§2l").toItemStack());

        inv.addItem(new ItemBuilder(Material.SNOWBALL).setName("§7High big").toItemStack());
        inv.addItem(new ItemBuilder(Material.BLUE_DYE).setName("§7B §9Bleu").toItemStack());
        inv.addItem(new ItemBuilder(Material.WHITE_DYE).setName("§7B §fBlanc").toItemStack());
        inv.addItem(new ItemBuilder(Material.RED_DYE).setName("§7B §cRouge").toItemStack());
        inv.addItem(new ItemBuilder(Material.ORANGE_DYE).setName("§7B §6Orange").toItemStack());
        inv.addItem(new ItemBuilder(Material.LIME_DYE).setName("§7B §aVert").toItemStack());
        inv.addItem(new ItemBuilder(Material.YELLOW_DYE).setName("§7B §eJaune").toItemStack());
        inv.addItem(new ItemBuilder(Material.PINK_DYE).setName("§7B §dRose").toItemStack());
        inv.addItem(new ItemBuilder(Material.GRAY_DYE).setName("§7B §aC§co§9l§6o§br§df§eu§2l").toItemStack());

        inv.addItem(new ItemBuilder(Material.NETHER_STAR).setName("§7High flicker").toItemStack());
        inv.addItem(new ItemBuilder(Material.BLUE_DYE).setName("§7F §9Bleu").toItemStack());
        inv.addItem(new ItemBuilder(Material.WHITE_DYE).setName("§7F §fBlanc").toItemStack());
        inv.addItem(new ItemBuilder(Material.RED_DYE).setName("§7F §cRouge").toItemStack());
        inv.addItem(new ItemBuilder(Material.ORANGE_DYE).setName("§7F §6Orange").toItemStack());
        inv.addItem(new ItemBuilder(Material.LIME_DYE).setName("§7F §aVert").toItemStack());
        inv.addItem(new ItemBuilder(Material.YELLOW_DYE).setName("§7F §eJaune").toItemStack());
        inv.addItem(new ItemBuilder(Material.PINK_DYE).setName("§7F §dRose").toItemStack());
        inv.addItem(new ItemBuilder(Material.GRAY_DYE).setName("§7F §aC§co§9l§6o§br§df§eu§2l").toItemStack());

        inv.addItem(new ItemBuilder(Material.CREEPER_HEAD).setName("§7High creeper").toItemStack());
        inv.addItem(new ItemBuilder(Material.BLUE_DYE).setName("§7C §9Bleu").toItemStack());
        inv.addItem(new ItemBuilder(Material.WHITE_DYE).setName("§7C §fBlanc").toItemStack());
        inv.addItem(new ItemBuilder(Material.RED_DYE).setName("§7C §cRouge").toItemStack());
        inv.addItem(new ItemBuilder(Material.ORANGE_DYE).setName("§7C §6Orange").toItemStack());
        inv.addItem(new ItemBuilder(Material.LIME_DYE).setName("§7C §aVert").toItemStack());
        inv.addItem(new ItemBuilder(Material.YELLOW_DYE).setName("§7C §eJaune").toItemStack());
        inv.addItem(new ItemBuilder(Material.PINK_DYE).setName("§7C §dRose").toItemStack());
        inv.addItem(new ItemBuilder(Material.GRAY_DYE).setName("§7C §aC§co§9l§6o§br§df§eu§2l").toItemStack());

        inv.addItem(new ItemBuilder(Material.CAMPFIRE).setName("§7Massive fade").toItemStack());
        inv.addItem(new ItemBuilder(Material.BLUE_DYE).setName("§7MF §9Bleu").toItemStack());
        inv.addItem(new ItemBuilder(Material.WHITE_DYE).setName("§7MF §fBlanc").toItemStack());
        inv.addItem(new ItemBuilder(Material.RED_DYE).setName("§7MF §cRouge").toItemStack());
        inv.addItem(new ItemBuilder(Material.ORANGE_DYE).setName("§7MF §6Orange").toItemStack());
        inv.addItem(new ItemBuilder(Material.LIME_DYE).setName("§7MF §aVert").toItemStack());
        inv.addItem(new ItemBuilder(Material.YELLOW_DYE).setName("§7MF §eJaune").toItemStack());
        inv.addItem(new ItemBuilder(Material.PINK_DYE).setName("§7MF §dRose").toItemStack());
        inv.addItem(new ItemBuilder(Material.GRAY_DYE).setName("§7MF §aC§co§9l§6o§br§df§eu§2l").toItemStack());

        p.openInventory(inv);
    }

}
