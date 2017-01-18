package com.prodec.keel.ui.modifier;

import br.com.etyllica.motion.feature.Component;
import com.prodec.keel.ui.ModifierView;

import java.util.List;

public class DummyModifierView extends ModifierView {

    public DummyModifierView(int x, int y) {
        super(x, y, VIEW_WIDTH, 80);
        title = "Dummy Modifier";
    }

    @Override
    public void modify(List<Component> results) {
        output.clear();
        output.addAll(results);
    }
}
