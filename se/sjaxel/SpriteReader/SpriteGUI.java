package se.sjaxel.SpriteReader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SpriteGUI extends JFrame 
			implements ChangeListener, ActionListener,  PropertyChangeListener {
	
	private final JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
	private SpriteReader reader;
	private JPanel picPanel;
	private JPanel buttonPanel;
	private ArrayList<BufferedImage> workingSprites;
	private ArrayList<ImageBox> workingBoxes;
	
	SpriteGUI () {
		picPanel = new JPanel();
		setPreferredSize(new Dimension(640, 480));
	}
	
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (source.getName() == "xOffset" && reader != null) {
			reader.setXOffset(source.getValue());
			updatePicPanel("reload");
		} else if (source.getName() == "yOffset" && reader != null) {
			reader.setYOffset(source.getValue()); 
			updatePicPanel("reload");
		}	
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "loadimage") {
			loadReader();
		} else if (e.getActionCommand() == "reload") {
			updatePicPanel("reload");
		} else if (e.getActionCommand() == "update") {
			updatePicPanel("update");
		} else if (e.getActionCommand() == "export" && workingBoxes != null) {
			SpriteExport.write(getCurrentSprites());
		} 
	}
	
	public void propertyChange(PropertyChangeEvent e) {
		JFormattedTextField source = (JFormattedTextField)e.getSource();
		if (source.getName() == "xgrid" && reader != null) {
			reader.setXGrid((int)e.getNewValue());
		} else if (source.getName() == "ygrid" && reader != null) {
			reader.setYGrid((int)e.getNewValue());
		}
	}
	
	private void loadReader() {
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            reader = new SpriteReader(file, 9, 13);
            updatePicPanel("reload");
        }
	}
	
	private void updatePicPanel(String mode) {
		picPanel.removeAll();
		if (mode == "reload" && reader != null) {
			populate();
		} else if (mode == "update" && workingBoxes != null) {
			populate(workingBoxes);
		} 
		picPanel.revalidate();
		picPanel.repaint();
	}
	
	public void populate() {
		workingSprites = reader.getSprites();
		workingBoxes = new ArrayList<ImageBox>();
		for (BufferedImage image : workingSprites) {
			ImageBox box = new ImageBox(image, reader);
			workingBoxes.add(box);
			picPanel.add(box);
		}	
	}
	public void populate(ArrayList<ImageBox> current) {
		for (ImageBox box : current) {
			if (box.state == ImageBox.Status.VISIBLE) {
				picPanel.add(box);
			}
		}	
	}	
	private void addSlider(String name, int orientation, String placement, int min, int max, int value) {
		JSlider slider = new JSlider(orientation, min, max, value);
		slider.setName(name);
		sliderSettings(slider);
		add(slider, placement);
	}
	
	private void sliderSettings(JSlider slider) {
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}
	
	private void addButtonsPanel() {
		buttonPanel = new JPanel();
		addButton("Load spritesheet", "loadimage", buttonPanel);
		addFormatTextField("xgrid", 1, 4, buttonPanel);
		addFormatTextField("ygrid", 1, 4, buttonPanel);
		addButton("Update", "update", buttonPanel);
		addButton("Reload", "reload", buttonPanel);
		addButton("Export", "export", buttonPanel);
		add(buttonPanel, BorderLayout.NORTH);
		
	}
	
	private void addButton(String name, String actioncommand, JPanel parent) {
		JButton button = new JButton(name);
		button.addActionListener(this);
		button.setActionCommand(actioncommand);
		parent.add(button);
	}
	
	private void addFormatTextField(String name, int value, int columns, JPanel parent) {
		JFormattedTextField field = new JFormattedTextField();
		field.setValue(new Integer(value));
		field.setName(name);
		field.setColumns(columns);
		field.addPropertyChangeListener("value", this);
		parent.add(field);
	}
	
	public void display() {
		addSlider("xOffset", JSlider.HORIZONTAL, BorderLayout.SOUTH, -30, 30, 1);
		addSlider("yOffset", JSlider.VERTICAL, BorderLayout.WEST, -30, 30, 1);
		picPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(picPanel, BorderLayout.CENTER);
		addButtonsPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public ArrayList<BufferedImage> getCurrentSprites() {
		ArrayList<BufferedImage> spriteList = new ArrayList<BufferedImage>();
		for (ImageBox box : workingBoxes) {
			if (box.state == ImageBox.Status.VISIBLE) {
				spriteList.add(box.getImage());
			}
		}
		return spriteList;
	}
	
	public static void main(String[] args) {
		SpriteGUI main = new SpriteGUI();
		main.display();
	}
}
