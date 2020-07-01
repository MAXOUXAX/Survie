package com.lyorine.survie.utils.core;

import org.bukkit.Bukkit;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;
import com.lyorine.survie.Main;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class TaskUpdateImage extends BukkitRunnable {

    private ImageMap imageMap;
    private Main main;

    public TaskUpdateImage(Main main, ImageMap imageMap) {
        this.main = main;
        this.imageMap = imageMap;
    }

    @Override
    public void run() {
        try {
            BufferedImage image = main.getImageHelper().getImage(imageMap.getPath());
            int row = image.getHeight()/128;
            int col = image.getWidth()/128;

            MapView map;
            int index = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    map = Bukkit.getMap(imageMap.getMapIds().get(index));
                    map = main.getRenderHelper().resetRenderers(map);
                    map.setScale(MapView.Scale.FARTHEST);
                    map.setUnlimitedTracking(false);
                    map.addRenderer(new ImageMapRenderer(image.getSubimage(j * 128, i * 128, 128, 128)));
                    index++;
                }
            }
        } catch (IOException e) {
            main.getLogger().warning("Cannot load image "+imageMap.getPath()+".");
            main.getLogger().warning(e.getMessage());
            e.printStackTrace();
        }
    }

}
