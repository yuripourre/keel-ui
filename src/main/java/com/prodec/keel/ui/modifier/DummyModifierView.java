package com.prodec.keel.ui.modifier;

import java.util.List;

import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.ui.ModifierView;

public class DummyModifierView extends ModifierView {

	public DummyModifierView() {
		this(0, 0);
	}
	
    public DummyModifierView(int x, int y) {
        super(x, y, VIEW_WIDTH, 80);
        this.className = DummyModifierView.class.getName();
        
        title = "Dummy Modifier";
    }

    @Override
    public void modify(List<Component> results) {
        output.clear();
        output.addAll(results);
    }
}
