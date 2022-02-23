package application.tools;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JWindow;

public class ScreenCapture {
	
	private Robot robot;
	private JWindow window;
	
	private Color colorAtPosition;
	private Dimension shotboxSize;
	private int thick = 5;
	
	private boolean mousePressed = false;
		
	public ScreenCapture(int size) throws AWTException {
		shotboxSize = new Dimension(size, size);
		robot = new Robot();

		initialize();
	}
	
	private void initialize() {
		window = new JWindow();
		window.setAlwaysOnTop(true);
		window.addMouseListener(new MouseAdapter() {
		
			public void mouseClicked(MouseEvent event) {
				mousePressed = true;
			}
			
		});
		
		window.setContentPane(new JPanel() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics gl) {
				
				colorAtPosition = robot.getPixelColor(MouseInfo.getPointerInfo().getLocation().x,
						MouseInfo.getPointerInfo().getLocation().y);
				
				gl.setColor(mousePressed ? Color.GREEN : colorAtPosition);
								
				gl.fillRect(1, 1, (getWidth() / 3)-1, thick-1);
				gl.fillRect(1, 1, thick-1, (getHeight() / 3)-1);
				gl.fillRect(getWidth() - (getWidth() / 3)+1, (getHeight()-thick) +1, (getWidth() / 3)-2, thick-2);
				gl.fillRect((getWidth() - thick) + 1, getHeight() - (getHeight() / 3)+2, thick-1, (getHeight() / 3)-2);
				
				gl.setColor(Color.WHITE);
				gl.drawRect(0, 0, getWidth()-1, getHeight()-1);
				gl.drawRect(thick-1, thick-1, (getWidth()-thick*2)+1, (getHeight()-thick*2)+1);
			}
			
		});
		
		window.setPreferredSize(new Dimension(shotboxSize.width, shotboxSize.height));
		window.setBackground(new Color(0, 0, 0, 0));
		window.pack();
	}
	
	public void show() {
		window.repaint();
		PointerInfo mouseLocation = MouseInfo.getPointerInfo();
		window.setLocation(mouseLocation.getLocation().x - shotboxSize.width / 2,
				mouseLocation.getLocation().y - shotboxSize.height / 2);
		
		if(!window.isVisible())
			window.setVisible(true);
	}
	
	public void clear() {
		window.setVisible(false);
		mousePressed = false;
		window.dispose();
	}
	
	public BufferedImage getImage() {
		window.setVisible(false);
		Rectangle screenRect = new Rectangle(window.getX()+thick, window.getY()+thick,
				shotboxSize.width-thick*2, shotboxSize.height-thick*2);
		return robot.createScreenCapture(screenRect);
	}
	
	public boolean isMousePressed() {
		return mousePressed;
	}
	
	public Color getColorAtPosition() {
		return colorAtPosition;
	}
}
