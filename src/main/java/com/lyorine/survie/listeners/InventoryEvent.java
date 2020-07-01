package com.lyorine.survie.listeners;

import com.lyorine.survie.utils.Glow;
import com.lyorine.survie.utils.Home;
import com.lyorine.survie.utils.raids.Difficulty;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import com.lyorine.survie.Main;
import com.lyorine.survie.utils.ItemBuilder;
import com.lyorine.survie.utils.Reference;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class InventoryEvent implements Listener {

    private Main main;

    public InventoryEvent(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            Inventory inv = e.getClickedInventory();
            InventoryView view = e.getView();
            ItemStack item = e.getCurrentItem();
            if (item != null && item.getItemMeta() != null) {
                String name = item.getItemMeta().getDisplayName();
                if (view.getTitle().equalsIgnoreCase(Reference.INVSURVIE.getName())) {
                    e.setCancelled(true);

                    if (name.equalsIgnoreCase(Reference.INVSURVIE_SPAWN.getName())) {
                        World world = Bukkit.getWorld(main.getConfig().getString("worldName"));
                        main.getTeleportationManager().teleportPlayerWithoutMoving(p, world.getSpawnLocation().clone().add(0.5, 0, 0.5), 5);
                        p.closeInventory();
                    } else if (name.equalsIgnoreCase(Reference.INVSURVIE_BOUTIQUE.getName())) {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10f, 1f);
                        openComptoir(p);
                    } else if (name.equalsIgnoreCase(Reference.INVSURVIE_INFO.getName())) {
                        sendServerInfo(p);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                        p.closeInventory();

                    } else if (name.equalsIgnoreCase(Reference.INVSURVIE_EXP.getName())) {
                        openExpInventory(p);
                    } else if (name.equalsIgnoreCase(Reference.INVSURVIE_RTP.getName())) {
                        openRTPMenu(p);
                    } else if (name.equalsIgnoreCase(Reference.INVSURVIE_END.getName())) {
                        p.closeInventory();
                        //main.getTeleportationManager().teleportPlayerWithoutMoving(p, end, 5);
                    }
                } else if (view.getTitle().equalsIgnoreCase(Reference.INVHOME.getName())) {
                    e.setCancelled(true);
                    if (!(name.equalsIgnoreCase(Reference.INVHOME_MANAGE.getName()))) {
                        String homeName = name.replaceAll(Reference.INVHOME_HOME.getName(), "");
                        Optional<Home> homeOpt = main.getHomeManager().getFromName(p, homeName);
                        if(homeOpt.isPresent()){
                            Home home = homeOpt.get();
                            home.teleport(p);
                            p.closeInventory(InventoryCloseEvent.Reason.TELEPORT);
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 10f, 1f);
                        } else {
                            p.sendMessage("§7» §cVotre home n'existe pas");
                        }
                    } else {
                        main.getHomeManager().openManageHome(p);
                    }
                } else if (view.getTitle().equalsIgnoreCase(Reference.INVMHOME.getName())) {
                    e.setCancelled(true);
                    if (name.equalsIgnoreCase(Reference.INVMHOME_CREATE.getName())) {
                        new AnvilGUI.Builder()
                                .onComplete((player, text) -> {
                                    if (main.getHomeManager().exists(p, text)) {
                                        player.sendMessage("§7» §cVous possèdez déjà un home avec ce nom !");
                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10f, 1f);
                                        return AnvilGUI.Response.text("Mon home");
                                    } else {
                                        main.getHomeManager().createHome(p, text);
                                        player.sendMessage("§7» §aVotre home vient d'être crée ! Vous pouvez le consulter en faisant /home");
                                        return AnvilGUI.Response.close();
                                    }
                                })
                                .preventClose()
                                .text("Mon home")
                                .plugin(main)
                                .open(p);
                    } else if (name.equalsIgnoreCase(Reference.INVMHOME_DELETE.getName())) {
                        main.getHomeManager().openDeleteHome(p);
                    } else if (name.equalsIgnoreCase(Reference.INVMHOME_RENAME.getName())) {
                        main.getHomeManager().openRenameHome(p);
                    } else if (name.equalsIgnoreCase(Reference.INVMHOME_CHANGE.getName())) {
                        main.getHomeManager().openChangeHome(p);
                    } else if (name.equalsIgnoreCase(Reference.INVMHOME_SLOT.getName())) {
                        main.getHomeManager().openChangeSlotMenu(p);
                    }
                } else if (view.getTitle().equalsIgnoreCase(Reference.INVDELETEHOME.getName())) {
                    e.setCancelled(true);
                    String homeName = name.replaceAll(Reference.INVHOME_HOME.getName(), "");
                    Optional<Home> homeOpt = main.getHomeManager().getFromName(p, homeName);
                    if(homeOpt.isPresent()) {
                        Home home = homeOpt.get();
                        main.getHomeManager().openConfirmDeleteMenu(home, p);
                    }
                } else if (view.getTitle().startsWith(Reference.INVCONFIRMDELETE.getName())) {
                    e.setCancelled(true);
                    if (name.equalsIgnoreCase(Reference.INV_CONFIRM.getName())) {
                        String homeName = ChatColor.stripColor(view.getTitle());
                        homeName = homeName.replace(Reference.INVCONFIRMDELETE_STRIP.getName(), "");
                        main.getHomeManager().deleteHome(p, homeName);
                        p.sendMessage("§7» §6Home §csupprimée §7!");
                        p.closeInventory();
                    } else if (name.equalsIgnoreCase(Reference.INV_CANCEL.getName())) {
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    }
                } else if (view.getTitle().equalsIgnoreCase(Reference.INVRENAMEHOME.getName())) {
                    e.setCancelled(true);
                    String homeName = name.replaceAll(Reference.INVHOME_HOME.getName(), "");
                    Optional<Home> homeOpt = main.getHomeManager().getFromName(p, homeName);
                    if(homeOpt.isPresent()) {
                        Home home = homeOpt.get();
                        new AnvilGUI.Builder()
                                .onComplete((player, text) -> {
                                    if (main.getHomeManager().exists(p, text)) {
                                        player.sendMessage("§7» §cVous possèdez déjà un home avec ce nom !");
                                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10f, 1f);
                                        return AnvilGUI.Response.text(home.getName());
                                    } else {
                                        main.getHomeManager().renameHome(p, home, text);
                                        player.sendMessage("§7» §aVotre home vient d'être renommé ! Vous pouvez le consulter en faisant /home");
                                        return AnvilGUI.Response.close();
                                    }
                                })
                                .preventClose()
                                .text(home.getName())
                                .plugin(main)
                                .open(p);
                    }
                } else if (view.getTitle().equalsIgnoreCase(Reference.INVCHANGEHOME.getName())) {
                    e.setCancelled(true);
                    String homeName = name.replaceAll(Reference.INVHOME_HOME.getName(), "");
                    Optional<Home> homeOpt = main.getHomeManager().getFromName(p, homeName);
                    if(homeOpt.isPresent()) {
                        Home home = homeOpt.get();
                        main.getHomeManager().openMaterialMenu(home, p);
                    }
                } else if (view.getTitle().startsWith(Reference.INVMATERIALCHANGE.getName())) {
                    e.setCancelled(true);
                    String homeName = ChatColor.stripColor(view.getTitle());
                    homeName = homeName.replace(Reference.INVMATERIALCHANGE_STRIP.getName(), "");
                    Optional<Home> homeOpt = main.getHomeManager().getFromName(p, homeName);
                    if(homeOpt.isPresent()) {
                        Home home = homeOpt.get();
                        Material material = item.getType();
                        main.getHomeManager().changeMaterial(p, home, material);
                        p.sendMessage("§7» §aLe matériel pour votre §6home §a\"§6" + home.getName() + "§a\" a été modifié !");
                        p.closeInventory();
                    }
                } else if (view.getTitle().startsWith(Reference.INVSLOTHOME.getName())) {
                    e.setCancelled(true);
                    int slot = e.getSlot();
                    if (e.getAction().equals(InventoryAction.PICKUP_ALL)) {
                        if (!(slot - 1 < 0)) {
                            ItemStack itemToSwap = inv.getItem(slot - 1);
                            if (itemToSwap != null && itemToSwap.getType() != Material.AIR && itemToSwap.getItemMeta() != null) {
                                inv.setItem(slot, itemToSwap);
                                inv.setItem(slot - 1, item);
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 100, 1);
                            }
                        } else {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
                        }
                    } else if (e.getAction().equals(InventoryAction.PICKUP_HALF)) {
                        if (!(slot + 1 >= main.getHomeManager().getHomes(p).size())) {
                            ItemStack itemToSwap = inv.getItem(slot + 1);
                            if (itemToSwap != null && itemToSwap.getType() != Material.AIR && itemToSwap.getItemMeta() != null) {
                                inv.setItem(slot, itemToSwap);
                                inv.setItem(slot + 1, item);
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 100, 1);
                            }
                        } else {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
                        }
                    }
                } else if (view.getTitle().equalsIgnoreCase(Reference.INVEXP.getName())) {
                    e.setCancelled(true);
                    ItemStack selectItem = inv.getItem(13);
                    String loreLine = selectItem.getItemMeta().getLore().get(2);
                    loreLine = loreLine.replace("§7une bouteille de §a", "").replace("xp §7!", "");
                    int currentXp = Integer.parseInt(loreLine);
                    if (name.equalsIgnoreCase(Reference.INVEXP_PLUS1.getName())) {
                        currentXp++;
                        inv.setItem(13, updateItem(selectItem, currentXp));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 10f);
                    } else if (name.equalsIgnoreCase(Reference.INVEXP_PLUS5.getName())) {
                        currentXp += 5;
                        inv.setItem(13, updateItem(selectItem, currentXp));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 10f);
                    } else if (name.equalsIgnoreCase(Reference.INVEXP_PLUS10.getName())) {
                        currentXp += 10;
                        inv.setItem(13, updateItem(selectItem, currentXp));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 10f);
                    } else if (name.equalsIgnoreCase(Reference.INVEXP_PLUS50.getName())) {
                        currentXp += 50;
                        inv.setItem(13, updateItem(selectItem, currentXp));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 10f);
                    } else if (name.equalsIgnoreCase(Reference.INVEXP_PLUS100.getName())) {
                        currentXp += 100;
                        inv.setItem(13, updateItem(selectItem, currentXp));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 10f);

                    } else if (name.equalsIgnoreCase(Reference.INVEXP_LESS1.getName())) {
                        currentXp--;
                        if (currentXp <= 0) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10f, 1f);
                        } else {
                            inv.setItem(13, updateItem(selectItem, currentXp));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 10f);
                        }
                    } else if (name.equalsIgnoreCase(Reference.INVEXP_LESS5.getName())) {
                        currentXp -= 5;
                        if (currentXp <= 0) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10f, 1f);
                        } else {
                            inv.setItem(13, updateItem(selectItem, currentXp));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 10f);
                        }
                    } else if (name.equalsIgnoreCase(Reference.INVEXP_LESS10.getName())) {
                        currentXp -= 10;
                        if (currentXp <= 0) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10f, 1f);
                        } else {
                            inv.setItem(13, updateItem(selectItem, currentXp));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 10f);
                        }
                    } else if (name.equalsIgnoreCase(Reference.INVEXP_LESS50.getName())) {
                        currentXp -= 50;
                        if (currentXp <= 0) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10f, 1f);
                        } else {
                            inv.setItem(13, updateItem(selectItem, currentXp));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 100f);
                        }
                    } else if (name.equalsIgnoreCase(Reference.INVEXP_LESS100.getName())) {
                        currentXp -= 100;
                        if (currentXp <= 0) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10f, 1f);
                        } else {
                            inv.setItem(13, updateItem(selectItem, currentXp));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 100f);
                        }

                    } else if (name.equalsIgnoreCase(Reference.INVEXP_MAX.getName())) {
                        currentXp = getPlayerExp(p);
                        inv.setItem(13, updateItem(selectItem, currentXp));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, currentXp / 100f);
                    } else if (name.equalsIgnoreCase(Reference.INV_CONFIRM.getName())) {
                        if (p.getInventory().firstEmpty() != -1) {
                            if (getPlayerExp(p) >= currentXp) {
                                p.giveExp(-currentXp);
                                p.getInventory().addItem(new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName("§eExpérience §7» §a" + currentXp + "xp").addEnchant(new Glow(new NamespacedKey(main, "glowing_shit")), 1).toItemStack());
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100f, 1f);
                                p.closeInventory();
                            } else {
                                p.sendMessage("§7» §cVous n'avez pas assez d'xp !\n§7Vous avez actuellement: §a" + getPlayerExp(p) + "xp");
                            }
                        } else {
                            p.sendMessage("§7» §cVotre inventaire est plein !");
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 100f, 1f);
                        }
                    }


                } else if (view.getTitle().equalsIgnoreCase("§7» §eFeu d'artifice")) {
                    e.setCancelled(true);
                    String fName = ChatColor.stripColor(name);
                    if (fName.startsWith("AL")) {
                        ArrayList<Location> locs = main.getFireworkManager().getLocations();
                        Location loc1 = locs.get(0);
                        Location loc2 = locs.get(locs.size() - 1);
                        main.getFireworkManager().spawnFirework(loc1, fName);
                        main.getFireworkManager().spawnFirework(loc2, fName);
                    } else if (fName.startsWith("M")) {
                        ArrayList<Location> locs = main.getFireworkManager().getLocations();
                        Location loc1 = locs.get(0);
                        Location loc2 = locs.get(locs.size() - 1);
                        main.getFireworkManager().spawnFirework(loc1, fName);
                        main.getFireworkManager().spawnFirework(loc2, fName);
                    } else if (fName.startsWith("B")) {
                        ArrayList<Location> locs = getTwoLocations();
                        locs.forEach(loc -> {
                            main.getFireworkManager().spawnFirework(loc, fName);
                        });
                    } else if (fName.startsWith("F")) {
                        ArrayList<Location> locs = getTwoLocations();
                        locs.forEach(loc -> {
                            main.getFireworkManager().spawnFirework(loc, fName);
                        });
                    } else if (fName.startsWith("C")) {
                        ArrayList<Location> locs = getTwoLocations();
                        locs.forEach(loc -> {
                            main.getFireworkManager().spawnFirework(loc, fName);
                        });
                    } else if (fName.startsWith("MF")) {
                        ArrayList<Location> locs = main.getFireworkManager().getLocations();
                        Location loc1 = locs.get(0);
                        Location loc2 = locs.get(locs.size() - 1);
                        main.getFireworkManager().spawnFirework(loc1, fName);
                        main.getFireworkManager().spawnFirework(loc2, fName);
                    }


                } else if (view.getTitle().equalsIgnoreCase(Reference.INVSURVIE_BOUTIQUE.getName())) {
                    e.setCancelled(true);
                    if (name.equalsIgnoreCase(Reference.INVBOUTIQUE_ACHAT.getName())) {
                        openComptoirAchat(p);
                    } else if (name.equalsIgnoreCase(Reference.INVBOUTIQUE_VENTE.getName())) {
                        openComptoirVente(p);
                    }


                } else if (view.getTitle().equalsIgnoreCase(Reference.INVRTP.getName())) {
                    e.setCancelled(true);
                    if (name.equalsIgnoreCase(Reference.INV_CONFIRM.getName())) {
                        randomTeleport(p);
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 20, 1);
                    } else if (name.equalsIgnoreCase(Reference.INV_CANCEL.getName())) {
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 20, 1);
                    }
                } else if (view.getTitle().equalsIgnoreCase(Reference.INVRAID.getName())) {
                    if (name.contains(Reference.INVRAID_DIFFICULTY.getName())) {
                        String difficulty = ChatColor.stripColor(name.replace(Reference.INVRAID_DIFFICULTY.getName(), ""));
                        Difficulty difficultyObj = Difficulty.valueOf(difficulty);

                        if (difficultyObj != null) {
                            if (difficultyObj == Difficulty.FACILE) {
                                difficultyObj = Difficulty.MOYEN;
                            } else if (difficultyObj == Difficulty.MOYEN) {
                                difficultyObj = Difficulty.NORMAL;
                            } else if (difficultyObj == Difficulty.NORMAL) {
                                difficultyObj = Difficulty.HARDCORE;
                            } else if (difficultyObj == Difficulty.HARDCORE) {
                                difficultyObj = Difficulty.EXTREME;
                            } else if (difficultyObj == Difficulty.EXTREME) {
                                difficultyObj = Difficulty.FACILE;
                            }

                            main.getRaidManager().getRaid().setDifficulty(difficultyObj);
                            main.getCommandRaid().openConfigMenu(p);
                        }
                    } else if (name.contains(Reference.INVRAID_VAGUECOUNT.getName())) {
                        int vagueCount = Integer.parseInt(name.replace(Reference.INVRAID_VAGUECOUNT.getName(), ""));
                        new AnvilGUI.Builder()
                                .onComplete((player, text) -> {
                                    int newVagueCount = Integer.parseInt(text);
                                    main.getRaidManager().getRaid().setVagueCount(newVagueCount);
                                    Bukkit.getScheduler().runTaskLater(main, () -> main.getCommandRaid().openConfigMenu(player), 5);
                                    return AnvilGUI.Response.close();
                                })
                                .preventClose()
                                .text(vagueCount + "")
                                .plugin(main)
                                .open(p);

                    } else if (name.contains(Reference.INVRAID_START.getName())) {
                        main.getRaidManager().getRaid().setStartLocation(p.getLocation());
                        main.getRaidManager().getRaid().setStartTime(System.currentTimeMillis());
                        main.getRaidManager().getRaid().loadRaid();
                        p.closeInventory();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent e){
        InventoryView view = e.getView();
        if(view.getTitle().equalsIgnoreCase(Reference.INVSLOTHOME.getName())) {
            if (e.getPlayer() instanceof Player) {
                Player p = (Player) e.getPlayer();
                Inventory inv = e.getInventory();
                int slot = 0;
                for (ItemStack content : inv.getContents()) {
                    if(content != null && content.getItemMeta() != null){
                        String homeName = content.getItemMeta().getDisplayName().replaceAll(Reference.INVHOME_HOME.getName(), "");
                        Optional<Home> homeOpt = main.getHomeManager().getFromName(p, homeName);
                        if(homeOpt.isPresent()) {
                            Home home = homeOpt.get();
                            home.setSlot(slot);
                            slot++;
                        }
                    }
                }
                p.sendMessage("§7» §aEmplacement de vos §6homes §acorrectement sauvegardés !");
            }
        }
    }

    private void randomTeleport(Player p) {
        p.closeInventory();
        World world = Bukkit.getWorld(main.getConfig().getString("worldName"));
        if(p.getWorld().equals(world)) {
            int x = ThreadLocalRandom.current().nextInt(-2000, 2000 + 1);
            int z = ThreadLocalRandom.current().nextInt(-2000, 2000 + 1);
            int y = p.getWorld().getHighestBlockYAt(x, z);

            Location randomLocation = new Location(p.getWorld(), x, y, z);

            main.getTeleportationManager().teleportPlayerWithoutMoving(p, randomLocation, 5);
        }else{
            p.sendMessage("§7» §c§lImpossible de se téléporter aléatoirement dans le nether ou dans l'end !");
        }
    }

    private void openRTPMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, Reference.INVRTP.getName());

        inv.setItem(0, new ItemBuilder(Material.GREEN_DYE).setName(Reference.INV_CONFIRM.getName()).setLore("§7Cliquez ici pour §avous téléporter §7aléatoiremement §7!").toItemStack());
        inv.setItem(2, new ItemBuilder(Material.MAP).setName("§7Téléportation §aaléatoire").toItemStack());
        inv.setItem(4, new ItemBuilder(Material.RED_DYE).setName(Reference.INV_CANCEL.getName()).setLore("§7Cliquez ici pour §cannuler §7!").toItemStack());

        p.openInventory(inv);
    }

    private void openComptoirAchat(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9*3, Reference.INVBOUTIQUE_ACHAT.getName());

        inv.setItem(2, new ItemBuilder(Material.GLISTERING_MELON_SLICE).setName(Reference.INVBOUTIQUE_ACHAT.getName()).setLore(Reference.INVBOUTIQUE_ACHATLORE.getName()).toItemStack());
        inv.setItem(6, new ItemBuilder(Material.GOLD_NUGGET).setName(Reference.INVBOUTIQUE_VENTE.getName()).setLore(Reference.INVBOUTIQUE_VENTELORE.getName()).toItemStack());

        p.openInventory(inv);
    }

    private void openComptoirVente(Player p) {

    }

    private void openComptoir(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9*1, Reference.INVSURVIE_BOUTIQUE.getName());

        inv.setItem(2, new ItemBuilder(Material.GLISTERING_MELON_SLICE).setName(Reference.INVBOUTIQUE_ACHAT.getName()).setLore(Reference.INVBOUTIQUE_ACHATLORE.getName()).toItemStack());
        inv.setItem(6, new ItemBuilder(Material.GOLD_NUGGET).setName(Reference.INVBOUTIQUE_VENTE.getName()).setLore(Reference.INVBOUTIQUE_VENTELORE.getName()).toItemStack());

        p.openInventory(inv);
    }

    private ArrayList<Location> getTwoLocations() {
        int size = main.getFireworkManager().getLocations().size();
        int random1 = new Random().nextInt(size);
        int random2 = new Random().nextInt(size);
        while(random1 == random2){
            random2 = new Random().nextInt(size);
        }

        return new ArrayList<>(Arrays.asList(main.getFireworkManager().getLocations().get(random1), main.getFireworkManager().getLocations().get(random2)));
    }

    private ItemStack updateItem(ItemStack selectItem, int currentXp) {
        List<String> lore = selectItem.getItemMeta().getLore();
        StringBuilder str = new StringBuilder();
        str.append("§7une bouteille de §a").append(currentXp).append("xp §7!");
        lore.set(2, str.toString());
        selectItem.getItemMeta().setLore(lore);
        return selectItem;
    }

    private void openExpInventory(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9*3, Reference.INVEXP.getName());

        inv.setItem(9, new ItemBuilder(Material.GREEN_WOOL).setName(Reference.INVEXP_PLUS1.getName()).setLore(Reference.INVEXP_PLUS1LORE.getName()).toItemStack());
        inv.setItem(10, new ItemBuilder(Material.GREEN_WOOL).setName(Reference.INVEXP_PLUS5.getName()).setLore(Reference.INVEXP_PLUS5LORE.getName()).toItemStack());
        inv.setItem(11, new ItemBuilder(Material.GREEN_WOOL).setName(Reference.INVEXP_PLUS10.getName()).setLore(Reference.INVEXP_PLUS10LORE.getName()).toItemStack());
        inv.setItem(18, new ItemBuilder(Material.GREEN_WOOL).setName(Reference.INVEXP_PLUS50.getName()).setLore(Reference.INVEXP_PLUS50LORE.getName()).toItemStack());
        inv.setItem(19, new ItemBuilder(Material.GREEN_WOOL).setName(Reference.INVEXP_PLUS100.getName()).setLore(Reference.INVEXP_PLUS100LORE.getName()).toItemStack());

        inv.setItem(4, new ItemBuilder(Material.WHITE_WOOL).setName(Reference.INVEXP_MAX.getName()).setLore(Reference.INVEXP_MAXLORE.getName()).toItemStack());
        inv.setItem(13, new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName("§eSélection").setLore("", "§7Vous avez actuellement séléctionné", "§7une bouteille de §a100xp §7!", "", "§7Cliquez sur §aconfirmer §7pour récupérer votre bouteille !").toItemStack());
        inv.setItem(22, new ItemBuilder(Material.GREEN_DYE).setName(Reference.INV_CONFIRM.getName()).setLore("§7Cliquez ici pour §arécupérer votre bouteille §7!").toItemStack());

        inv.setItem(15, new ItemBuilder(Material.RED_WOOL).setName(Reference.INVEXP_LESS1.getName()).setLore(Reference.INVEXP_LESS1LORE.getName()).toItemStack());
        inv.setItem(16, new ItemBuilder(Material.RED_WOOL).setName(Reference.INVEXP_LESS5.getName()).setLore(Reference.INVEXP_LESS5LORE.getName()).toItemStack());
        inv.setItem(17, new ItemBuilder(Material.RED_WOOL).setName(Reference.INVEXP_LESS10.getName()).setLore(Reference.INVEXP_LESS10LORE.getName()).toItemStack());
        inv.setItem(25, new ItemBuilder(Material.RED_WOOL).setName(Reference.INVEXP_LESS50.getName()).setLore(Reference.INVEXP_LESS50LORE.getName()).toItemStack());
        inv.setItem(26, new ItemBuilder(Material.RED_WOOL).setName(Reference.INVEXP_LESS100.getName()).setLore(Reference.INVEXP_LESS100LORE.getName()).toItemStack());

        p.openInventory(inv);
    }

    private void sendServerInfo(Player p) {
        p.sendMessage("§7§m------------------------------------");
        p.sendMessage("");
        p.sendMessage("§aSurvie §7• §e© all rights reserved");
        p.sendMessage("");
        p.sendMessage("§7Ceci est le serveur §asurvie §7de la §6reine Lyorine §7!");
        p.sendMessage("§7Voici quelques commandes qui pourront t'être utiles§f:");
        p.sendMessage("");
        p.sendMessage("§7» §c/menu §7ainsi que §c/home");
        p.sendMessage("");
        p.sendMessage("§c§lATTENTION§r§7, si tu es nouveau, merci de lire entièrement les §erègles §adisponibles au spawn §7!");
        p.sendMessage("");
        p.sendMessage("§7» §aSi tu rencontres quelconque problème, merci de contacter §aMAXOUXAX §asur le §9Discord §adisponible à cette adresse§f: §7https://go.lyorine.com/discord");
        p.sendMessage("");
        p.sendMessage("§7» §c§lIl est impératif de rejoindre le §9Discord §c§l!");
        p.sendMessage("");
        p.sendMessage("§bTwitter§f: §7https://go.lyorine.com/twitter");
        p.sendMessage("§9Discord§f: §7https://go.lyorine.com/discord");
        p.sendMessage("§5Twitch§f: §7https://go.lyorine.com/twitch");
        p.sendMessage("§eInstagram§f: §7https://go.lyorine.com/instagram");
        p.sendMessage("§cYou§fTube§f: §7https://go.lyorine.com/youtube");
        p.sendMessage("");
        p.sendMessage("§7§m------------------------------------");
    }

    // Calculate amount of EXP needed to level up
    public static int getExpToLevelUp(int level){
        if(level <= 15){
            return 2*level+7;
        } else if(level <= 30){
            return 5*level-38;
        } else {
            return 9*level-158;
        }
    }

    // Calculate total experience up to a level
    public static int getExpAtLevel(int level){
        if(level <= 16){
            return (int) (Math.pow(level,2) + 6*level);
        } else if(level <= 31){
            return (int) (2.5*Math.pow(level,2) - 40.5*level + 360.0);
        } else {
            return (int) (4.5*Math.pow(level,2) - 162.5*level + 2220.0);
        }
    }

    // Calculate player's current EXP amount
    public static int getPlayerExp(Player player){
        int exp = 0;
        int level = player.getLevel();

        // Get the amount of XP in past levels
        exp += getExpAtLevel(level);

        // Get amount of XP towards next level
        exp += Math.round(getExpToLevelUp(level) * player.getExp());

        return exp;
    }

}
