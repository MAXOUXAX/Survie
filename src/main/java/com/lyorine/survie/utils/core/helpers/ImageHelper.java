package com.lyorine.survie.utils.core.helpers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageHelper {

    public boolean isURL(String path){
        return path.startsWith("http://") || path.startsWith("https://");
    }

    public BufferedImage getImage(String path) throws IOException {
        if(isURL(path)){
            URL url = new URL(path);

            return ImageIO.read(url);
        }else{
            final File imageFile = new File(path);
            if(imageFile.exists()){
                return ImageIO.read(imageFile);
            }else{
                throw new IOException("Image not found.");
            }
        }
    }

}
