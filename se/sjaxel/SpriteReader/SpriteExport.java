package se.sjaxel.SpriteReader;

import java.io.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
public abstract class SpriteExport {
	
	public static void write(ArrayList<BufferedImage> spriteList, String format, SpriteGUI mainwindow) {
		int i = 1;
		for (BufferedImage image : spriteList) {
			try {
				File file = new File(i + "." + format);
				ImageIO.write(image, format, file);
				i++;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		showMessage("Export completed!", mainwindow);
	}
	
	public static void showMessage(String message, SpriteGUI mainwindow) {
		JOptionPane.showMessageDialog(mainwindow, message);
	}
}