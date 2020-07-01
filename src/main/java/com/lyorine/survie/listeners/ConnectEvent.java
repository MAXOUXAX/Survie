package com.lyorine.survie.listeners;

import com.lyorine.survie.Main;
import com.lyorine.survie.utils.EasyLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.logging.Level;

public class ConnectEvent implements Listener {

    private Main main;

    public ConnectEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e){
        Player p = e.getPlayer();
        if(main.getLagManager().isStartupLagging()){
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cVeuillez patienter encore §e"+(main.getLagManager().getStableAfterSeconds() - main.getLagManager().getIteration().get())+" secondes §c!");
            return;
        }
        if(main.getAccessManager().isPrivate()){
            if(!p.isOp()){
                e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "Désolé, vous n'êtes pas autorisé à rejoindre le serveur pour l'instant");
            }
        }else {
            if (!main.getAccessManager().isAuthorized(p)) {
                e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "Désolé, vous n'êtes pas autorisé à rejoindre le serveur pour l'instant");
                main.getLogger().log(Level.WARNING, p.getName() + " a tente de rejoindre le serveur !");
                Bukkit.broadcastMessage(p.getName() + " a tenté de rejoindre le serveur !");
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        sendWelcomeMessage(p);
        main.getAfkMonitor().getLocationHashMap().put(p.getUniqueId(), new EasyLocation(p.getLocation()));
        e.setJoinMessage("§7(§a+§7) §7"+p.getDisplayName());
    }

    private void sendWelcomeMessage(Player p) {
        p.sendMessage("§7§m------------------------------------");
        p.sendMessage("");
        p.sendMessage("§aSurvie §7• §e© all rights reserved");
        p.sendMessage("");
        p.sendMessage("§7Bienvenue sur le serveur §asurvie §7de la §6reine Lyorine §7!");
        p.sendMessage("§7Voici quelques commandes qui pourront t'être utiles§f:");
        p.sendMessage("");
        p.sendMessage("§7» §c/menu §7• §7Cette commande te permet d'ouvrir le menu du serveur qui te permet de te téléporter au spawn, et par la suite d'obtenir des informations sur le serveur, ainsi que d'ouvrir la boutique du serveur, qui te permettra par la suite d'échanger des cultures contre de la monnaie, et d'acheter des minerais et items avec cette même monnaie.");
        p.sendMessage("§7» §c/home §7• §7Cette commande te permet d'ouvrir le menu des homes pour créer, renommer, supprimer, ou bien te téléporter à un de tes homes !");
        p.sendMessage("");
        p.sendMessage("§c§lATTENTION§r§7, si tu es nouveau, merci de lire entièrement les §erègles §adisponibles au spawn §7!");
        p.sendMessage("");
        p.sendMessage("§7» §aSi tu rencontres quelconque problème, merci de contacter §aMAXOUXAX §asur le §9Discord §adisponible à cette adresse§f: §7https://go.lyorine.com/discord");
        p.sendMessage("");
        p.sendMessage("§7§m------------------------------------");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(main.getAfkManager().isAfk(p)) {
            main.getAfkManager().setAfk(p, false);
        }
        main.getAfkMonitor().getLocationHashMap().remove(p.getUniqueId());
        main.getAfkMonitor().getIterationHashMap().remove(p.getUniqueId());
        e.setQuitMessage("§7(§c-§7) §7"+p.getDisplayName());
    }

    @EventHandler
    public void onMotd(ServerListPingEvent e){
        if(main.getLagManager().isStartupLagging()){
            e.setMotd("§cVeuillez patienter avant de vous connecter !\n§7Temps restant: §e"+(main.getLagManager().getStableAfterSeconds() - main.getLagManager().getIteration().get())+" secondes");
            return;
        }
        String motd = Bukkit.getMotd();
        motd = motd.replace("{version}", main.getVersion().split("-")[0]);
        e.setMotd(motd);
    }

}
