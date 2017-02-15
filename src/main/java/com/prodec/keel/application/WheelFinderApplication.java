package com.prodec.keel.application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.awt.SVGColor;
import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.core.linear.Line2D;
import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.tools.wheel.WheelFilter;

public class WheelFinderApplication extends Application {

	private BufferedImageSource source;
	private BufferedImage image;

	private int currentImage = 0;
	private List<String> images = new ArrayList<String>();
	
	private boolean drawImage = false;

	boolean startLine = true;

	Point2D startPoint = new Point2D(200, 304);
	Point2D endPoint = new Point2D(300, 150);
	Line2D base = new Line2D(startPoint, endPoint);
	
	int[] spectrogram;
	WheelFilter filter;

	public WheelFinderApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		source = new BufferedImageSource();
		//Silver Wheels
		//image = ImageLoader.getInstance().getImage("cars/1485968690020.jpg");

		//Bus
		//image = ImageLoader.getInstance().getImage("cars/1485969625131.jpg");

		//Black Wheels
		images.add("cars/1485968917194.jpg");
		images.add("cars/1485969694374.jpg");
		images.add("cars/1485969859969.jpg");
		
		filter = new WheelFilter(startPoint, endPoint);
		
		applyFilter();
	}
	
	private void applyFilter() {
		image = ImageLoader.getInstance().getImage(images.get(currentImage));
		source.setImage(image);
		
		Component screen = new Component(0, 0, source.getWidth(), source.getHeight());
		
		spectrogram = filter.filter(source, screen);
	}
	
	@Override
	public void updateKeyboard(KeyEvent event) {
		if(event == null) {
			System.err.println("BAD BAD BUG");
		}
		if (event.isKeyDown(KeyEvent.VK_SPACE)) {
			drawImage = !drawImage;
		}
		if (event.isKeyDown(KeyEvent.VK_RIGHT)) {
			currentImage++;
			currentImage %= images.size();
			
			applyFilter();
		} else if (event.isKeyDown(KeyEvent.VK_LEFT)) {
			currentImage += images.size() - 1;
			currentImage %= images.size();
			
			applyFilter();
		}
	}

	@Override
	public void updateMouse(PointerEvent event) {
		if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
			/*if (startLine) {
				startPoint.setLocation(event.getX(), event.getY());
				System.out.println(startPoint.getX()+" "+startPoint.getY());
				startLine = !startLine;
			} else {
				endPoint.setLocation(event.getX(), event.getY());
				System.out.println(endPoint.getX()+" "+endPoint.getY());
				startLine = !startLine;
			}*/
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(this);

		if (drawImage) {
			g.drawImage(image, 0, 0);
		}

		g.setColor(Color.RED);
		for (Line2D line: filter.getLines()) {
			g.drawLine(line);
		}

		g.setColor(SVGColor.BLUE);
		for (Line2D line: filter.getProjections()) {
			g.drawLine(line);
		}

		g.setColor(Color.YELLOW);
		g.drawLine(startPoint, endPoint);

		drawSpectrogram(g, spectrogram);
	}

	private void drawSpectrogram(Graphics g, int[] spectrogram) {
		int ex = 10;
		int ey = 300;
		int ew = 256;
		int eh = 20;

		g.setColor(Color.YELLOW);
		g.drawLine(ex - 1, ey, ex - 1, ey + eh + 1);
		g.drawLine(ex + ew + 1, ey, ex + ew + 1, ey + eh + 1);
		g.drawLine(ex, ey + eh + 1, ex + ew, ey + eh + 1);

		for (int i = 0; i < spectrogram.length; i++) {
			if (spectrogram[i] <= 0) {
				continue;
			}
			g.setColor(new Color(0, 0, spectrogram[i]));
			g.drawLine(ex + i, ey, ex + i, ey + eh);
		}
	}

}
