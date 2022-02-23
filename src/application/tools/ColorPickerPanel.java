package application.tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ColorPickerPanel extends JPanel{
	
	private SelectListener selectListener;
	public static interface SelectListener {
		public void colorSelected(Color color);	
	}
	
	private static final long serialVersionUID = 1L;
	private Color selectedColor = Color.PINK;
	
	private JLabel imageView;
	private JLabel colorView;
	private JPanel colorsBox;
	
	public ColorPickerPanel(Color defaultColor, int size) {
		this.selectedColor = defaultColor != null ? defaultColor: this.selectedColor;

		super.setBorder(BorderFactory.createTitledBorder("Color Selection"));
		super.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		initializeImageView();
		super.add(imageView, gbc);
		
		gbc.gridy = 1;
		initializeColorView();
		super.add(colorView, gbc);

		gbc.gridy = 0;
		gbc.weightx = .5f;
		gbc.gridheight = 2;
		gbc.insets.left = 2;
		super.add(initializeColorsBox(), gbc);
	}
	
	public void initializeImageView() {
		imageView = new JLabel();
		imageView.setPreferredSize(new Dimension(64, 64));
		imageView.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		
		imageView.setOpaque(true);
		imageView.setBackground(selectedColor);
	}
	
	public void initializeColorView() {
		colorView = new JLabel();
		colorView.setPreferredSize(new Dimension(64, 32));
		colorView.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

		colorView.setOpaque(true);
		colorView.setBackground(selectedColor);
	}
	
	public JScrollPane initializeColorsBox() {
		colorsBox = new JPanel();
		colorsBox.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane(colorsBox);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Colors"));
		scrollPane.setPreferredSize(new Dimension((25 * 8) + 6, 100));
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		return scrollPane;
	}
	
	public void setImage(BufferedImage image) {
		
		colorsBox.removeAll();
		ArrayList<Integer> colors = new ArrayList<>();
		imageView.setIcon(new ImageIcon(image.getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH)));
		
		int xPos = 3, yPos = 0;
		
		for(int y = 0 ; y < image.getHeight() ; y++) {
			for(int x = 0 ; x < image.getWidth() ; x++) {
				
				int pixelColor = image.getRGB(x, y);
				
				if(!colors.contains(pixelColor)) {
					colors.add(pixelColor);					
					JPanel pixel = new JPanel();
					pixel.addMouseListener(new MouseAdapter() {
						@Override public void mousePressed(MouseEvent event) {
							selectedColor = pixel.getBackground();
							colorView.setBackground(selectedColor);
							if(selectListener != null)
								selectListener.colorSelected(selectedColor);
							
						}						
						@Override public void mouseEntered(MouseEvent event) {	
							if(event.getModifiers() == MouseEvent.BUTTON1_MASK) {
								selectedColor = pixel.getBackground();
								colorView.setBackground(selectedColor);
								if(selectListener != null)
									selectListener.colorSelected(selectedColor);
							}
							pixel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
							pixel.setCursor(new Cursor(Cursor.HAND_CURSOR));
						}
						@Override public void mouseExited(MouseEvent event) {
							pixel.setBorder(null);
						}
					});
					pixel.setBounds(xPos, yPos, 8, 8);
					pixel.setBackground(new Color(pixelColor));
					colorsBox.add(pixel);
					if(xPos >= 8 * 23) {
						yPos += 8;
						xPos = 3;
					}else {
						xPos += 8;						
					}
				}
			}		
		}
		
		colorsBox.setPreferredSize(new Dimension(colorsBox.getPreferredSize().width, yPos + 8));
		imageView.repaint();
		colorsBox.repaint();
	}
	
	public void setSelectListener(SelectListener selectListener) {
		this.selectListener = selectListener;
	}
	
	public void setSelectedColor(Color selectedColor) {		

		if(selectListener != null)
			selectListener.colorSelected(selectedColor);

		this.selectedColor = selectedColor;
		colorView.setBackground(selectedColor);
	}
	
	public Color getSelectedColor() {
		return selectedColor;
	}
	
}
