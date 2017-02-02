package com.prodec.keel.tools.video.helper;

import java.awt.image.BufferedImage;

import br.com.etyllica.motion.filter.color.ColorStrategy;
import br.com.etyllica.util.math.EtyllicaMath;

public class ScreenComparator {

	int tolerance = 14;
	int threshold = 30;

	int step = 1;
	int w;
	int h;

	int[][] changed;
	int[][] screen;
	boolean[][] fixed;

	public ScreenComparator(BufferedImage frame) {
		w = frame.getWidth();
		h = frame.getHeight();

		changed = new int[h][w];
		fixed = new boolean[h][w];
		screen = ScreenGenerator.generateScreen(frame);		
	}
	
	public BufferedImage generateImage() {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				image.setRGB(x, y, screen[y][x]);
			}
		}
		
		return image;
	}

	public int[][] compareScreens(int [][] toCompare) {
		for (int y = step; y < h - step * 2; y += step) {
			for (int x = step; x < w - step * 2; x += step) {
				if (fixed[y][x]) {
					continue;
				}

				if (!validColor(screen[y][x], toCompare[y][x], tolerance)) {
					for (int j = 0; j < step; j++) {
						for (int i = 0; i < step; i++) {
							screen[y+j][x+i] = toCompare[y+j][x+i];
						}
					}
					
					changed[y][x] = 0;
				} else {
					changed[y][x]++;

					if (changed[y][x] > threshold) {
						fixed[y][x] = true;
					}
				}
			}
		}
		return screen;
	}

	private static boolean validColor(int color, int compareColor, int tolerance) {
		int or = ColorStrategy.getRed(color);
		int og = ColorStrategy.getGreen(color);
		int ob = ColorStrategy.getBlue(color);

		int cr = ColorStrategy.getRed(compareColor);
		int cg = ColorStrategy.getGreen(compareColor);
		int cb = ColorStrategy.getBlue(compareColor);

		return EtyllicaMath.diffMod(or, cr) < tolerance && EtyllicaMath.diffMod(og, cg) < tolerance && EtyllicaMath.diffMod(ob, cb) < tolerance;
	}
}
