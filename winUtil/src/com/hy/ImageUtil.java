package com.hy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class ImageUtil {

    //这个方法就拿来保存，测试效果一下
    public static void save(BufferedImage image,String path) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmssSSS");
            String imageFormat;

            int type = image.getType();
            switch (type) {
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                 imageFormat = "png";
                break;
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
                imageFormat = "bmp";
                break;
            default:
                imageFormat = "jpg";
                break;
            }
            String date = simpleDateFormat.format(new Date());
            ImageIO.write(image,imageFormat , new File(path+"_"+date+"."+imageFormat));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    /**
     * Photoshop 黑白算法，默认效果
     * @param image 
     * @return 新的黑白化图片
     */
    public static BufferedImage createBlackWhiteImage(BufferedImage image) {
        return createBlackWhiteImage(image, null);
    }

    /**
     * Photoshop 黑白算法，默认效果
     * @param image
     * @radios 颜色通道配置，依次为红、黄、 绿、 青、 蓝、紫六个通道
     * @return 新的黑白化图片
     */
    public static BufferedImage createBlackWhiteImage(BufferedImage image,float[] radios) {
        int width = image.getWidth();   //获取位图的宽
        int height = image.getHeight();  //获取位图的高

        int alpha = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int max = 0;
        int min = 0;
        int mid = 0;
        int gray = 0;

        float radioMax = 0;
        float radioMaxMid = 0;

        if (radios == null) {
            //                    红        黄         绿         青         蓝        紫
            radios = new float[]{0.4f,0.6f,0.4f,0.6f,0.2f,0.8f};
        }

        //int[] pixels = new int[width*height];
        int[] pixels = image.getRGB(0, 0, width,height, null, 0, width);

//      BufferedImage result = new BufferedImage(width, height, image.getType());
        for (int i = 0; i < width; i++) {//一列列扫描
            for (int j = 0; j < height; j++) {

//              gray = image.getRGB(i, j);
                gray = pixels[j*width+i]; 


                alpha = gray >>> 24;
                r = (gray>>16) & 0x000000ff;
                g = (gray >> 8) & 0x000000ff;
                b = gray & 0x000000ff;

                if (r >= g && r>=b) {
                    max = r;
                    radioMax = radios[0];
                }
                if (g>= r && g>=b) {
                    max = g;
                    radioMax = radios[2]; 
                }
                if (b >= r && b>=g) {
                    max = b;
                    radioMax = radios[4];
                }


                if (r<=g && r<=b) { // g+ b = cyan 青色
                    min = r;
                    radioMaxMid = radios[3];
                }

                if (b <= r && b<=g) {//r+g = yellow 黄色
                    min = b;
                    radioMaxMid = radios[1];
                }
                if (g <= r && g<=b) {//r+b = m 洋红
                    min = g;
                    radioMaxMid = radios[5];
                }

                mid = r + g + b-max -min;

//              公式：gray= (max - mid) * ratio_max + (mid - min) * ratio_max_mid + min

                gray = (int) ((max - mid) * radioMax + (mid - min) * radioMaxMid + min);
                gray = (alpha << 24) | (gray << 16) | (gray << 8) | gray;


//                result.setRGB(i, j, gray);
                pixels[j*width+i] = gray;

            }

        }
        BufferedImage result = new BufferedImage(width, height, image.getType());
        result.setRGB(0, 0, width, height, pixels, 0, width);
        return result;
    }

}
