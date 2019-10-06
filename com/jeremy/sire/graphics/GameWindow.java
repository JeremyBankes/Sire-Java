package com.jeremy.sire.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow {

	private JFrame frame;
	private Renderer renderer;

	private Dimension priorToFullscreen;

	public GameWindow(String title, Renderer renderer) {
		this.renderer = renderer;
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel content = (JPanel) frame.getContentPane();
		content.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		content.setFocusable(false);
		content.add(renderer.getCanvas());

		Canvas canvas = renderer.getCanvas();
		content.setBackground(Color.BLACK);
		content.add(canvas);

		frame.addComponentListener(new ComponentAdapter() {

			public void componentResized(ComponentEvent event) {
				Dimension size = content.getSize();

				if (size.height * renderer.getAspectRatio() > size.width) {
					size.height = Math.round(size.width / renderer.getAspectRatio());
				} else if (size.width / renderer.getAspectRatio() > size.height) {
					size.width = Math.round(size.height * renderer.getAspectRatio());
				}

				canvas.setPreferredSize(size);
				canvas.setSize(size);
				event.getComponent().validate();
			}

		});
	}

	public GraphicsDevice getBestMonitor() {
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice bestDevice = null;
		int bestScore = 0;
		for (GraphicsDevice screen : environment.getScreenDevices()) {
			Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();
			Rectangle frameBounds = frame.getBounds();
			Rectangle2D intersection = screenBounds.createIntersection(frameBounds);
			int score = (int) (intersection.getWidth() * intersection.getHeight());
			if (score > bestScore) {
				score = bestScore;
				bestDevice = screen;
			}
		}
		return bestDevice;
	}

	public void center() {
		Rectangle screenBounds = getBestMonitor().getDefaultConfiguration().getBounds();
		Rectangle frameBounds = frame.getBounds();
		frame.setLocation(screenBounds.width / 2 - frameBounds.width / 2, screenBounds.height / 2 - frameBounds.height / 2);
	}

	public void fullscreen() {
		frame.setUndecorated(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public void setIcons(BufferedImage... icons) {
		setIcons(Arrays.asList(icons));
	}

	public void setIcons(List<? extends Image> icons) {
		frame.setIconImages(icons);
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void pack() {
		frame.pack();
	}

	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	public void setSize(int width, int height) {
		renderer.getCanvas().setPreferredSize(new Dimension(width, height));
		if (frame.isVisible()) {
			frame.pack();
		}
	}

	public void setFullScreen(boolean fullscreen) {
		frame.dispose();
		if (fullscreen) {
			priorToFullscreen = frame.getSize();
			frame.setUndecorated(true);
			frame.setBounds(getBestMonitor().getDefaultConfiguration().getBounds());
		} else {
			frame.setUndecorated(false);
			frame.setSize(priorToFullscreen);
			center();
		}
		frame.setVisible(true);
	}

	/**
	 * Returns the GameWindow's frame's content pane
	 * 
	 * @return
	 */
	public JPanel getContentPane() {
		return (JPanel) frame.getContentPane();
	}

	public boolean isFullscreen() {
		return frame.isUndecorated();
	}

	public void dispose() {
		frame.dispose();
	}

}
