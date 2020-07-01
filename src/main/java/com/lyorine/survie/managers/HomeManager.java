package com.lyorine.survie.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import com.lyorine.survie.Main;
import com.lyorine.survie.utils.Home;
import com.lyorine.survie.utils.ItemBuilder;
import com.lyorine.survie.utils.Reference;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class HomeManager {

    private Main main;
    private HashMap<UUID, ArrayList<Home>> playersHomes = new HashMap<>();

    public HomeManager(Main main) {
        this.main = main;
    }

    public void loadHomes() throws IOException, InvalidConfigurationException {
        File filea = new File(main.getDataFolder(), "homes");
        if (!filea.exists()) {
            filea.getParentFile().mkdirs();
        }
        File[] files = filea.listFiles();
        if(files != null){
            for(File file : files){
                if(file.getName().endsWith(".yml")){
                    YamlConfiguration homesConfig = new YamlConfiguration();
                    homesConfig.load(file);
                    UUID pUuid = UUID.fromString(file.getName().replaceAll(".yml", ""));
                    Set<String> homeNames = homesConfig.getKeys(false);
                    ArrayList<Home> toAdd = new ArrayList<>();
                    final int[] iteration = {0};
                    homeNames.forEach(s -> {
                        String name = homesConfig.getString(s+".name");
                        String material = homesConfig.getString(s+".material");
                        String world = homesConfig.getString(s+".world");
                        int slot = iteration[0];
                        if(homesConfig.isInt(s+".slot")) {
                            slot = homesConfig.getInt(s + ".slot");
                        }
                        double x = homesConfig.getDouble(s+".x");
                        double y = homesConfig.getDouble(s+".y");
                        double z = homesConfig.getDouble(s+".z");
                        Material materialReal = Material.getMaterial(material);
                        Home home;
                        if(materialReal != null){
                            home = new Home(new Location(Bukkit.getWorld(world), x, y, z), materialReal, name, main, slot);
                        }else{
                            home = new Home(new Location(Bukkit.getWorld(world), x, y, z), Material.BLUE_BED, name, main, slot);
                        }
                        toAdd.add(home);
                        iteration[0]++;
                    });
                    playersHomes.put(pUuid, toAdd);
                }
            }
        }
    }

    public void saveHomes(Player p) {
        try {
            File file = new File(main.getDataFolder(), "homes");
            if (!file.exists()) {
                file.mkdirs();
            }
            ArrayList<Home> homes = playersHomes.getOrDefault(p.getUniqueId(), new ArrayList<>());
            File pFile = new File(main.getDataFolder() + File.separator + "homes", p.getUniqueId() + ".yml");
            if (!pFile.exists()) {
                pFile.createNewFile();
            }
            YamlConfiguration config = new YamlConfiguration();
            homes.forEach(home -> {
                config.set(home.getName().toLowerCase()+".name", home.getName());
                config.set(home.getName().toLowerCase()+".material", home.getMaterial().name());
                config.set(home.getName().toLowerCase()+".world", home.getLocation().getWorld().getName());
                config.set(home.getName().toLowerCase()+".slot", home.getSlot());
                config.set(home.getName().toLowerCase()+".x", home.getLocation().getX());
                config.set(home.getName().toLowerCase()+".y", home.getLocation().getY());
                config.set(home.getName().toLowerCase()+".z", home.getLocation().getZ());
            });
            config.save(pFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createHome(Player p, String name){
        ArrayList<Home> homes = playersHomes.getOrDefault(p.getUniqueId(), new ArrayList<>());
        Home newHome = new Home(p.getLocation(), Material.BLUE_BED, name, main, homes.size());
        homes.add(newHome);
        playersHomes.put(p.getUniqueId(), homes);

        saveHomes(p);
    }

    public void deleteHome(Player p, String name){
        ArrayList<Home> homes = playersHomes.getOrDefault(p.getUniqueId(), new ArrayList<>());
        Optional<Home> homeOpt = getFromName(p, name);
        if(homeOpt.isPresent()) {
            Home home = homeOpt.get();
            homes.remove(home);
            homes.stream().filter(h -> h.getSlot() > home.getSlot()).forEach(h -> {
                h.setSlot(h.getSlot() - 1);
            });
            playersHomes.put(p.getUniqueId(), homes);

            saveHomes(p);
        }
    }

    public ArrayList<Home> getHomes(Player p){
        return playersHomes.getOrDefault(p.getUniqueId(), new ArrayList<>());
    }

    public Optional<Home> getFromName(Player p, String name){
        return playersHomes.get(p.getUniqueId()).stream().filter(home -> home.getName().equalsIgnoreCase(name)).findFirst();
    }

    public boolean exists(Player p, String name){
        if(playersHomes.containsKey(p.getUniqueId())) {
            return playersHomes.get(p.getUniqueId()).stream().anyMatch(home -> home.getName().equalsIgnoreCase(name));
        }else{
            return false;
        }
    }

    public void openManageHome(Player p) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, Reference.INVMHOME.getName());

        inv.addItem(new ItemBuilder(Material.CAMPFIRE).setName(Reference.INVMHOME_CREATE.getName()).setLore(Reference.INVMHOME_CREATELORE.getName()).toItemStack());
        inv.addItem(new ItemBuilder(Material.NAME_TAG).setName(Reference.INVMHOME_RENAME.getName()).setLore(Reference.INVMHOME_RENAMELORE.getName()).toItemStack());
        inv.addItem(new ItemBuilder(Material.BARRIER).setName(Reference.INVMHOME_DELETE.getName()).setLore(Reference.INVMHOME_DELETELORE.getNames()).toItemStack());
        inv.addItem(new ItemBuilder(Material.BLACK_BED).setName(Reference.INVMHOME_CHANGE.getName()).setLore(Reference.INVMHOME_CHANGELORE.getName()).toItemStack());
        inv.addItem(new ItemBuilder(Material.CLOCK).setName(Reference.INVMHOME_SLOT.getName()).setLore(Reference.INVMHOME_SLOTLORE.getNames()).toItemStack());

        p.openInventory(inv);
    }

    public void openDeleteHome(Player p) {
        ArrayList<Home> playerHomes = main.getHomeManager().getHomes(p);
        if(playerHomes.isEmpty()){
            p.sendMessage("§7» Vous n'avez §caucun home §7!");
        }else {
            Inventory inv = Bukkit.createInventory(null, main.getInventoryUtils().getSizeOfInventoryFromSlotsNeeded(playerHomes.size()), Reference.INVDELETEHOME.getName());
            playerHomes.forEach(home -> {
                inv.setItem(home.getSlot(), new ItemBuilder(home.getMaterial()).setName(Reference.INVHOME_HOME.getName() + home.getName()).setLore("§7Cliquez ici pour supprimer votre §6home§7 \"§6" + home.getName() + "§7\" !").toItemStack());
            });
            p.openInventory(inv);
        }
    }

    public void openChangeSlotMenu(Player p) {
        ArrayList<Home> playerHomes = main.getHomeManager().getHomes(p);
        if(playerHomes.isEmpty()){
            p.sendMessage("§7» Vous n'avez §caucun home §7!");
        }else {
            Inventory inv = Bukkit.createInventory(null, main.getInventoryUtils().getSizeOfInventoryFromSlotsNeeded(playerHomes.size()), Reference.INVSLOTHOME.getName());
            playerHomes.forEach(home -> {
                inv.setItem(home.getSlot(), new ItemBuilder(home.getMaterial()).setName(Reference.INVHOME_HOME.getName() + home.getName()).setLore(" ", "  §7• §eClique-gauche §7» §aDéplacer vers la gauche", "  §7• §eClique-droit §7» §aDéplacer vers la droite", " ").toItemStack());
            });
            p.openInventory(inv);
        }
    }

    public void openConfirmDeleteMenu(Home home, Player p) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, Reference.INVCONFIRMDELETE.getName()+home.getName());

        inv.setItem(0, new ItemBuilder(Material.GREEN_DYE).setName(Reference.INV_CONFIRM.getName()).setLore("§7Cliquez ici pour §csupprimer §7votre §6home §7!").toItemStack());
        inv.setItem(2, new ItemBuilder(home.getMaterial()).setName("§7Supression du §6home").setLore("", "§7Ce home va être supprimé", "  §7• Nom§f: §e"+home.getName(), "  §7• Location§7: x: §e"+new DecimalFormat("######.##").format(home.getLocation().getX()), "                §7y: §e"+new DecimalFormat("######.##").format(home.getLocation().getY()), "                §7z: §e"+new DecimalFormat("######.##").format(home.getLocation().getZ()), "", "§c§lCette action est irréversible !").toItemStack());
        inv.setItem(4, new ItemBuilder(Material.RED_DYE).setName(Reference.INV_CANCEL.getName()).setLore("§7Cliquez ici pour §cannuler §7!").toItemStack());

        p.openInventory(inv);
    }

    public void openRenameHome(Player p) {
        ArrayList<Home> playerHomes = main.getHomeManager().getHomes(p);
        if(playerHomes.isEmpty()){
            p.sendMessage("§7» Vous n'avez §caucun home §7!");
        }else {
            Inventory inv = Bukkit.createInventory(null, main.getInventoryUtils().getSizeOfInventoryFromSlotsNeeded(playerHomes.size() + 1), Reference.INVRENAMEHOME.getName());
            playerHomes.forEach(home -> {
                inv.setItem(home.getSlot(), new ItemBuilder(home.getMaterial()).setName(Reference.INVHOME_HOME.getName() + home.getName()).setLore("§7Cliquez ici pour renommer votre §6home§7 \"§6" + home.getName() + "§7\" !").toItemStack());
            });
            p.openInventory(inv);
        }
    }

    public void renameHome(Player p, Home home, String text) {
        home.rename(text);
        saveHomes(p);
    }

    public void changeMaterial(Player p, Home home, Material material) {
        home.setMaterial(material);
        saveHomes(p);
    }

    public void openChangeHome(Player p) {
        ArrayList<Home> playerHomes = main.getHomeManager().getHomes(p);
        if(playerHomes.isEmpty()){
            p.sendMessage("§7» Vous n'avez §caucun home §7!");
        }else {
            Inventory inv = Bukkit.createInventory(null, main.getInventoryUtils().getSizeOfInventoryFromSlotsNeeded(playerHomes.size() + 1), Reference.INVCHANGEHOME.getName());
            playerHomes.forEach(home -> {
                inv.setItem(home.getSlot(), new ItemBuilder(home.getMaterial()).setName(Reference.INVHOME_HOME.getName() + home.getName()).setLore("§7Cliquez ici pour modifier le matériel de votre §6home§7 \"§6" + home.getName() + "§7\" !").toItemStack());
            });
            p.openInventory(inv);
        }
    }

    public void openMaterialMenu(Home home, Player p) {

        List<Material> bedMaterials = new ArrayList<>(Arrays.asList(Material.BLACK_BED,
                Material.BLUE_BED,
                Material.YELLOW_BED,
                Material.BROWN_BED,
                Material.CYAN_BED,
                Material.GRAY_BED,
                Material.GREEN_BED,
                Material.LIGHT_BLUE_BED,
                Material.LIGHT_GRAY_BED,
                Material.LIME_BED,
                Material.MAGENTA_BED,
                Material.ORANGE_BED,
                Material.PINK_BED,
                Material.PURPLE_BED,
                Material.RED_BED,
                Material.WHITE_BED));
        Inventory inv = Bukkit.createInventory(null, main.getInventoryUtils().getSizeOfInventoryFromSlotsNeeded(bedMaterials.size()+1), Reference.INVMATERIALCHANGE.getName()+home.getName());

        bedMaterials.forEach(material -> {
            inv.addItem(new ItemBuilder(material).setName("§7» §aChoisir ce lit").setLore("§7Cliquez ici pour choisir ce lit pour représenter votre home").toItemStack());
        });
        p.openInventory(inv);
    }
}
