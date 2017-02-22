package com.prodec.keel.tools.wheel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.feature.Component;
import br.com.etyllica.motion.feature.Interval;
import br.com.etyllica.util.PathHelper;

public class WheelFilterTest {

	private BufferedImageSource source;
	private Component screen;
	
	WheelFilter filter;
	
	@Before
	public void setUp() {
		Point2D startPoint = new Point2D(200, 284);
		Point2D endPoint = new Point2D(300, 130);
		
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
	
	private int countIntervals(int[] spectrogram) {
		int count = 0;
		boolean valid = false;
		for (int i = 0; i < spectrogram.length; i++) {
			if (spectrogram[i] > 0) {
				
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
	
	@Test
	public void testGroupIntervals() {
		List<Interval> intervals = new ArrayList<Interval>();
		
		intervals.add(new Interval(0, 2));
		intervals.add(new Interval(4, 4));
		intervals.add(new Interval(6, 9));

		// Intervals
		// 012-4-6789
		
		// After Group
		// 012-456789
		
		List<Interval> groups = groupIntervals(intervals, 4);
		Assert.assertEquals(2, groups.size());
	}

	private List<Interval> groupIntervals(List<Interval> intervals, int maxDist) {
		List<Interval> sorted = new ArrayList<Interval>(intervals);
		Collections.sort(sorted, new IntervalComparator());
				
		int dist;
		for (int i = sorted.size() - 1; i > 0 ; i--) {
			for (int j = sorted.size() - 1; j > 0; j--) {
				if (j == i) {
					continue;
				}
				Interval interval = sorted.get(i);
				Interval otherInterval = sorted.get(j);
			
				dist = maxDist;
				if (interval.getStart() > otherInterval.getEnd()) {
					dist = interval.getStart() - otherInterval.getEnd();
				} else if (interval.getEnd() < otherInterval.getStart()) {
					dist = otherInterval.getStart() - interval.getEnd();
				}
				
				if (dist < maxDist) {
					interval.merge(otherInterval);
				}
			}
		}
		
		return intervals;
	}
	
	class IntervalComparator implements Comparator<Interval> {
		@Override
		public int compare(Interval interval1, Interval interval2) {
			return interval2.length() - interval1.length();		
		}
	}
}
