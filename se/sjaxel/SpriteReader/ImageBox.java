package se.sjaxel.SpriteReader;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class ImageBox extends JLabel implements MouseListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Status state;
	BufferedImage image;
	private int index;
    enum Status {
        VISIBLE, SELECTED;
    }
	ImageBox(int ind, BufferedImage i, SpriteReader reader) {
		index = ind;
		image = i;
		setIcon(new ImageIcon(image));
		setState(Status.VISIBLE);
		addMouseListener(this);
	}
	
	public void setState(Status state) {
		this.state = state;
		if (state == Status.VISIBLE) {
			setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.white));
		} else if (state == Status.SELECTED) {
			setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.red));
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage i) {
		image = i;
		setIcon(new ImageIcon(image));
	}
	
	public int getIndex() {
		return index;
	}
	
	public void mouseClicked(MouseEvent e) {
	    //
	    }

	public void mouseReleased(MouseEvent e) {
	    //
	    }

	public void mouseEntered(MouseEvent e) {
	    //
	    }

	public void mouseExited(MouseEvent e) {
	    //
	    }

	public void mousePressed(MouseEvent e) {
	    if (state == Status.VISIBLE) {
	    	setState(Status.SELECTED);
	    	revalidate();
	    	repaint();
	    } else if (state == Status.SELECTED) {
	    	setState(Status.VISIBLE);
	    	revalidate();
	    	repaint();
	    }
	}
	
}