package com.prodec.keel.application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.Mode;
import com.prodec.keel.model.PipelineLink;
import com.prodec.keel.ui.ClassifierView;
import com.prodec.keel.ui.DrawerView;
import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.PipelineComponentItem;
import com.prodec.keel.ui.PipelineLinkView;
import com.prodec.keel.ui.classifier.SquareClassifierView;
import com.prodec.keel.ui.drawer.RectDrawerView;
import com.prodec.keel.ui.filter.ColorFilterView;
import com.prodec.keel.ui.validation.MaxDimensionValidationView;
import com.prodec.keel.ui.validation.MinDimensionValidationView;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.feature.Component;
import br.com.etyllica.motion.filter.ColorFilter;
import br.com.etyllica.theme.ThemeManager;

public class FilterViewApplication extends Application {

	private static Mode mode = Mode.NORMAL;
	
	private Component screen;
	private BufferedImage source;
	
	private ColorFilter colorFiler;
	private ColorFilterView filterView;

	PipelineLink currentLink = new PipelineLink();

	List<PipelineLinkView> links = new ArrayList<PipelineLinkView>();
	List<PipelineComponent> components = new ArrayList<PipelineComponent>();
	
	List<Component> results = new ArrayList<Component>();

	public FilterViewApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		
		source = ImageLoader.getInstance().getImage("test1.jpg");
		screen = new Component(0, 0, source.getWidth(), source.getHeight());
		
		colorFiler = new ColorFilter(w, h, Color.YELLOW, 100);
		filterView = new ColorFilterView(20, 310, colorFiler);
		
		RectDrawerView rectangleView = new RectDrawerView(20, 450);

		MaxDimensionValidationView maxDimensionView = new MaxDimensionValidationView(275, 310);
		MinDimensionValidationView minDimensionView = new MinDimensionValidationView(275, 410);
		
		SquareClassifierView classifierView = new SquareClassifierView(20, 610);
		

		components.add(filterView);
		components.add(maxDimensionView);
		components.add(minDimensionView);
		components.add(rectangleView);
		components.add(classifierView);
		
		//Force First Filter
		resetFilter();
		
		setupUI();
	}
	
	private void setupUI() {
		//ThemeManager.getInstance().getTheme().setTextColor(Color.WHITE);
		ThemeManager.getInstance().getTheme().setTextColor(Color.BLACK);
	}

	@Override
	public void draw(Graphics g) {
		
		g.drawImage(source, 0, 0);
		
		g.setColor(Color.BLACK);
		for (PipelineLinkView link: links) {
			link.drawLine(g);
		}

		for (PipelineComponent component : components) {
			if (ComponentType.DRAWER == component.getType()) {
				((DrawerView)component).drawResults(g);
			}
		}
				
		for (PipelineComponent component : components) {
			component.draw(g);
		}
		
		for (PipelineLinkView link: links) {
			link.drawJoints(g);
		}
	}

	@Override
	public void updateMouse(PointerEvent event) {
		for (PipelineComponent component : components) {
			component.updateMouse(event);
			
			if (component.isMoving()) {
				mode = Mode.SELECTION;
				return;
			} else if(component.isRemoveLink()) {
				removeLink(component);
				component.linkRemoved();
				return;
			}
			
			if (component.getSelectedItem() != PipelineComponent.INVALID_ITEM) {
				if (currentLink.getFromItem() == PipelineComponent.INVALID_ITEM) {
					currentLink.setFrom(component);
					mode = Mode.SELECTION;
				} else if (component != currentLink.getFrom()) {
					currentLink.setTo(component);
				}
			}
		}

		if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
			linkComponents();
			resetLink();
		}
	}
	
	private void removeLink(PipelineComponent component) {
		PipelineComponentItem item = component.getMouseOnItem();
		
		for (int i = links.size()-1; i >= 0 ; i--) {
			PipelineLinkView view = links.get(i);
			PipelineLink link = view.getLink();
			if (link.getFrom() == component && link.getFromItem().equals(item) ||
				link.getTo() == component && link.getToItem().equals(item)) {
				link.unlink();
				links.remove(i);
				
				resetFilter();
				break;
			}
		}		
	}

	private void resetLink() {
		currentLink.reset();
		mode = Mode.NORMAL;
	}

	public static Mode getMode() {
		return mode;
	}
	
	private void linkComponents() {
		if (currentLink.isValid()) {
			currentLink.link();
			links.add(new PipelineLinkView(currentLink));
			currentLink = new PipelineLink();
			
			resetFilter();
		}
	}

	private void resetFilter() {
		System.out.println("Reset Filter");
		results = colorFiler.filter(new BufferedImageSource(source), screen);
		System.out.println(results.size());
		
		//TODO Get filterView by source
		DrawerView drawer = filterView.getDrawer();
		if (drawer != null) {
			drawer.setResults(results);
		}
		ClassifierView classifier = filterView.getClassifier();
		if (classifier != null) {
			classifier.setResults(results);
			classifier.classify(results);
		}
	}
	
	@Override
	public void updateKeyboard(KeyEvent event) {
		for (PipelineComponent component : components) {
			component.updateKeyboard(event);
		}
	}

}
