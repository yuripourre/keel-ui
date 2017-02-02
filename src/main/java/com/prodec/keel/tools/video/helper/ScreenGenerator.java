package com.prodec.keel.tools.video.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.AWTFrameGrab8Bit;
import org.jcodec.api.awt.AWTHoldFrameGrab;

public class ScreenGenerator {
		
	public static int[][] generateScreen(File file, int frameNumber) throws IOException, JCodecException {
		BufferedImage frame = AWTFrameGrab8Bit.getFrame(file, frameNumber);
		
		int w = frame.getWidth();
		int h = frame.getHeight();
		
		int[][] screen = new int[h][w];
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				screen[y][x] = frame.getRGB(x, y);
			}
		}
		
		return screen;
	}
	
	public static int[][] generateScreen(BufferedImage frame) {
		int w = frame.getWidth();
		int h = frame.getHeight();
		
		int[][] screen = new int[h][w];
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				screen[y][x] = frame.getRGB(x, y);
			}
		}
		
		return screen;
	}

	public static int[][] generateScreen(AWTHoldFrameGrab frameGrab, int frameNumber) throws IOException, JCodecException {
		BufferedImage frame = frameGrab.getFrame(frameNumber);
		return generateScreen(frame);
	}
}
