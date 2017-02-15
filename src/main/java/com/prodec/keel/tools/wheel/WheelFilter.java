package com.prodec.keel.tools.wheel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.etyllica.core.linear.Line2D;
import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.motion.core.source.ImageSource;
import br.com.etyllica.motion.feature.Component;
import br.com.etyllica.motion.filter.color.ColorStrategy;
import br.com.etyllica.motion.filter.image.BlackAndWhiteLuminosityFilter;

public class WheelFilter {

	private Set<Point2D> edges = new HashSet<Point2D>();
	private List<Line2D> lines = new ArrayList<Line2D>();
	private List<Line2D> projections = new ArrayList<Line2D>();
	
	int maxLineDist = 10;
	int size = 256;

	int[] spectrogram;
	int resolution = 255;
	
	Line2D base;
	
	public WheelFilter(Point2D start, Point2D end) {
		this.base = new Line2D(start, end);
	}
	
	public int[] filter(ImageSource source, Component component) {
		edges.clear();
		lines.clear();
		projections.clear();
		
		findEdges(source, component);
		projectLines();
		spectrogram = generateSpectrogram();
		return spectrogram;
	}
	
	private void findEdges(ImageSource source, Component component) {
		int w = component.getW();
		int h = component.getH();
		
		boolean found = false;
		Line2D currentLine = null;
		Point2D destination = new Point2D();

		int minSize = 18;
		int length = 0;
		int step = 2;
		int horizontalStep = 1;

		for (int j = component.getX(); j < w; j += horizontalStep) {
			found = false;
			length = 0;
			for (int i = component.getY() + h - 1; i >= component.getY(); i -= step) {
				int rgb = source.getRGB(j, i);

				if (validateColor(rgb)) {
					if (!found) {
						length = 0;
						found = true;
						destination = new Point2D(j, i);
						currentLine = new Line2D(new Point2D(j, i), destination);
					} else {
						length += step;
						destination.setY(i);
					}
				} else if(found) {
					found = false;
					
					if (length > minSize) {
						addLine(currentLine);
					}
				}
			}
		}
	}
	
	private void projectLines() {
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

	private int[] generateSpectrogram() {
		int[] spectrogram = new int[size];
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
				spectrogram[ix] = resolution;
			}

			double dist = base.getP1().distance(line.getP2());
			int ix = (int)(dist * size / lineLength);
			spectrogram[ix] = resolution;
		}

		groupSpectrogram(spectrogram);
		clearSpectrogram(spectrogram);
		
		return spectrogram;
	}
	
	private void groupSpectrogram(int[] spectrogram) {
		int maxDistance = 7;
		
		for (int i = 0; i < spectrogram.length; i++) {
			if (spectrogram[i] <= 0) {
				continue;
			} else {
				int distance = 0;
				for (int j = i + 1; j < spectrogram.length-1; j++) {
					if (spectrogram[j] > 0) {
						break;
					}
					distance++;
				}
				
				if (distance > 0 && distance < maxDistance) {
					for (int k = 0; k < distance; k++) {
						spectrogram[i + 1 + k] = resolution; 
					}
				}
			}
		}
	}
	
	private void clearSpectrogram(int[] spectrogram) {
		int minDistance = 3;
		
		for (int i = 0; i < spectrogram.length - 1; i++) {
			if (spectrogram[i] <= 0) {
				continue;
			} else {
				int distance = 1;
				for (int j = i + 1; j < spectrogram.length - 1; j++) {
					if (spectrogram[j] <= 0) {
						break;
					}
					distance++;
				}
				
				if (distance < minDistance) {
					for (int k = 0; k < distance; k++) {
						spectrogram[i + k] = 0; 
					}
				}
				
				i += distance-1;
			}
		}
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

		int maxColor = 0x92;
		int minColor = 0x12;

		boolean checkGray = gray > minColor + 30;

		boolean checkRed = red < maxColor && red >= minColor;
		boolean checkGreen = green < maxColor && green >= minColor;
		boolean checkBlue = blue < maxColor && blue >= minColor;

		boolean isLeaf = green > gray;

		return checkGray && checkRed && checkGreen && checkBlue && !isLeaf;
	}

	public List<Line2D> getLines() {
		return lines;
	}
	
	public List<Line2D> getProjections() {
		return projections;
	}
}
