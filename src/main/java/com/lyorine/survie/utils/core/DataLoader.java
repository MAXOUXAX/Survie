package com.lyorine.survie.utils.core;

import com.lyorine.survie.Main;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DataLoader {

    private Main main;

    public DataLoader(Main main) {
        this.main = main;
    }

    public void loadMaps() throws IOException {
        File imageDir = main.getIMAGE_DIR();
        File imageMapDir = main.getIMAGE_MAP_DIR();
        if(!imageDir.exists()){
            if(!imageDir.mkdirs()){
                throw new IOException("Cannot create image directory");
            }
        }
        if(!imageMapDir.exists()){
            if(!imageMapDir.mkdirs()){
                throw new IOException("Cannot create image maps directory");
            }
        }

        File[] files = imageMapDir.listFiles();
        if(files != null){
            ImageMap imageMap;
            ImageMapYML imageMapYML;

            for(File file : files){
                if(file.getName().endsWith(".yml")){
                    imageMapYML = new ImageMapYML(main, UUID.fromString(file.getName().replaceAll(".yml", "")));
                    imageMap = imageMapYML.read();

                    main.getImageMapManager().addImageMap(imageMap);
                    new TaskUpdateImage(main, imageMap).runTask(main);
                }
            }
        }
    }

}
