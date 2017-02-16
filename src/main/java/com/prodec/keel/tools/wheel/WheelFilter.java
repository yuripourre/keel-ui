package com.prodec.keel.tools.wheel;

import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.core.linear.Line2D;
import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.motion.core.source.ImageSource;
import br.com.etyllica.motion.feature.Component;
import br.com.etyllica.motion.filter.color.ColorStrategy;
import br.com.etyllica.motion.filter.image.BlackAndWhiteLuminosityFilter;

public class WheelFilter {

	private List<Line2D> lines = new ArrayList<Line2D>();
	private List<Line2D> projections = new ArrayList<Line2D>();
	
	int maxLineDist = 10;
	int size = 256;

	int[] spectrogram;
	int resolution = 255;
	
	Line2D base;
	
	int meanLength;
	
	public WheelFilter(Point2D start, Point2D end) {
		this.base = new Line2D(start, end);
	}
	
	public int[] filter(ImageSource source, Component component) {
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
		Point2D origin = new Point2D();

		int minSize = h / 16;//Related to image.Height
		int length = 0;
		int step = 2; //Step is necessary to avoid problems with slow capture rates camera
		int horizontalStep = 1;

		int lengthSum = 1;
		
		for (int j = component.getX(); j < w; j += horizontalStep) {
			found = false;
			length = 0;
			for (int i = component.getY() + h - 1; i >= component.getY(); i -= step) {
				int rgb = source.getRGB(j, i);

				if (validateColor(rgb)) {
					if (!found) {
						length = 0;
						found = true;
						//Define origin as destination
						origin = new Point2D(j, i);
						currentLine = new Line2D(origin, new Point2D(j, i));
					} else {
						//Update origin
						length += step;
						origin.setY(i);
					}
				} else if(found) {
					found = false;
					lengthSum += length;
					
					//If line has a valid size
					if (length > minSize) {
						lines.add(currentLine);
					}
				}
			}
		}
		
		meanLength = lengthSum / lines.size();
		//System.out.println("Mean: "+meanLength);
	}
	
	private void projectLines() {
		Double minDist = Double.MAX_VALUE;
		
		for (Line2D line: lines) {
			Point2D end = line.getP2();

			double dist = base.distance(end);
			if (dist < minDist) {
				minDist = dist;
			}
		}
		
		//TODO Normalize image dimensions
		int magicOffset = meanLength/6;
		double maxDist = minDist + magicOffset;
		
		for (Line2D line: lines) {
			Point2D end = line.getP2();

			if (base.distance(end) < maxDist) {
				Point2D projected = base.nearestPoint(end);
				projections.add(new Line2D(end, projected));
			}
		}
	}

	private int[] generateSpectrogram() {
		int[] spectrogram = new int[size];
		double lineLength = base.length();

		for (int i = 0; i < projections.size() - 1; i++) {
			Line2D line = projections.get(i);
			
			double dist = base.getP1().distance(line.getP2());
			int ix = (int)(dist * size / lineLength);
			spectrogram[ix] = resolution;
		}

		log = false;
		//clearSpectrogram(spectrogram, 1);
		//reinforceSpectrogram(spectrogram, 2);
		groupSpectrogram(spectrogram, 1, 0);
		reinforceSpectrogram(spectrogram, 2);
		//groupSpectrogram(spectrogram, 8, 0);
		
		//reinforceSpectrogram(spectrogram, 1);
		/*groupSpectrogram(spectrogram, 12, 3);
		
		log = true;
		groupSpectrogram(spectrogram, 4, 0);*/
		//clearSpectrogram(spectrogram, 2);
		//groupSpectrogram(spectrogram, 4);
		//clearSpectrogram(spectrogram, 3);
		
		return spectrogram;
	}
	
	private void reinforceSpectrogram(int[] spectrogram, int minDistance) {
		int width = 0;
		
		for (int i = 0; i < spectrogram.length; i++) {
			if (spectrogram[i] <= 0) {
				if (width >= minDistance) {
					int before = i-width-1; 
					if (before > 0) {
						spectrogram[before] = spectrogram[i-width];	
					}
					
					if(i < spectrogram.length-1) {
						spectrogram[i+1] = spectrogram[i];	
					}
						
				}
				width = 0;
				continue;
			} else {
				width++;
			}
		}
	}

	public boolean log = false;
	
	private void groupSpectrogram(int[] spectrogram, int maxDistance) {
		groupSpectrogram(spectrogram, maxDistance, 1);
	}
	
	private void groupSpectrogram(int[] spectrogram, int maxDistance, int minWidth) {
		
		int width = 0;
		
		for (int i = 0; i < spectrogram.length; i++) {
			if (spectrogram[i] <= 0) {
				width = 0;
				continue;
			} else {
				if (width < minWidth) {
					width++;
					continue;
				}
				width++;
				
				int distance = 0;
				for (int j = i + 1; j < spectrogram.length - 1; j++) {
					if (spectrogram[j] > 0) {
						break;
					}
					distance++;
				}
				
				if(log)
					System.out.println("Distance: "+distance);
				
				if (distance <= maxDistance) {
					int k = 0;
					for (; k < distance; k++) {
						spectrogram[i + 1 + k] = resolution; 
					}
					i = i + k;
				}
			}
		}
	}
	
	private void clearSpectrogram(int[] spectrogram, int minDistance) {
				
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
	
	private boolean validateColor(int rgb) {
		int red = ColorStrategy.getRed(rgb);
		int green = ColorStrategy.getGreen(rgb);
		int blue = ColorStrategy.getBlue(rgb);

		int gray = BlackAndWhiteLuminosityFilter.toBlackAndWhite(rgb);

		int maxR = 0x92;
		int minR = 0x12;
		int maxG = 0x90;
		int minG = 0x12;
		int maxB = 0x92;
		int minB = 0x12;

		boolean checkGray = gray > minB + 30;

		boolean checkRed = red < maxR && red >= minR;
		boolean checkGreen = green < maxG && green >= minG;
		boolean checkBlue = blue < maxB && blue >= minB;

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
