package application.tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ColorInformation extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTextField[] colorsInfo = new JTextField[4];
	
	public ColorInformation() {
		
		super.setBorder(BorderFactory.createTitledBorder("Color Information"));
		super.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets.bottom = 5;
		gbc.weightx = 1f;
		gbc.weighty = 1f;
		
		String[] names = {"R","G","B","HEX"};
		for(int index = 0 ; index < colorsInfo.length ; index++) {
			colorsInfo[index] = new JTextField();
			colorsInfo[index].setEditable(false);
			colorsInfo[index].setForeground(Color.BLACK);
			colorsInfo[index].setPreferredSize(new Dimension(60, 20));
			colorsInfo[index].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(new StringSelection(((JTextField) e.getSource()).getText()), null);
					((JTextField) e.getSource()).setCursor(null);
				}
				public void mouseEntered(MouseEvent e) {
					((JTextField) e.getSource()).setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			});
			
			
			gbc.gridy = index == 3 ? 1 : 0;
			super.add(new JLabel(names[index]), gbc);
			super.add(colorsInfo[index], gbc);
			
		}
	}
	
	public void setColor(Color color) {
		colorsInfo[0].setText(String.valueOf(color.getRed()));
		colorsInfo[1].setText(String.valueOf(color.getGreen()));
		colorsInfo[2].setText(String.valueOf(color.getBlue()));		
		colorsInfo[3].setText(String.valueOf(String.format("%02x%02x%02x",
				color.getRed(), color.getGreen(), color.getBlue())));
	}
	
}
