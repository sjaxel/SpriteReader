package se.sjaxel.SpriteReader;

import java.io.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
public abstract class SpriteExport {
	
	public static void write(ArrayList<BufferedImage> spriteList) {
		int i = 1;
		for (BufferedImage image : spriteList) {
			try {
				ImageIO.write(image, "png", new File(i + ".png"));
				i++;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}