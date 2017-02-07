package com.prodec.keel.ui.modifier;

import java.util.List;

import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.model.DataType;
import com.prodec.keel.ui.ModifierView;

public class MergerModifierView extends ModifierView<Component, Component> {

	public MergerModifierView() {
		this(0, 0);
	}
	
    public MergerModifierView(int x, int y) {
        super(x, y, VIEW_WIDTH, 80);
        this.className = MergerModifierView.class.getName();
        dataType = DataType.FEATURE;
        
        outputs.remove(0);
        outputs.add(0, "Merged Component");
        
        title = "Merger Modifier";
    }

    @Override
    public void modify(List<Component> results) {
        output.clear();
        
        Component component = new Component();
        
        for (Component result : results) {
        	for (Point2D point : result.getPoints()) {
        		component.add(point);	
        	}
        }
        
        output.add(component);
    }
}
