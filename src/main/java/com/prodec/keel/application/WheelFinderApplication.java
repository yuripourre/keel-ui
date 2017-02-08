package com.prodec.keel.application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.core.linear.Line2D;
import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.classifier.cluster.Cluster;
import br.com.etyllica.motion.classifier.cluster.DBScan;
import br.com.etyllica.motion.filter.color.ColorStrategy;
import br.com.etyllica.motion.filter.image.BlackAndWhiteAverageFilter;
import br.com.etyllica.motion.filter.image.BlackAndWhiteLuminosityFilter;

public class WheelFinderApplication extends Application {

	private BufferedImage image;
	
	private Set<Point2D> edges = new HashSet<Point2D>();
	private List<Line2D> lines = new ArrayList<Line2D>();
	
	private boolean drawImage = false;
	
	private DBScan clusterer;
	private List<Cluster> clusters = new ArrayList<Cluster>();
	
	public WheelFinderApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		//image = ImageLoader.getInstance().getImage("cars/1485968690020.jpg");
		
		image = ImageLoader.getInstance().getImage("cars/1485968917194.jpg");
		//image = ImageLoader.getInstance().getImage("cars/1485968917194_edit.jpg");
		
		//image = ImageLoader.getInstance().getImage("cars/1485969625131.jpg");
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		boolean found = false;
		Line2D currentLine = null;
		Point2D destination = new Point2D();
		
		int minSize = 18;
		int size;
		int step = 2;
		
		for (int j = 0; j < w; j++) {
			found = false;
			size = 0;
			for (int i = h - 1; i >= 0; i-=step) {
				int rgb = image.getRGB(j, i);
				
				if (validateColor(rgb)) {
					if (!found) {
						size = 0;
						found = true;
						destination = new Point2D(j, i);
						currentLine = new Line2D(new Point2D(j, i), destination);
					} else {
						size += step;
						destination.setY(i);
					}
				} else if(found) {
					if (size > minSize) {
						addLine(currentLine);
					}
					found = false;
				}
			}
		}
		
		clusterer = new DBScan(7, 21);
		//applyModifier();
	}
	
	private void applyModifier() {
		clusters = clusterer.cluster(edges);
	}

	private void addLine(Line2D currentLine) {
		lines.add(currentLine);
		int ox = (int) currentLine.getP1().getX();
		int oy = (int) currentLine.getP1().getY();
		int delta = (int)(oy - currentLine.getP2().getY());
		for (int i = 0; i < delta; i++) {
			edges.add(new Point2D(ox, oy + i));
		}
	}
	
	private boolean validateColor(int rgb) {
		int red = ColorStrategy.getRed(rgb);
		int green = ColorStrategy.getGreen(rgb);
		int blue = ColorStrategy.getBlue(rgb);
		
		//int gray = BlackAndWhiteLuminosityFilter.toBlackAndWhite(rgb);
		int gray = BlackAndWhiteAverageFilter.toBlackAndWhite(rgb);
		
		int maxColor = 0x92;
		int minColor = 0x12;
		
		boolean checkGray = gray > minColor + 30;
		
		boolean checkRed = red < maxColor && red >= minColor;
		boolean checkGreen = green < maxColor && green >= minColor;
		boolean checkBlue = blue < maxColor && blue >= minColor;
 
		boolean isLeaf = green > gray;
		
		return checkGray && checkRed && checkGreen && checkBlue && !isLeaf;
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		if (event.isKeyDown(KeyEvent.VK_SPACE)) {
			drawImage = !drawImage;
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
		for (Line2D line: lines) {
			g.drawLine(line);	
		}

		g.setColor(Color.WHITE);
		for (Cluster cluster: clusters) {
			g.drawCircle(cluster.centroid, 12);
			/*for (Point2D point: cluster.getPoints()) {
				g.fillRect((int)point.getX(), (int)point.getY(), 1, 1);	
			}*/
		}
	}

}
