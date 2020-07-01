package com.lyorine.survie.utils.core;

import java.util.ArrayList;

public class ImageMapManager {

    private ArrayList<ImageMap> imageMaps = new ArrayList<>();

    public void addImageMap(ImageMap imageMap){
        this.imageMaps.add(imageMap);
    }

    public void removeImageMap(ImageMap imageMap){
        this.imageMaps.remove(imageMap);
    }

    public ArrayList<ImageMap> getImageMaps() {
        return imageMaps;
    }
}
