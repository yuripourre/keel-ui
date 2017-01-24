package com.prodec.keel.ui.drawer;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.model.attribute.ColorPickerAttribute;
import com.prodec.keel.ui.DrawerView;

public class RectDrawerView extends DrawerView {

    private ColorPickerAttribute colorPickerAttribute;

    public RectDrawerView() {
		this(0, 0);
	}
    
    public RectDrawerView(int x, int y) {
        super(x, y, VIEW_WIDTH, 80);
        this.className = RectDrawerView.class.getName();
        
        title = "Rectangular Drawer";

        colorPickerAttribute = new ColorPickerAttribute("Color");
        addAttribute(colorPickerAttribute);
    }

    public void drawResults(Graphics g) {
        g.setColor(colorPickerAttribute.getColor());
        for (Component component : results) {
            g.drawRect(component.getRectangle());
        }
    }

}
