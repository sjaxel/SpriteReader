package se.sjaxel.SpriteReader;


import java.awt.image.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class SpriteReader {
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private BufferedImage spritesheet;
	private ArrayList<BufferedImage> workingSprites;
	private int row, col;
	private int xOffset, yOffset;
	private int spriteH;
	private int spriteW;
	
	public SpriteReader() {
		row = 1; col = 1;
		xOffset = 0; yOffset = 0;			
	}
	
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		  pcs.addPropertyChangeListener(l);
	}
	public void removePropertyChangeListener(PropertyChangeListener l) {
		  pcs.removePropertyChangeListener(l);
	}
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
		  pcs.addPropertyChangeListener(propertyName, l);
	}
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener l) {
		  pcs.removePropertyChangeListener(propertyName, l);
	}	
	public void setYOffset(int y) {
		int oldy = yOffset;
		yOffset = y;
		updateSprites();
		pcs.firePropertyChange("xOffset", oldy, yOffset);
		
	}
		
	public void setXOffset(int x) {
		int oldx = xOffset;
		xOffset = x;
		updateSprites();
		pcs.firePropertyChange("xOffset", oldx, xOffset);
	}
	
	public void setXGrid(int row) {
		int oldRow = this.row;
		this.row = row;
		updateDimensions();
		updateSprites();
		pcs.firePropertyChange("xGrid", oldRow, row);
	}
	public void setYGrid(int col) {
		int oldCol = this.col;
		this.col = col;
		updateDimensions();
		updateSprites();
		pcs.firePropertyChange("yGrid", oldCol, col);
	}
	
	public int getIndex() {
		return row*col;
	}
	
	public BufferedImage getSprite(int n) {
		return workingSprites.get(n-1);
	}

	
	public void updateSprites() {
		ArrayList<BufferedImage> workingSpritesCopy = new ArrayList<BufferedImage>();
		if (workingSprites != null) {
			
			workingSpritesCopy = workingSprites;
			workingSprites.clear();
		} else {
			workingSprites = new ArrayList<BufferedImage>();
			workingSpritesCopy = workingSprites;
		}
		for (int n=1; n<(getIndex()+1); n++) {
			int j = ((n-1) % col);
			int i = ((n-1)/col);
			BufferedImage sprite;
			try {
				sprite = spritesheet.getSubimage((j)*spriteW+(xOffset), 
						(i)*spriteH+yOffset, spriteW, spriteH);
				workingSprites.add(sprite);
				pcs.fireIndexedPropertyChange("Sprite", n, workingSpritesCopy.get(n-1), workingSprites.get(n-1));
			} catch (RasterFormatException e) {
				workingSprites.add(placeHolder());
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Catch");
			}
		}
	}
	
	private void updateDimensions() {
		spriteH = (spritesheet.getHeight()/row);
		spriteW = (spritesheet.getWidth()/col);
	}
	
	private BufferedImage placeHolder() {
		return new BufferedImage(spriteH, spriteW, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void setImage(File file) {
		try {
			spritesheet = ImageIO.read(file);
			updateDimensions();
			updateSprites();
			//pcs.firePropertyChange("image", null, spritesheet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}