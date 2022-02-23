package application;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import application.tools.ColorInformation;
import application.tools.ColorPickerPanel;
import application.tools.ColorPickerPanel.SelectListener;
import application.tools.ScreenCapture;

public class JColorPicker implements ActionListener, SelectListener {
	
	public static String appTitle = "JColorPicker";
	public static Dimension appSize = new Dimension(300, 265);
	
	private ColorPickerPanel colorPicker;
	private ColorInformation colorInformation;
	private ScreenCapture capture;
	
	private Color defaultColor = Color.BLACK;
	private int captureSize = 48;
	
	private boolean autoPick = false;

	public JColorPicker() throws AWTException {
		colorInformation = new ColorInformation();
		colorInformation.setColor(defaultColor);

		colorPicker = new ColorPickerPanel(defaultColor, captureSize);
		colorPicker.setSelectListener(this);
		
		capture = new ScreenCapture(captureSize);
		initialize();
	}
	
	public void initialize() {
		JFrame frame = new JFrame(appTitle);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setPreferredSize(appSize);
		frame.setMaximumSize(appSize);
		frame.setMinimumSize(appSize);
		
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.pack();

		frame.add(colorPicker, BorderLayout.PAGE_START);
		frame.add(colorInformation, BorderLayout.CENTER);
		frame.add(getButtonsRow(), BorderLayout.PAGE_END);
		
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private JButton pickColor;
	private JCheckBox autoPickCheckBox;
	public JPanel getButtonsRow() {
		JPanel row = new JPanel();
		
		pickColor = new JButton("Pick Color");
		pickColor.setPreferredSize(new Dimension(120, 20));
		pickColor.setActionCommand("pick");
		pickColor.addActionListener(this);
		pickColor.setFocusable(false);
		row.add(pickColor);
		
		autoPickCheckBox = new JCheckBox("Auto pick avter 5s");
		autoPickCheckBox.setActionCommand("autopick");
		autoPickCheckBox.addActionListener(this);
		autoPickCheckBox.setSelected(autoPick);
		autoPickCheckBox.setFocusable(false);
		row.add(autoPickCheckBox);
		return row;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("pick")) {
			
			pickColor.setEnabled(false);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					if(autoPick) {
						String lastText = pickColor.getText();
						long lastCurrentTimeMills = System.currentTimeMillis();
						while(System.currentTimeMillis() - lastCurrentTimeMills <= 5000) {
							pickColor.setText("Pick after " + (5 - ((System.currentTimeMillis() - lastCurrentTimeMills) / 1000)));
							
							if(System.currentTimeMillis() - lastCurrentTimeMills >= 4500) {
								capture.show();
							}
							
							if(!autoPick) {
								pickColor.setText(lastText);
								pickColor.setEnabled(true);
								return;
							}
						}
						pickColor.setText(lastText);
						capture.clear();
					}else {
						while(!capture.isMousePressed()) {
							capture.show();
						}
					}
					
					colorPicker.setSelectedColor(capture.getColorAtPosition());
					colorPicker.setImage(capture.getImage());
					autoPickCheckBox.setEnabled(true);
					pickColor.setEnabled(true);
					capture.clear();
				}
			}).start();
			
		}else if(e.getActionCommand().equals("autopick")) {
			((JCheckBox) e.getSource()).setSelected(autoPick = !autoPick);
		}
	}
	
	@Override
	public void colorSelected(Color color) {
		if(color != null)
			colorInformation.setColor(color);
	}
	
	public static void main(String[] args) throws AWTException {	
		new JColorPicker();
	}
}
