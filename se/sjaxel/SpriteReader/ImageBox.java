package se.sjaxel.SpriteReader;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class ImageBox extends JLabel implements MouseListener, KeyListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Status state;
	BufferedImage image;
	SpriteGUI gui;
	private int index;
    enum Status {
        VISIBLE, SELECTED;
    }
	ImageBox(int ind, BufferedImage i, SpriteGUI gui) {
		this.gui = gui;
		index = ind;
		image = i;
		setIcon(new ImageIcon(image));
		setState(Status.VISIBLE);
		addMouseListener(this);
		addKeyListener(this);
	}
	
	public void setState(Status state) {
		this.state = state;
		if (state == Status.VISIBLE) {
			setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.white));
		} else if (state == Status.SELECTED) {
			setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.red));
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
	
	public void keyTyped(KeyEvent e) {
    }


    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 16) {
			gui.setModkey(SpriteGUI.ModKey.SELECT);
		} else if (e.getKeyCode() == 18) {
			gui.setModkey(SpriteGUI.ModKey.DESELECT);
		}
    }


    public void keyReleased(KeyEvent e) {
    	gui.setModkey(SpriteGUI.ModKey.NONE);
    }
	
	
	public void mouseClicked(MouseEvent e) {
	    //
	    }

	public void mouseReleased(MouseEvent e) {
	    //
	    }

	public void mouseEntered(MouseEvent e) {
	    if (gui.getModkey() == SpriteGUI.ModKey.SELECT) {
	    	setState(Status.SELECTED);
	    } else if (gui.getModkey() == SpriteGUI.ModKey.DESELECT) {
	    	setState(Status.VISIBLE);
	    }
	    }

	public void mouseExited(MouseEvent e) {
	    if (gui.getModkey() == SpriteGUI.ModKey.SELECT) {
	    	setState(Status.SELECTED);
	    } else if (gui.getModkey() == SpriteGUI.ModKey.DESELECT) {
	    	setState(Status.VISIBLE);
	    }
	    }

	public void mousePressed(MouseEvent e) {
	    if (state == Status.VISIBLE) {
	    	setState(Status.SELECTED);
	    	revalidate();
	    	repaint();
	    	requestFocus();
	    } else if (state == Status.SELECTED) {
	    	setState(Status.VISIBLE);
	    	revalidate();
	    	repaint();
	    	requestFocus();
	    }
	}
	
}