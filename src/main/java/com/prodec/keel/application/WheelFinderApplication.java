package com.prodec.keel.application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.etyllica.awt.SVGColor;
import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
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
	private List<Line2D> projections = new ArrayList<Line2D>();

	private boolean drawImage = false;

	private DBScan clusterer;
	private List<Cluster> clusters = new ArrayList<Cluster>();

	boolean startLine = true;

	Point2D startPoint = new Point2D(198, 262);
	Point2D endPoint = new Point2D(298, 108.0);
	Line2D base = new Line2D(startPoint, endPoint);

	int maxLineDist = 10;
	int size = 256;

	boolean[] spectrogram;

	public WheelFinderApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		//Silver Wheels
		//image = ImageLoader.getInstance().getImage("cars/1485968690020.jpg");

		//Bus
		//image = ImageLoader.getInstance().getImage("cars/1485969625131.jpg");

		//Black Wheels
		//image = ImageLoader.getInstance().getImage("cars/1485968917194.jpg");
		//image = ImageLoader.getInstance().getImage("cars/1485968917194_edit.jpg");
		image = ImageLoader.getInstance().getImage("cars/1485969694374.jpg");
		//image = ImageLoader.getInstance().getImage("cars/1485969859969.jpg");

		int w = image.getWidth();
		int h = image.getHeight();

		boolean found = false;
		Line2D currentLine = null;
		Point2D destination = new Point2D();

		//int minY = 60;
		int minY = 0;
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
					found = false;
					if (i < minY) {
						continue;
					}
					if (size > minSize) {
						addLine(currentLine);
					}
				}
			}
		}

		clusterer = new DBScan(7, 21);
		//applyModifier();
		projectLines();
		spectrogram = generateSpectrogram();
	}

	private void projectLines() {
		projections.clear();

		Double minDist = Double.MAX_VALUE;
		
		for (Line2D line: lines) {
			Point2D q = line.getP1();

			double dist = base.distance(q); 
			if (dist < minDist) {
				minDist = dist;
			}
		}
		
		//TODO Normalize image dimensions
		int magicOffset = 14;
		double maxDist = minDist + magicOffset;
		
		for (Line2D line: lines) {
			Point2D q = line.getP1();

			if (base.distance(q) < maxDist) {
				Point2D projected = base.nearestPoint(q);
				projections.add(new Line2D(q, projected));
			}
		}
	}

	private boolean[] generateSpectrogram() {
		boolean[] spectrogram = new boolean[size];
		double lineLength = base.length();

		for (int i = 0; i < projections.size()-1; i++) {
			Line2D line = projections.get(i);
			Line2D nextLine = projections.get(i+1);

			double lx = line.getP2().getX();
			double ly = line.getP2().getY();
			double nx = nextLine.getP2().getX();
			double ny = nextLine.getP2().getY();

			//Add more datails to spectrogram
			if (nx < lx + 2 && ny < ly) {
				double dist = base.getP1().distance(lx, ny);
				int ix = (int)(dist * size / lineLength);
				spectrogram[ix] = true;
			}

			double dist = base.getP1().distance(line.getP2());
			int ix = (int)(dist * size / lineLength);
			spectrogram[ix] = true;
		}

		groupSpectrogram(spectrogram);
		clearSpectrogram(spectrogram);
		
		return spectrogram;
	}
	
	private void groupSpectrogram(boolean[] spectrogram) {
		int maxDistance = 6;
		
		for (int i = 0; i < spectrogram.length; i++) {
			if (!spectrogram[i]) {
				continue;
			} else {
				int distance = 0;
				for (int j = i + 1; j < spectrogram.length-1; j++) {
					if (spectrogram[j]) {
						break;
					}
					distance++;
				}
				
				if (distance > 0 && distance < maxDistance) {
					for (int k = 0; k < distance; k++) {
						spectrogram[i + 1 + k] = true; 
					}
				}
			}
		}
	}
	
	private void clearSpectrogram(boolean[] spectrogram) {
		int minDistance = 3;
		
		for (int i = 0; i < spectrogram.length - 1; i++) {
			if (!spectrogram[i]) {
				continue;
			} else {
				int distance = 1;
				for (int j = i + 1; j < spectrogram.length - 1; j++) {
					if (!spectrogram[j]) {
						break;
					}
					distance++;
				}
				
				if (distance < minDistance) {
					for (int k = 0; k < distance; k++) {
						spectrogram[i + k] = false; 
					}
				}
				
				i += distance-1;
			}
		}
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

		int gray = BlackAndWhiteLuminosityFilter.toBlackAndWhite(rgb);
		//int gray = BlackAndWhiteAverageFilter.toBlackAndWhite(rgb);

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
		for (Line2D line: lines) {
			g.drawLine(line);
		}

		g.setColor(SVGColor.BLUE);
		for (Line2D line: projections) {
			g.drawLine(line);
		}

		g.setColor(Color.YELLOW);
		g.drawLine(startPoint, endPoint);

		drawSpectrogram(g, spectrogram);

		g.setColor(Color.WHITE);
		for (Cluster cluster: clusters) {
			g.drawCircle(cluster.centroid, 12);
			/*for (Point2D point: cluster.getPoints()) {
				g.fillRect((int)point.getX(), (int)point.getY(), 1, 1);	
			}*/
		}
	}

	private void drawSpectrogram(Graphics g, boolean[] spectrogram) {
		int ex = 10;
		int ey = 300;
		int ew = 256;
		int eh = 20;

		g.setColor(Color.YELLOW);
		g.drawLine(ex - 1, ey, ex - 1, ey + eh + 1);
		g.drawLine(ex + ew + 1, ey, ex + ew + 1, ey + eh + 1);
		g.drawLine(ex, ey + eh + 1, ex + ew, ey + eh + 1);

		g.setColor(Color.BLUE);
		for (int i = 0; i < spectrogram.length; i++) {
			if (!spectrogram[i]) {
				continue;
			}
			g.drawLine(ex + i, ey, ex + i, ey + eh);
		}
	}

}
