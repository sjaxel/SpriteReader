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
	private JPanel westSlider;
	private JPanel southSlider;
	private JPanel buttonPanel;
	private ArrayList<ImageBox> workingBoxes;
	private ModKey key;
	private EditMode mode;
    enum ModKey {
        SELECT, DESELECT, NONE;
    }
    enum EditMode {
    	BASE, SPRITES;
    }
    
	SpriteGUI () {
		key = ModKey.NONE;
		mode = EditMode.BASE;
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
		} else if (source.getName() == "yIndent" && reader != null) {
			reader.setYIndent(source.getValue()); 
			updatePicPanel("update");
		} else if (source.getName() == "xIndent" && reader != null) {
			reader.setXIndent(source.getValue()); 
			updatePicPanel("update");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "loadimage") {
			loadReader();
		} else if (e.getActionCommand() == "reload") {
			updatePicPanel("reload");
		} else if (e.getActionCommand() == "editmode") {
			switchEditMode();
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
	
	public EditMode getEditMode() {
		return mode;
	}
	
	public void switchEditMode() {
		if (mode == EditMode.BASE) {
			mode = EditMode.SPRITES;
		} else if (mode == EditMode.SPRITES) {
			mode = EditMode.BASE;
		}
		updateSliderPanels();
		updatePicPanel("update");
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
		if (picPanel == null) {
			picPanel = new JPanel();
			picPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			add(picPanel, BorderLayout.CENTER);
		} else {
			picPanel.removeAll();
		}
		if (mode == "reload" && reader != null) {
			reload();
		} else if (mode == "update" && workingBoxes != null) {
			update();
		} 
		revalidate();
		repaint();
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
			if (box.state == ImageBox.Status.VISIBLE && !(mode == EditMode.BASE)) {
				picPanel.add(box);
			} else if (box.state == ImageBox.Status.SELECTED  || mode == EditMode.BASE) {
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
	
	private void updateSliderPanels() {
		if (westSlider == null || southSlider == null) {
			westSlider = new JPanel();
			westSlider.setLayout(new BoxLayout(westSlider, BoxLayout.LINE_AXIS));
			southSlider = new JPanel();
			southSlider.setLayout(new BoxLayout(southSlider, BoxLayout.PAGE_AXIS));
		} else {
			westSlider.removeAll();
			southSlider.removeAll();
			remove(westSlider);
			remove(southSlider);
		}
		addSliderPanels();
	}
	
	private void addSliderPanels() {
		if (mode == EditMode.BASE) {
			addSlider("xIndent", SwingConstants.HORIZONTAL, southSlider, 0, 100, 0);
			addSlider("yIndent", SwingConstants.VERTICAL, westSlider, 0, 100, 0);
		} else if (mode == EditMode.SPRITES) {
			addSlider("xOffset", SwingConstants.HORIZONTAL, southSlider, -30, 30, 0);
			addSlider("yOffset", SwingConstants.VERTICAL, westSlider, -30, 30, 0);
		}
		add(westSlider, BorderLayout.WEST);
		add(southSlider, BorderLayout.SOUTH);
		revalidate();
		repaint();
	}
	
	private void addSlider(String name, int orientation, JPanel target, int min, int max, int value) {
		JSlider slider = new JSlider(orientation, min, max, value);
		slider.setName(name);
		sliderSettings(slider);
		target.add(slider);
	}
	
	private void sliderSettings(JSlider slider) {
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
	}
	
	private void updateButtonsPanel() {
		if (buttonPanel != null) {
			buttonPanel.removeAll();
		} else {
		buttonPanel = new JPanel();
		}
		addButtonsPanel();
	}
	
	private void addButtonsPanel() {
		addButton("Load spritesheet", "loadimage", buttonPanel);
		addButton("Switch edit mode", "editmode", buttonPanel);
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
	
	private void mainWindowSettings() {
		setPreferredSize(new Dimension(640, 480));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void displayMainWindow() {
		updateSliderPanels();
		updateButtonsPanel();
		mainWindowSettings();
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
		main.displayMainWindow();
	}
}
