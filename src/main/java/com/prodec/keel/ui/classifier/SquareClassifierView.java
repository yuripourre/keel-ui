package com.prodec.keel.ui.classifier;

import java.util.List;

import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.ui.ClassifierView;

public class SquareClassifierView extends ClassifierView {

	public static final String CLASS_SQUARE = "Square";
	public static final String CLASS_NON_SQUARE = "NonSquare";
	
	public SquareClassifierView(int x, int y) {
		super(x, y, VIEW_WIDTH, 80);
		
		title = "Square Classifier";

		addCategory(CLASS_SQUARE);
		addCategory(CLASS_NON_SQUARE);
	}

	@Override
	public void classify(List<Component> results) {
		for (Component component : results) {
			String category = CLASS_NON_SQUARE;
			if (component.getW() == component.getH()) {
				category = CLASS_SQUARE;
			}

			List<Component> components = classifications.get(category);
			components.add(component);
		}
	}

}
