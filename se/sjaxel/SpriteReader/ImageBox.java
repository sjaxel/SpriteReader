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
    enum Status {
        VISIBLE, SELECTED;
    }
	ImageBox(BufferedImage i, SpriteReader reader) {
		image = i;
		setIcon(new ImageIcon(image));
		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.white));
		addMouseListener(this);
		state = Status.VISIBLE;
	}
	
	public BufferedImage getImage() {
		return image;
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
	    	state = Status.SELECTED;
	    	setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
	    	revalidate();
	    	repaint();
	    } else if (state == Status.SELECTED) {
	    	state = Status.VISIBLE;
	    	setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.WHITE));
	    	revalidate();
	    	repaint();
	    }
	}
	
}