package com.hy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {

public static void main(String[] args) {
    String url = "men.png";
    try {
        BufferedImage colorImage = ImageIO.read(new File(url));
        ImageUtil.save(colorImage, "color");

        long time = System.currentTimeMillis();
        BufferedImage grayImage = ImageUtil.createBlackWhiteImage(colorImage);
        System.out.println(System.currentTimeMillis()-time);
        ImageUtil.save(grayImage, "gray");

    } catch (IOException e) {

        e.printStackTrace();
    }
}   
}