package se.sjaxel.SpriteReader;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class SpriteGUI extends JFrame implements ChangeListener {
	private SpriteReader reader;
	private JPanel picPanel;
	private int[] picIndex;
	
	SpriteGUI (SpriteReader reader) {
		this.reader = reader;
		picPanel = new JPanel();
	}
	
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (source.getName() == "xOffset") {
			reader.setXOffset(source.getValue());
			updatePicPanel();
		} else if (source.getName() == "yOffset") {
			reader.setYOffset(source.getValue()); 
			updatePicPanel();
		}	
	}
	
	private void updatePicPanel() {
		picPanel.removeAll();
		populate(picIndex);
		picPanel.revalidate();
		picPanel.repaint();
	}
	
	public void populate(int[] index) {
		picIndex = index;
		for (int i : index) {
			addImage(i);
		}	
	}

	private void addImage(int n) {
		JLabel picture = new JLabel(new ImageIcon(reader.getSprite(n)));
		picture.setBorder(BorderFactory.createMatteBorder(
                2, 2, 2, 2, Color.white));
		picPanel.add(picture);
	}
	
	private void addSlider(String name, int orientation, String placement, int min, int max, int value) {
		JSlider slider = new JSlider(orientation, min, max, value);
		slider.setName(name);
		sliderSettings(slider);
		add(slider, placement);
	}
	
	public void sliderSettings(JSlider slider) {
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}
	
	public void display() {
		addSlider("xOffset", JSlider.HORIZONTAL, BorderLayout.SOUTH, -30, 30, 0);
		addSlider("yOffset", JSlider.HORIZONTAL, BorderLayout.SOUTH, -30, 30, 0);
		picPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		add(picPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		SpriteReader sreader = new SpriteReader("lolsprite.jpg", 9, 13);
		SpriteGUI main = new SpriteGUI(sreader);
		int[] picArray = {1, 13, 44, 105, 117};
		main.populate(picArray);
		main.display();
	}
}
