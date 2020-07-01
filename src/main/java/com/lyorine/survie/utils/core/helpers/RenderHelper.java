package com.lyorine.survie.utils.core.helpers;

import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import com.lyorine.survie.Main;

import java.util.Iterator;

public class RenderHelper {

    private Main main;

    public RenderHelper(Main main) {
        this.main = main;
    }

    public MapView resetRenderers(MapView map){
        Iterator<MapRenderer> iterator = map.getRenderers().iterator();
        while(iterator.hasNext()){
            map.removeRenderer(iterator.next());
        }

        return map;
    }
}
