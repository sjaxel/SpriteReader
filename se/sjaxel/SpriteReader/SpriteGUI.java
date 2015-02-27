package se.sjaxel.SpriteReader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	private ArrayList<ImageBox> workingBoxes;
	private ModKey key;
    enum ModKey {
        SELECT, DESELECT, NONE;
    }
	SpriteGUI () {
		picPanel = new JPanel();
		setPreferredSize(new Dimension(640, 480));
		key = ModKey.NONE;
	}
	
	public void setModel(SpriteReader r) {
		if (reader != null) {
			reader.removePropertyChangeListener(this);
			addButtonsPanel();
		}
			reader = r;
		if (reader != null) {
			reader.addPropertyChangeListener(this);
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (source.getName() == "xOffset" && reader != null) {
			reader.setXOffset(source.getValue());
			updatePicPanel("update");
		} else if (source.getName() == "yOffset" && reader != null) {
			reader.setYOffset(source.getValue()); 
			updatePicPanel("update");
		}	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "loadimage") {
			loadReader();
		} else if (e.getActionCommand() == "reload") {
			updatePicPanel("reload");
		} else if (e.getActionCommand() == "delete") {
			deleteSelected();
		} else if (e.getActionCommand() == "export" && workingBoxes != null) {
			SpriteExport.write(getCurrentSprites(), "png", this);
		} else if (e.getActionCommand() == "selectall" && workingBoxes != null) {
			selectAll();
		} 
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		Object obj = e.getSource();
		if (obj instanceof JFormattedTextField) {
			JFormattedTextField source = (JFormattedTextField)obj;
			if (source.getName() == "xgrid" && reader != null) {
				reader.setXGrid((int)e.getNewValue());
				updatePicPanel("reload");
			} else if (source.getName() == "ygrid" && reader != null) {
				reader.setYGrid((int)e.getNewValue());
				updatePicPanel("reload");
			}
		} else {
			updatePicPanel("update");
		}
	}
	
	public ModKey getModkey() {
		return key;
	}
	
	public void setModkey(ModKey k) {
		key = k;
	}

	private void loadReader() {
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            setModel(new SpriteReader());
            reader.setImage(file);
            updatePicPanel("reload");
        }
	}
	
	private void updatePicPanel(String mode) {
		picPanel.removeAll();
		if (mode == "reload" && reader != null) {
			reload();
		} else if (mode == "update" && workingBoxes != null) {
			update();
		} 
		picPanel.revalidate();
		picPanel.repaint();
	}
	
	public void reload() {
		workingBoxes = new ArrayList<ImageBox>();
		for (int n=1; n<(reader.getIndex()+1); n++) {
			ImageBox box = new ImageBox(n, reader.getSprite(n), this);
			workingBoxes.add(box);
			picPanel.add(box);
		}	
	}
	public void update() {
		for (ImageBox box : workingBoxes) {
			if (box.state == ImageBox.Status.VISIBLE) {
				picPanel.add(box);
			} else if (box.state == ImageBox.Status.SELECTED) {
				box.setImage(reader.getSprite(box.getIndex()));
				picPanel.add(box);
			}
		}	
	}
	
	private void selectAll() {
		for (ImageBox box : workingBoxes) {
			if (box.state == ImageBox.Status.VISIBLE) {
				box.setState(ImageBox.Status.SELECTED);
			}
		}
		updatePicPanel("update");
	}
	
	private void deleteSelected() {
		ArrayList<ImageBox> delete = new ArrayList<ImageBox>();
		for (ImageBox box : workingBoxes) {
			if (box.state == ImageBox.Status.SELECTED) {
				delete.add(box);
			}
		}
		workingBoxes.removeAll(delete);
		updatePicPanel("update");
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
		if (buttonPanel != null) {
			buttonPanel.removeAll();
		} else {
		buttonPanel = new JPanel();
		}
		addButton("Load spritesheet", "loadimage", buttonPanel);
		buttonPanel.add(new JLabel("Rows: "));
		addFormatTextField("xgrid", 1, 4, buttonPanel);
		buttonPanel.add(new JLabel("Columns: "));
		addFormatTextField("ygrid", 1, 4, buttonPanel);
		addButton("Delete selected", "delete", buttonPanel);
		addButton("De-/select all", "selectall", buttonPanel);
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
	
	public void displayMainWindow() {
		addSlider("xOffset", SwingConstants.HORIZONTAL, BorderLayout.SOUTH, -30, 30, 1);
		addSlider("yOffset", SwingConstants.VERTICAL, BorderLayout.WEST, -30, 30, 1);
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
	
	public void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	public static void main(String[] args) {
		SpriteGUI main = new SpriteGUI();
		main.displayMainWindow();
	}
}
