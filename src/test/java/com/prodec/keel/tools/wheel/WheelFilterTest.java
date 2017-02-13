package com.prodec.keel.tools.wheel;

import java.awt.Point;
import java.awt.image.BufferedImage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.feature.Component;
import br.com.etyllica.util.PathHelper;

public class WheelFilterTest {

	private BufferedImageSource source;
	private Component screen;
	
	WheelFilter filter;
	
	@Before
	public void setUp() {
		Point2D startPoint = new Point2D(198, 262);
		Point2D endPoint = new Point2D(298, 108.0);
		
		filter = new WheelFilter(startPoint, endPoint);
		source = new BufferedImageSource();
	}
	
	private void loadImage(String path) {
		String fullPath = PathHelper.currentDirectory()+"../assets/images/"+path;
		BufferedImage image = ImageLoader.getInstance().getImage(fullPath, true);
		source.setImage(image);
		screen = new Component(0, 0, source.getWidth(), source.getHeight());
	}
	
	@Test
	public void testCountWheels() {
		loadImage("cars/1485968917194.jpg");
		Assert.assertEquals(6, countIntervals(filter.filter(source, screen)));
		
		loadImage("cars/1485969694374.jpg");
		Assert.assertEquals(6, countIntervals(filter.filter(source, screen)));
		
		loadImage("cars/1485969859969.jpg");
		Assert.assertEquals(3, countIntervals(filter.filter(source, screen)));
	}
	
	private int countIntervals(boolean[] spectrogram) {
		int count = 0;
		boolean valid = false;
		for (int i = 0; i < spectrogram.length; i++) {
			if (spectrogram[i]) {
				
				if (!valid) {
					count++;
					valid = true;	
				}
			} else {
				valid = false;
			}
		}
		
		return count;
	}
	
	
}
