package com.lyorine.survie.utils.core;

import com.lyorine.survie.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class TaskRenderImage extends BukkitRunnable {

    private Player p;
    private String path;
    private Main main;

    public TaskRenderImage(Main main, Player p, String path) {
        this.main = main;
        this.p = p;
        this.path = path;
        p.sendMessage("Rendering started...");
    }

    @Override
    public void run() {
        try {
            ArrayList<MapView> mapsToGive = new ArrayList<>();
            ArrayList<Integer> mapIds = new ArrayList<>();
            BufferedImage image = main.getImageHelper().getImage(path);
            int row = image.getHeight()/128;
            int col = image.getWidth()/128;

            MapView map;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    p.sendMessage("Rendering "+i+"/"+row);
                    map = Bukkit.createMap(p.getWorld());
                    map = main.getRenderHelper().resetRenderers(map);
                    map.setScale(MapView.Scale.FARTHEST);
                    map.setUnlimitedTracking(false);
                    map.addRenderer(new ImageMapRenderer(image.getSubimage(j * 128, i * 128, 128, 128)));
                    mapsToGive.add(map);
                    mapIds.add(map.getId());
                }
            }

            for (MapView mapView : mapsToGive) {
                ItemStack it = new ItemStack(Material.FILLED_MAP);
                MapMeta mapMeta = (MapMeta) it.getItemMeta();
                mapMeta.setMapView(mapView);
                it.setItemMeta(mapMeta);
                p.getInventory().addItem(it);
            }

            ImageMap imageMap = new ImageMap(UUID.randomUUID(), path, mapIds);
            ImageMapYML imageMapYML = new ImageMapYML(main, imageMap.getUuid());

            imageMapYML.write(imageMap);
            main.getImageMapManager().addImageMap(imageMap);
            p.sendMessage("§7» §aRendu terminé");
        } catch (IOException e) {
            main.getLogger().warning("Cannot load image "+path+".");
            main.getLogger().warning(e.getMessage());
            e.printStackTrace();
        }
    }

}
