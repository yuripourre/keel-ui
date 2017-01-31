package com.prodec.keel.ui.classifier;

import java.util.List;

import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.ui.ClassifierView;

public class DimensionClassifierView extends ClassifierView<Component> {

	public static final String CLASS_HORIZONTAL = "Horizontal";
	public static final String CLASS_SQUARED = "Squared";
	public static final String CLASS_VERTICAL = "Vertical";
	
	public DimensionClassifierView() {
		this(0, 0);
	}
	
	public DimensionClassifierView(int x, int y) {
		super(x, y, VIEW_WIDTH, 110);
		this.className = DimensionClassifierView.class.getName();
		
		title = "Dimension Classifier";

		addCategory(CLASS_HORIZONTAL);
		addCategory(CLASS_SQUARED);
		addCategory(CLASS_VERTICAL);
	}

	@Override
	public void classify(List<Component> results) {
		for (Component component : results) {
			String category = CLASS_SQUARED;
			if (component.getW() > component.getH()) {
				category = CLASS_HORIZONTAL;
			} else if (component.getW() < component.getH()) {
				category = CLASS_VERTICAL;
			}

			List<Component> components = classifications.get(category);
			components.add(component);
		}
	}

}
