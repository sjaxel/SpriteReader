package se.sjaxel.SpriteReader;

import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;


public class SpriteReader {
	private Path path;
	private BufferedImage spritesheet;
	private int row, col;
	private int xOffset, yOffset;
	private int spriteH;
	private int spriteW;
	
	static final int OFFSET_MIN = 0;
	static final int OFFSET_MAX = 30;
	static final int OFFSET_INIT = 0; 
	
	public SpriteReader(Path path, int row, int col) {
		this.path = path;
		this.row = row; this.col = col;
		this.xOffset = 0; this.yOffset = 0;
		loadImage();
		spriteH = (spritesheet.getHeight()/row);
		spriteW = (spritesheet.getWidth()/col);			
	}
	
	public SpriteReader(String pathstring, int row, int col) {
		this(Paths.get(pathstring), row, col);
	}
	
	public void setYOffset(int y) {
		yOffset = y;
		
	}
	
	public void setXOffset(int x) {
		xOffset = x;
	}

	public BufferedImage getSprite(int n) {
		int j = ((n-1) % col);
		int i = ((n-1)/col);
		BufferedImage sprite = null;
		try {
		sprite = spritesheet.getSubimage(j*spriteW+xOffset, i*spriteH+yOffset, spriteW, spriteH);
		return sprite;
		} catch (RasterFormatException e) {
			return placeHolder();
		}
	}
	
	private BufferedImage placeHolder() {
		return new BufferedImage(spriteH, spriteW, BufferedImage.TYPE_INT_ARGB);
	}
	
	private void loadImage() {
		spritesheet = null;
		try {
			spritesheet = ImageIO.read(path.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}