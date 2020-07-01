package com.lyorine.survie.utils.core;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class ImageMapRenderer extends MapRenderer {

    private boolean shouldRender;
    private BufferedImage image;

    public ImageMapRenderer(BufferedImage image) {
        this.image = image;
        this.shouldRender = true;
    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
        if(shouldRender){
            canvas.drawImage(0, 0, image);
            shouldRender = false;
        }
    }

    public void setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
