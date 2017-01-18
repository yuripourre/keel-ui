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
		
		outItems.add(CLASS_SQUARE);
		outItems.add(CLASS_NON_SQUARE);
	}

	@Override
	public void classify(List<Component> results) {
		for (Component component : results) {
			if (component.getW() == component.getH()) {
				classifications.put(component, CLASS_SQUARE);
			} else {
				classifications.put(component, CLASS_NON_SQUARE);
			}
		}
	}

}
