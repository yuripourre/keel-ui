package com.prodec.keel.serialization;

import java.util.HashSet;
import java.util.Set;

import com.prodec.keel.ui.classifier.DimensionClassifierView;
import com.prodec.keel.ui.classifier.SquareClassifierView;
import com.prodec.keel.ui.drawer.CenterDrawerView;
import com.prodec.keel.ui.drawer.HullDrawerView;
import com.prodec.keel.ui.drawer.RectDrawerView;
import com.prodec.keel.ui.filter.ColorFilterView;
import com.prodec.keel.ui.modifier.ConvexHullModifierView;
import com.prodec.keel.ui.modifier.DummyModifierView;
import com.prodec.keel.ui.source.CameraSourceView;
import com.prodec.keel.ui.source.ImageSourceView;
import com.prodec.keel.ui.validation.MaxDimensionValidationView;
import com.prodec.keel.ui.validation.MinDimensionValidationView;

public class Register {

	public static Set<Class<?>> registeredTypes() {
		Set<Class<?>> registeredTypes = new HashSet<Class<?>>();
		registeredTypes.add(DimensionClassifierView.class);
		registeredTypes.add(SquareClassifierView.class);
		registeredTypes.add(CenterDrawerView.class);
		registeredTypes.add(RectDrawerView.class);
		registeredTypes.add(ColorFilterView.class);
		registeredTypes.add(HullDrawerView.class);
		registeredTypes.add(ConvexHullModifierView.class);
		registeredTypes.add(DummyModifierView.class);
		registeredTypes.add(CameraSourceView.class);
		registeredTypes.add(ImageSourceView.class);
		registeredTypes.add(MaxDimensionValidationView.class);
		registeredTypes.add(MinDimensionValidationView.class);
		
		return registeredTypes;
	}
	
}
