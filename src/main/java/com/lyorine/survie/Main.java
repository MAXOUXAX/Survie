package com.lyorine.survie;

import com.lyorine.survie.commands.*;
import com.lyorine.survie.listeners.*;
import com.lyorine.survie.managers.*;
import com.lyorine.survie.schedulers.*;
import com.lyorine.survie.utils.*;
import com.lyorine.survie.utils.core.DataLoader;
import com.lyorine.survie.utils.core.ImageMapManager;
import com.lyorine.survie.utils.core.helpers.RenderHelper;
import com.lyorine.survie.utils.raids.RaidManager;
import net.minecraft.server.v1_16_R1.PacketPlayOutMapChunk;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.craftbukkit.v1_16_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.lyorine.survie.utils.core.helpers.ImageHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

public class Main extends JavaPlugin {

    private Main main;
    private AccessManager accessManager;
    private HomeManager homeManager;
    private AFKManager afkManager;
    private RaidManager raidManager;
    private FireworkManager fireworkManager;
    private TeleportationManager teleportationManager;
    private LagManager lagManager;
    private VanishManager vanishManager;
    private InventoryUtils inventoryUtils;
    private CommandRaid commandRaid;

    private ImageHelper imageHelper;
    private RenderHelper renderHelper;
    private ImageMapManager imageMapManager;
    private DataLoader dataLoader;
    private AFKMonitor afkMonitor;
    private OPArmorChanger opArmorChanger;

    private Logger logger;

    public File IMAGE_DIR = new File(getDataFolder(),"images");
    public File IMAGE_MAP_DIR = new File(getDataFolder(), "maps");

    private Long estimatedTime = 0L;
    private String version;
    private Runtime runtime;

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Saving default config...");
        saveDefaultConfig();
        this.main = this;
        System.out.println("Getting runtime");
        this.runtime = Runtime.getRuntime();
        System.out.println("Building AccessManager...");
        this.accessManager = new AccessManager(this);
        System.out.println("Building HomeManager...");
        this.homeManager = new HomeManager(this);
        System.out.println("Building AFKManager...");
        this.afkManager = new AFKManager(this);
        System.out.println("Building FireworkManager...");
        this.fireworkManager = new FireworkManager(this);
        System.out.println("Loading FireworkManager...");
        fireworkManager.load();
        System.out.println("Building RaidManager...");
        this.raidManager = new RaidManager(this);
        System.out.println("Building InventoryUtils...");
        this.inventoryUtils = new InventoryUtils();
        System.out.println("Building ImageHelper...");
        this.imageHelper = new ImageHelper();
        System.out.println("Building RenderHelper...");
        this.renderHelper = new RenderHelper(this);
        System.out.println("Building ImageMapManager...");
        this.imageMapManager = new ImageMapManager();
        System.out.println("Building TeleportationManager...");
        this.teleportationManager = new TeleportationManager(this);
        System.out.println("Building LagManager...");
        this.lagManager = new LagManager(this);
        System.out.println("Building VanishManager...");
        this.vanishManager = new VanishManager(this);
        System.out.println("Building DataLoader...");
        this.dataLoader = new DataLoader(this);
        System.out.println("Loading maps and homes...");
        try {
            this.logger = new Logger(this);
            dataLoader.loadMaps();
            homeManager.loadHomes();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        System.out.println("Registering commands...");
        registerCommands();
        System.out.println("Registering listeners...");
        registerListeners();
        System.out.println("Registering recipes...");
        registerRecipes();
        System.out.println("Registering schedulers...");
        registerSchedulers();
        System.out.println("Registering enchantments...");
        registerEnchantments();

        System.out.println("Calculating elapsed time.");
        Long disabledAt = getConfig().getLong("disabledAt");
        Long rightNow = System.currentTimeMillis();
        Long timeElapsed = rightNow - disabledAt;
        if (getConfig().isSet("disabledAt")) {
            estimatedTime = timeElapsed;
        }
        version = Bukkit.getPluginManager().getPlugin("Survie").getDescription().getVersion();
        System.out.println("Sending broadcasts");
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Bukkit.broadcastMessage("§4§lSystème §7» §eServeur chargé en §a" + timeElapsed + "ms");
            Bukkit.getOnlinePlayers().forEach(p -> {
                afkMonitor.getLocationHashMap().put(p.getUniqueId(), new EasyLocation(p.getLocation()));
                p.sendTitle("§4§lSystème §7» §aRechargement", "§aRechargement effectué en§f: " + timeElapsed + "§ams", 20, 60, 20);
            });
        }, 40);
    }

    private void registerSchedulers() {
        this.afkMonitor = new AFKMonitor(this);
        this.opArmorChanger = new OPArmorChanger();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new TPSMonitor(this), 20*5, 20*5);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, afkMonitor, 20*1, 20*1);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AFKParticules(this), 20*2, 20*2);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new TablistRunnable(this, runtime), 10, 10);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, opArmorChanger, 4, 4);
    }


    private void registerRecipes() {
        Bukkit.resetRecipes();
        craftExpBottle();
        craftGrapin();
        craftPickaxes();
    }

    private void craftPickaxes() {
        Glow glow = new Glow(new NamespacedKey(this, "glowing_shit"));
        ItemStack coalPickaxe = new ItemBuilder(Material.STONE_PICKAXE).addEnchant(glow, 1).addEnchant(Enchantment.DIG_SPEED, 3).setLore(" ", "§7Pioche en §0§lcharbon").toItemStack();
        NamespacedKey coalPickaxeKey = new NamespacedKey(this, "coalPickaxe");
        ShapedRecipe coalPickaxeRecipe = new ShapedRecipe(coalPickaxeKey, coalPickaxe);
        coalPickaxeRecipe.shape("AAA", "BCB", "AAA");
        coalPickaxeRecipe.setIngredient('A', Material.COAL_BLOCK);
        coalPickaxeRecipe.setIngredient('B', Material.BLAST_FURNACE);
        coalPickaxeRecipe.setIngredient('C', Material.STONE_PICKAXE);

        Bukkit.addRecipe(coalPickaxeRecipe);

        ItemStack lapisPickaxe = new ItemBuilder(Material.IRON_PICKAXE).addEnchant(glow, 1).addEnchant(Enchantment.DIG_SPEED, 3).setLore(" ", "§7Pioche en §9§llapis").toItemStack();
        NamespacedKey lapisPickaxeKey = new NamespacedKey(this, "lapisPickaxe");
        ShapedRecipe lapisPickaxeRecipe = new ShapedRecipe(lapisPickaxeKey, lapisPickaxe);
        lapisPickaxeRecipe.shape("AAA", "BCB", "AAA");
        lapisPickaxeRecipe.setIngredient('A', Material.IRON_BLOCK);
        lapisPickaxeRecipe.setIngredient('B', Material.LAPIS_BLOCK);
        lapisPickaxeRecipe.setIngredient('C', Material.IRON_PICKAXE);

        Bukkit.addRecipe(lapisPickaxeRecipe);

        ItemStack emeraldPickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(glow, 1).addEnchant(Enchantment.DIG_SPEED, 5).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3).setLore(" ", "§7Pioche en §a§lémeraude").toItemStack();
        NamespacedKey emeraldPickaxeKey = new NamespacedKey(this, "emeraldPickaxe");
        ShapedRecipe emeraldPickaxeRecipe = new ShapedRecipe(emeraldPickaxeKey, emeraldPickaxe);
        emeraldPickaxeRecipe.shape("AAA", "BCB", "AAA");
        emeraldPickaxeRecipe.setIngredient('A', Material.DIAMOND_BLOCK);
        emeraldPickaxeRecipe.setIngredient('B', Material.EMERALD_BLOCK);
        emeraldPickaxeRecipe.setIngredient('C', Material.DIAMOND_PICKAXE);

        Bukkit.addRecipe(emeraldPickaxeRecipe);
    }

    private void craftGrapin() {
        Glow glow = new Glow(new NamespacedKey(this, "glowing_shit"));
        ItemStack grapin = new ItemBuilder(Material.FISHING_ROD).setName("§7» §6§lGrapin").setLore(" ", "§6§lGrapin").addEnchant(glow, 1).setUnbreakable(true).toItemStack();
        NamespacedKey grapinKey = new NamespacedKey(this, "grapin");
        ShapedRecipe grapinRecipe = new ShapedRecipe(grapinKey, grapin);
        grapinRecipe.shape("A A", "CDC", "A A");
        grapinRecipe.setIngredient('A', Material.IRON_INGOT);
        grapinRecipe.setIngredient('C', Material.EMERALD_BLOCK);
        grapinRecipe.setIngredient('D', Material.FISHING_ROD);

        Bukkit.addRecipe(grapinRecipe);
    }

    private void craftExpBottle() {
        ItemStack c = new ItemStack(Material.EXPERIENCE_BOTTLE);
        NamespacedKey cKey = new NamespacedKey(this, "exp_bottle");
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(cKey, c);
        shapelessRecipe.addIngredient(2, Material.ROTTEN_FLESH);
        shapelessRecipe.addIngredient(2, Material.BONE);
        shapelessRecipe.addIngredient(2, Material.STRING);

        Bukkit.addRecipe(shapelessRecipe);
    }

    private void registerCommands() {
        getCommand("survie").setExecutor(new CommandSurvie(this));
        getCommand("access").setExecutor(new CommandAccess(this));
        getCommand("menu").setExecutor(new CommandMenu());
        getCommand("home").setExecutor(new CommandHome(this));
        getCommand("map").setExecutor(new CommandMap(this));
        getCommand("firework").setExecutor(new CommandFirework(this));
        getCommand("vanish").setExecutor(new CommandVanish(vanishManager));
        getCommand("oparmor").setExecutor(new CommandOPArmor(this));
        this.commandRaid = new CommandRaid(this);
        getCommand("raid").setExecutor(commandRaid);
        //this.commandDragon = new CommandDragon(this);
        //getCommand("dragon").setExecutor(commandDragon);
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ConnectEvent(this), this);
        pluginManager.registerEvents(new ChatEvent(), this);
        pluginManager.registerEvents(new InventoryEvent(this), this);
        pluginManager.registerEvents(new LogEvent(this), this);
        pluginManager.registerEvents(new BedEvent(this), this);
        //pluginManager.registerEvents(new DragonEvent(this), this);
        pluginManager.registerEvents(new InteractEvent(), this);
        pluginManager.registerEvents(new BreakEvent(), this);
        pluginManager.registerEvents(new GrapinEvent(), this);
        pluginManager.registerEvents(vanishManager, this);
        pluginManager.registerEvents(this.raidManager, this);
    }

    public void registerEnchantments() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(new NamespacedKey(this, "glowing_shit"));
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException ignored){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public Collection<Chunk> getChunkWithinRadius(Chunk origin, int radius) {
        World world = origin.getWorld();

        int length = (radius * 2) + 1;
        HashSet<Chunk> chunks = new HashSet<>(length * length);

        int cX = origin.getX();
        int cZ = origin.getZ();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                chunks.add(world.getChunkAt(cX + x, cZ + z));
            }
        }
        return chunks;
    }

    public void loadChunk(Player p, Chunk chunk) {
        chunk.load();
        PacketPlayOutMapChunk lowPacket = new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), 255, false); // 00000000 11111111
        PacketPlayOutMapChunk highPacket = new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), 65280, false); // 11111111 00000000

        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(lowPacket);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(highPacket);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        System.out.println("Resetting afks...");
        afkManager.resetAfks();
        System.out.println("Calculating estimated time...");
        int ticks = (estimatedTime > 100000) ? 20*15 : Math.toIntExact(estimatedTime/1000*20);
        estimatedTime = (estimatedTime > 100000) ? 1000*20 : estimatedTime;
        System.out.println("Broadcasting...");
        Bukkit.getOnlinePlayers().forEach(o -> {
            o.sendTitle("§4§lSystème §7» §cRechargement...", "§eTemps estimé§f: "+(estimatedTime == 0L ? "§cInconnu" : estimatedTime+"§ems"), 20, ticks*2, 20);
        });
        getConfig().set("disabledAt", System.currentTimeMillis());
        Bukkit.broadcastMessage("§4§lSystème §7» §cRechargement du serveur");
        System.out.println("Clearing tablists...");
        clearTabLists();
        System.out.println("Saving config...");
        saveConfig();
        System.out.println("Saving logs...");
        logger.save();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public AccessManager getAccessManager() {
        return accessManager;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public AFKManager getAfkManager() {
        return afkManager;
    }

    public AFKMonitor getAfkMonitor() {
        return afkMonitor;
    }

    public InventoryUtils getInventoryUtils() {
        return inventoryUtils;
    }

    public ImageHelper getImageHelper() {
        return imageHelper;
    }

    public RenderHelper getRenderHelper() {
        return renderHelper;
    }

    public ImageMapManager getImageMapManager() {
        return imageMapManager;
    }

    public File getIMAGE_DIR() {
        return IMAGE_DIR;
    }

    public File getIMAGE_MAP_DIR() {
        return IMAGE_MAP_DIR;
    }

    public Logger getSurvieLogger() {
        return logger;
    }

    public FireworkManager getFireworkManager() {
        return fireworkManager;
    }

    public boolean equivalentLocation(Location before, Location now){
        if(before.getX() == now.getX() && before.getY() == now.getY() && before.getZ() == now.getZ()){
            return true;
        }
        return false;
    }

    public String getVersion() {
        return version;
    }

    public void clearTabLists() {
        Bukkit.getOnlinePlayers().forEach(o -> o.setPlayerListHeaderFooter("\n" +
                "§a§lSurvie §ev"+main.getVersion()+"" +
                "\n" +
                "\n§c§lRechargement en cours..." +
                "\n ", "" +
                "\n§ego.lyorine.com/roadmap" +
                "\n§7Serveur développé par §cMaxouxax"));
    }

    public RaidManager getRaidManager() {
        return raidManager;
    }

    public TeleportationManager getTeleportationManager() {
        return teleportationManager;
    }

    public CommandRaid getCommandRaid() {
        return commandRaid;
    }

    public LagManager getLagManager() {
        return lagManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public OPArmorChanger getOpArmorChanger() {
        return opArmorChanger;
    }
}
