package com.prodec.keel.application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.gui.listener.ValueListener;
import br.com.etyllica.gui.spinner.IntegerSpinner;
import br.com.etyllica.keel.awt.source.BufferedImageSource;
import br.com.etyllica.keel.classifier.cluster.Cluster;
import br.com.etyllica.keel.classifier.cluster.DBScan;
import br.com.etyllica.keel.core.source.ImageSource;
import br.com.etyllica.keel.feature.Component;
import br.com.etyllica.keel.modifier.edge.CannyEdgeModifier;
import br.com.etyllica.keel.modifier.edge.EdgeModifier;
import br.com.etyllica.loader.image.ImageLoader;

public class CannyEdgeApplication extends Application {

	private BufferedImage image;
	private Component screen;
	private ImageSource source;
	private EdgeModifier modifier;

	private DBScan clusterer;
	
	private List<Point2D> edges;
	
	private List<Cluster> clusters;

	private boolean drawImage = false;
	
	private IntegerSpinner radiusSpinner;
	private IntegerSpinner minPointsSpinner;
	
	public CannyEdgeApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		//image = ImageLoader.getInstance().getImage("cars/1485968690020.jpg");
		
		image = ImageLoader.getInstance().getImage("cars/1485968917194.jpg");
		//image = ImageLoader.getInstance().getImage("cars/1485968917194_edit.jpg");
		
		//image = ImageLoader.getInstance().getImage("cars/1485969625131.jpg");

		source = new BufferedImageSource(image);
		screen = new Component(0, 0, image.getWidth(), image.getHeight());

		//clusterer = new DBScan(7, 26);
		clusterer = new DBScan(7, 21);
		
		CannyEdgeModifier modifier = new CannyEdgeModifier();
		/*modifier.setLowThreshold(1);
		modifier.setHighThreshold(5);
		modifier.setGaussianKernelWidth(5);*/
		//modifier.setGaussianKernelWidth(16);
		//modifier.setLowThreshold(8);
		//modifier.setHighThreshold(10);
		modifier.setContrastNormalized(true);

		this.modifier = modifier;

		applyModifier();
		
		radiusSpinner = new IntegerSpinner(510, 50, 160, 60);
		radiusSpinner.setValue((int)clusterer.getEps());
		radiusSpinner.setListener(new ValueListener<Integer>() {
			@Override
			public void onChange(Integer value) {
				clusterer.setEps(value);
				applyModifier();
			}
		});
		minPointsSpinner = new IntegerSpinner(510, 130, 160, 60);
		minPointsSpinner.setValue((int)clusterer.getMinPoints());
		minPointsSpinner.setListener(new ValueListener<Integer>() {
			@Override
			public void onChange(Integer value) {
				clusterer.setMinPoints(value);
				applyModifier();
			}
		});
		
		addView(radiusSpinner);
		addView(minPointsSpinner);
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		if (event.isKeyDown(KeyEvent.VK_SPACE)) {
			drawImage = !drawImage;
		}
	}

	private void applyModifier() {
		edges = modifier.modify(source, screen);
		clusters = clusterer.cluster(edges);
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(this);

		if (drawImage) {
			g.drawImage(image, 0, 0);
		}

		g.setColor(Color.WHITE);
		for (Point2D point: edges) {
			g.fillRect((int)point.getX(), (int)point.getY(), 1, 1);	
		}
		
		g.setColor(Color.RED);
		for (Cluster cluster: clusters) {
			g.drawCircle(cluster.centroid, 12);
			for (Point2D point: cluster.getPoints()) {
				g.fillRect((int)point.getX(), (int)point.getY(), 1, 1);	
			}
		}
	}

}
