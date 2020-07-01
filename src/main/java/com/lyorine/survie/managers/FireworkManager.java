package com.lyorine.survie.managers;

import com.lyorine.survie.Main;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FireworkManager {

    private Main main;
    private Map<String, Set<FireworkEffect>> rockets = new HashMap<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private File configFile;

    public FireworkManager(Main main) {
        this.main = main;
        this.configFile = new File(main.getDataFolder(), "floc.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addLoc(Location loc){
        locations.add(loc);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        craftEffects();
        try {
            loadLocations();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        YamlConfiguration config = new YamlConfiguration();
        final int[] i = {0};
        locations.forEach(loc -> {
            config.set(i[0] +".world", loc.getWorld().getName());
            config.set(i[0] +".x", loc.getX());
            config.set(i[0] +".y", loc.getY());
            config.set(i[0] +".z", loc.getZ());
            i[0]++;
        });
        config.save(configFile);
    }

    private void loadLocations() throws IOException, InvalidConfigurationException {
        locations.clear();
        YamlConfiguration homesConfig = new YamlConfiguration();
        homesConfig.load(configFile);
        Set<String> locInt = homesConfig.getKeys(false);
        locInt.forEach(s -> {
            String world = homesConfig.getString(s+".world");
            double x = homesConfig.getDouble(s+".x");
            double y = homesConfig.getDouble(s+".y");
            double z = homesConfig.getDouble(s+".z");
            locations.add(new Location(Bukkit.getWorld(world), x, y, z));
        });
    }

    private void craftEffects(){

        FireworkEffect alrouge = FireworkEffect.builder().trail(true).withColor(Color.RED).flicker(true).with(FireworkEffect.Type.BURST).build();
        rockets.put("AL Rouge", Collections.singleton(alrouge));

        FireworkEffect alorange = FireworkEffect.builder().trail(true).withColor(Color.ORANGE).flicker(true).with(FireworkEffect.Type.BURST).build();
        rockets.put("AL Orange", Collections.singleton(alorange));

        FireworkEffect alvert = FireworkEffect.builder().trail(true).withColor(Color.LIME).flicker(true).with(FireworkEffect.Type.BURST).build();
        rockets.put("AL Vert", Collections.singleton(alvert));

        FireworkEffect alblanc = FireworkEffect.builder().trail(true).withColor(Color.WHITE).flicker(true).with(FireworkEffect.Type.BURST).build();
        rockets.put("AL Blanc", Collections.singleton(alblanc));

        FireworkEffect aljaune = FireworkEffect.builder().trail(true).withColor(Color.YELLOW).flicker(true).with(FireworkEffect.Type.BURST).build();
        rockets.put("AL Jaune", Collections.singleton(aljaune));

        FireworkEffect alrose = FireworkEffect.builder().trail(true).withColor(Color.FUCHSIA).flicker(true).with(FireworkEffect.Type.BURST).build();
        rockets.put("AL Rose", Collections.singleton(alrose));

        FireworkEffect albleu = FireworkEffect.builder().trail(true).withColor(Color.BLUE).flicker(true).with(FireworkEffect.Type.BURST).build();
        rockets.put("AL Bleu", Collections.singleton(albleu));

        FireworkEffect alcolorful = FireworkEffect.builder().trail(true).withColor(Color.RED, Color.ORANGE, Color.LIME, Color.WHITE, Color.YELLOW, Color.FUCHSIA, Color.BLUE).flicker(true).with(FireworkEffect.Type.BURST).build();
        rockets.put("AL Colorful", Collections.singleton(alcolorful));




        FireworkEffect mrouge = FireworkEffect.builder().trail(true).withColor(Color.RED).withColor(Color.WHITE).withColor(Color.RED).with(FireworkEffect.Type.BURST).flicker(true).build();
        rockets.put("M Rouge", Collections.singleton(mrouge));

        FireworkEffect morange = FireworkEffect.builder().trail(true).withColor(Color.ORANGE).withColor(Color.WHITE).withColor(Color.ORANGE).with(FireworkEffect.Type.BURST).flicker(true).build();
        rockets.put("M Orange", Collections.singleton(morange));

        FireworkEffect mvert = FireworkEffect.builder().trail(true).withColor(Color.LIME).withColor(Color.WHITE).withColor(Color.LIME).with(FireworkEffect.Type.BURST).flicker(true).build();
        rockets.put("M Vert", Collections.singleton(mvert));

        FireworkEffect mblanc = FireworkEffect.builder().trail(true).withColor(Color.WHITE).withColor(Color.WHITE).withColor(Color.WHITE).with(FireworkEffect.Type.BURST).flicker(true).build();
        rockets.put("M Blanc", Collections.singleton(mblanc));

        FireworkEffect mjaune = FireworkEffect.builder().trail(true).withColor(Color.YELLOW).withColor(Color.WHITE).withColor(Color.YELLOW).with(FireworkEffect.Type.BURST).flicker(true).build();
        rockets.put("M Jaune", Collections.singleton(mjaune));

        FireworkEffect mrose = FireworkEffect.builder().trail(true).withColor(Color.FUCHSIA).withColor(Color.WHITE).withColor(Color.FUCHSIA).with(FireworkEffect.Type.BURST).flicker(true).build();
        rockets.put("M Rose", Collections.singleton(mrose));

        FireworkEffect mbleu = FireworkEffect.builder().trail(true).withColor(Color.BLUE).withColor(Color.WHITE).withColor(Color.BLUE).with(FireworkEffect.Type.BURST).flicker(true).build();
        rockets.put("M Bleu", Collections.singleton(mbleu));

        FireworkEffect mcolorful = FireworkEffect.builder().trail(true).withColor(Color.RED, Color.ORANGE, Color.LIME, Color.WHITE, Color.YELLOW, Color.FUCHSIA, Color.BLUE).withColor(Color.WHITE).withColor(Color.RED, Color.ORANGE, Color.LIME, Color.WHITE, Color.YELLOW, Color.FUCHSIA, Color.BLUE).with(FireworkEffect.Type.BURST).flicker(true).build();
        rockets.put("M Colorful", Collections.singleton(mcolorful));





        FireworkEffect brouge = FireworkEffect.builder().trail(true).flicker(true).withColor(Color.RED).with(FireworkEffect.Type.BALL_LARGE).build();
        rockets.put("B Rouge", Collections.singleton(brouge));

        FireworkEffect borange = FireworkEffect.builder().trail(true).flicker(true).withColor(Color.ORANGE).with(FireworkEffect.Type.BALL_LARGE).build();
        rockets.put("B Orange", Collections.singleton(borange));

        FireworkEffect bvert = FireworkEffect.builder().trail(true).flicker(true).withColor(Color.LIME).with(FireworkEffect.Type.BALL_LARGE).build();
        rockets.put("B Vert", Collections.singleton(bvert));

        FireworkEffect bblanc = FireworkEffect.builder().trail(true).flicker(true).withColor(Color.WHITE).with(FireworkEffect.Type.BALL_LARGE).build();
        rockets.put("B Blanc", Collections.singleton(bblanc));

        FireworkEffect bjaune = FireworkEffect.builder().trail(true).flicker(true).withColor(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build();
        rockets.put("B Jaune", Collections.singleton(bjaune));

        FireworkEffect brose = FireworkEffect.builder().trail(true).flicker(true).withColor(Color.FUCHSIA).with(FireworkEffect.Type.BALL_LARGE).build();
        rockets.put("B Rose", Collections.singleton(brose));

        FireworkEffect bbleu = FireworkEffect.builder().trail(true).flicker(true).withColor(Color.BLUE).with(FireworkEffect.Type.BALL_LARGE).build();
        rockets.put("B Bleu", Collections.singleton(bbleu));

        FireworkEffect bcolorful = FireworkEffect.builder().trail(true).flicker(true).withColor(Color.RED, Color.ORANGE, Color.LIME, Color.WHITE, Color.YELLOW, Color.FUCHSIA, Color.BLUE).with(FireworkEffect.Type.BALL_LARGE).build();
        rockets.put("B Colorful", Collections.singleton(bcolorful));




        FireworkEffect frouge = FireworkEffect.builder()
                .withColor(Color.RED)
                .withFade(Color.BLACK, Color.WHITE)
                .with(FireworkEffect.Type.STAR)
                .trail(true)
                .flicker(true)
                .build();
        rockets.put("F Rouge", new HashSet<>(Arrays.asList(frouge, mrouge)));

        FireworkEffect forange = FireworkEffect.builder()
                .withColor(Color.ORANGE)
                .withFade(Color.BLACK, Color.WHITE)
                .trail(true)
                .flicker(true)
                .with(FireworkEffect.Type.STAR)
                .build();
        rockets.put("F Orange", new HashSet<>(Arrays.asList(forange, morange)));

        FireworkEffect fvert = FireworkEffect.builder()
                .withColor(Color.LIME)
                .withFade(Color.BLACK, Color.WHITE)
                .trail(true)
                .flicker(true)
                .with(FireworkEffect.Type.STAR)
                .build();
        rockets.put("F Vert", new HashSet<>(Arrays.asList(fvert, mvert)));

        FireworkEffect fblanc = FireworkEffect.builder()
                .withColor(Color.WHITE)
                .withFade(Color.BLACK, Color.WHITE)
                .trail(true)
                .flicker(true)
                .with(FireworkEffect.Type.STAR)
                .build();
        rockets.put("F Blanc", new HashSet<>(Arrays.asList(fblanc, mblanc)));

        FireworkEffect fjaune = FireworkEffect.builder()
                .withColor(Color.YELLOW)
                .withFade(Color.BLACK, Color.WHITE)
                .trail(true)
                .flicker(true)
                .with(FireworkEffect.Type.STAR)
                .build();
        rockets.put("F Jaune", new HashSet<>(Arrays.asList(fjaune, mjaune)));

        FireworkEffect frose = FireworkEffect.builder()
                .withColor(Color.FUCHSIA)
                .withFade(Color.BLACK, Color.WHITE)
                .trail(true)
                .flicker(true)
                .with(FireworkEffect.Type.STAR)
                .build();
        rockets.put("F Rose", new HashSet<>(Arrays.asList(frose, mrose)));

        FireworkEffect fbleu = FireworkEffect.builder()
                .withColor(Color.BLUE)
                .withFade(Color.BLACK, Color.WHITE)
                .trail(true)
                .flicker(true)
                .with(FireworkEffect.Type.STAR)
                .build();
        rockets.put("F Bleu", new HashSet<>(Arrays.asList(fbleu, mbleu)));

        FireworkEffect fcolorful = FireworkEffect.builder()
                .withColor(Color.RED, Color.ORANGE, Color.LIME, Color.WHITE, Color.YELLOW, Color.FUCHSIA, Color.BLUE)
                .withFade(Color.BLACK, Color.WHITE)
                .with(FireworkEffect.Type.STAR)
                .trail(true)
                .flicker(true)
                .build();
        rockets.put("F Colorful", new HashSet<>(Arrays.asList(fcolorful, mcolorful)));





        FireworkEffect crouge = FireworkEffect.builder().trail(true).withColor(Color.RED).with(FireworkEffect.Type.CREEPER).build();
        rockets.put("C Rouge", Collections.singleton(crouge));

        FireworkEffect corange = FireworkEffect.builder().trail(true).withColor(Color.ORANGE).with(FireworkEffect.Type.CREEPER).build();
        rockets.put("C Orange", Collections.singleton(corange));

        FireworkEffect cvert = FireworkEffect.builder().trail(true).withColor(Color.LIME).with(FireworkEffect.Type.CREEPER).build();
        rockets.put("C Vert", Collections.singleton(cvert));

        FireworkEffect cblanc = FireworkEffect.builder().trail(true).withColor(Color.WHITE).with(FireworkEffect.Type.CREEPER).build();
        rockets.put("C Blanc", Collections.singleton(cblanc));

        FireworkEffect cjaune = FireworkEffect.builder().trail(true).withColor(Color.YELLOW).with(FireworkEffect.Type.CREEPER).build();
        rockets.put("C Jaune", Collections.singleton(cjaune));

        FireworkEffect crose = FireworkEffect.builder().trail(true).withColor(Color.FUCHSIA).with(FireworkEffect.Type.CREEPER).build();
        rockets.put("C Rose", Collections.singleton(crose));

        FireworkEffect cbleu = FireworkEffect.builder().trail(true).withColor(Color.BLUE).with(FireworkEffect.Type.CREEPER).build();
        rockets.put("C Bleu", Collections.singleton(cbleu));

        FireworkEffect ccolorful = FireworkEffect.builder().trail(true).withColor(Color.RED, Color.ORANGE, Color.LIME, Color.WHITE, Color.YELLOW, Color.FUCHSIA, Color.BLUE).with(FireworkEffect.Type.CREEPER).build();
        rockets.put("C Colorful", Collections.singleton(ccolorful));






        FireworkEffect mfrouge = FireworkEffect.builder().trail(true).withColor(Color.RED, Color.BLACK).with(FireworkEffect.Type.BALL_LARGE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).build();
        rockets.put("MF Rouge", Collections.singleton(mfrouge));

        FireworkEffect mforange = FireworkEffect.builder().trail(true).withColor(Color.ORANGE, Color.BLACK).with(FireworkEffect.Type.BALL_LARGE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).build();
        rockets.put("MF Orange", Collections.singleton(mforange));

        FireworkEffect mfvert = FireworkEffect.builder().trail(true).withColor(Color.LIME, Color.BLACK).with(FireworkEffect.Type.BALL_LARGE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).build();
        rockets.put("MF Vert", Collections.singleton(mfvert));

        FireworkEffect mfblanc = FireworkEffect.builder().trail(true).withColor(Color.WHITE, Color.BLACK).with(FireworkEffect.Type.BALL_LARGE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).build();
        rockets.put("MF Blanc", Collections.singleton(mfblanc));

        FireworkEffect mfjaune = FireworkEffect.builder().trail(true).withColor(Color.YELLOW, Color.BLACK).with(FireworkEffect.Type.BALL_LARGE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).build();
        rockets.put("MF Jaune", Collections.singleton(mfjaune));

        FireworkEffect mfrose = FireworkEffect.builder().trail(true).withColor(Color.FUCHSIA, Color.BLACK).with(FireworkEffect.Type.BALL_LARGE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).build();
        rockets.put("MF Rose", Collections.singleton(mfrose));

        FireworkEffect mfbleu = FireworkEffect.builder().trail(true).withColor(Color.BLUE, Color.BLACK).with(FireworkEffect.Type.BALL_LARGE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).build();
        rockets.put("MF Bleu", Collections.singleton(mfbleu));

        FireworkEffect mfcolorful = FireworkEffect.builder().trail(true).withColor(Color.RED, Color.ORANGE, Color.LIME, Color.WHITE, Color.YELLOW, Color.FUCHSIA, Color.BLUE, Color.BLACK).with(FireworkEffect.Type.BALL_LARGE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).withFade(Color.WHITE).build();
        rockets.put("MF Colorful", Collections.singleton(mfcolorful));
    }

    public void spawnFirework(Location loc, String name){
        Location launchLocation = loc.clone();
        if(rockets.containsKey(name)) {
            if (name.startsWith("M")) {
                launchLocation.add(0,12,0);
            } else if (name.startsWith("B")) {
                launchLocation.add(0,25,0);
            } else if (name.startsWith("F")) {
                launchLocation.add(0,25,0);
            } else if (name.startsWith("C")) {
                launchLocation.add(0,25,0);
            } else if (name.startsWith("MF")) {
                launchLocation.add(0,40,0);
            }
        }


        Firework firework = (Firework)loc.getWorld().spawnEntity(launchLocation, EntityType.FIREWORK);
        Location particleLoc = loc.clone().add(0,1,0);
        loc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, particleLoc, 20, 1, 0, 1, 0.1);
        loc.getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, particleLoc, 20, 1, 0, 1, 0.1);
        loc.getWorld().spawnParticle(Particle.CLOUD, particleLoc, 20, 1, 0, 1, 0.1);
        FireworkMeta meta = firework.getFireworkMeta();
        /*boolean shouldDirect = false;

        if(rockets.containsKey(name)){
            if(name.startsWith("AL")){
                shouldDirect = true;
            }else if(name.startsWith("M")){
                meta.setPower(1);
            }else if(name.startsWith("B")){
                meta.setPower(2);
            }else if(name.startsWith("F")){
                meta.setPower(2);
            }else if(name.startsWith("C")){
                meta.setPower(2);
            }else if(name.startsWith("MF")){
                meta.setPower(3);
            }*/

            meta.addEffects(rockets.get(name));
            firework.setFireworkMeta(meta);
            firework.detonate();
            /*if(shouldDirect){
                firework.detonate();
            }
        }*/

    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void reloadLocations() {
        try {
            loadLocations();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
