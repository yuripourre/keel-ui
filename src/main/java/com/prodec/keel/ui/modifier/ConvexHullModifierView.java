package com.prodec.keel.ui.modifier;

import java.util.List;

import br.com.etyllica.keel.feature.Component;
import br.com.etyllica.keel.feature.hull.HullComponent;
import br.com.etyllica.keel.modifier.hull.FastConvexHullModifier;
import br.com.etyllica.keel.modifier.hull.HullModifier;

import com.prodec.keel.model.DataType;
import com.prodec.keel.ui.ModifierView;

public class ConvexHullModifierView extends ModifierView<Component, HullComponent> {

	HullModifier<HullComponent> hullModifier;
	
	public ConvexHullModifierView() {
		this(0, 0);
	}
	
    public ConvexHullModifierView(int x, int y) {
        super(x, y, VIEW_WIDTH, 80);
        this.className = ConvexHullModifierView.class.getName();
        dataType = DataType.HULL;
        
        hullModifier = new FastConvexHullModifier();
        
        outputs.remove(0);
        outputs.add(0, "Hull Component");
        
        title = "Hull Modifier";
    }

    @Override
    public void modify(List<Component> results) {
        output.clear();
        
        for (Component component : results) {
        	output.add(hullModifier.modify(component));	
        }
    }
}
