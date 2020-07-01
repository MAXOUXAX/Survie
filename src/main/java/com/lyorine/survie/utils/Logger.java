package com.lyorine.survie.utils;

import org.bukkit.event.Event;
import com.lyorine.survie.Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private File file;
    private Date date;
    private Main main;

    public Logger(Main main) throws IOException {
        this.main = main;
        this.date = new Date();
        this.file = new File(main.getDataFolder()+File.separator+"logs", "current.log");
        this.file.getParentFile().mkdirs();
        this.file.createNewFile();
    }

    public void log(Event e, String str){
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(this.file, true))) {
            bw.write("("+date+")"+" | Survie > ["+e.getEventName()+"] "+str+"\n");
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void save() {
        String str = new SimpleDateFormat("dd-MM-yyyy-HH-mm").format(this.date);
        file.renameTo(new File(main.getDataFolder()+File.separator+"logs", "old-logs-" + str.toLowerCase()));
    }
}
