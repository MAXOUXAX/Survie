package com.lyorine.survie.utils.core;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import com.lyorine.survie.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ImageMapYML  {

    private Main main;
    private UUID uuid;
    private File file;
    private FileConfiguration fileConfiguration;

    public ImageMapYML(Main main, UUID uuid) {
        this.main = main;
        this.uuid = uuid;
        this.file = new File(main.getIMAGE_MAP_DIR(), uuid.toString()+".yml");
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void write(ImageMap imageMap){
        ConfigurationSection config = this.fileConfiguration.createSection("image");
        config.set("uuid", imageMap.getUuid().toString());
        config.set("path", imageMap.getPath());
        config.set("ids", imageMap.getMapIds());

        save();
    }

    public ImageMap read(){
        ConfigurationSection config = this.fileConfiguration.getConfigurationSection("image");
        String uuid = config.getString("uuid");
        String path = config.getString("path");
        ArrayList<Integer> mapIds = (ArrayList<Integer>) config.getIntegerList("ids");

        return new ImageMap(UUID.fromString(uuid), path, mapIds);
    }

    private void save(){
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            main.getLogger().severe("Cannot save image map config file: "+uuid.toString()+".yml");
            e.printStackTrace();
        }
    }
}
