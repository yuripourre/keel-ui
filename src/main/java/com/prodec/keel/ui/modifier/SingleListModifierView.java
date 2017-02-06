package com.prodec.keel.ui.modifier;

import java.util.List;

import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.model.DataType;
import com.prodec.keel.ui.ModifierView;

public class SingleListModifierView extends ModifierView<Component, Point2D> {

	public SingleListModifierView() {
		this(0, 0);
	}
	
    public SingleListModifierView(int x, int y) {
        super(x, y, VIEW_WIDTH, 80);
        this.className = SingleListModifierView.class.getName();
        dataType = DataType.LIST;
        
        outputs.remove(0);
        outputs.add(0, "List of Points");
        
        title = "Single List Modifier";
    }

    @Override
    public void modify(List<Component> results) {
        output.clear();
        
        for (Component component : results) {
        	output.addAll(component.getPoints());	
        }
    }
}
