package com.prodec.keel.ui.drawer;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.model.attribute.ColorPickerAttribute;
import com.prodec.keel.model.attribute.SliderAttribute;
import com.prodec.keel.ui.DrawerView;

public class CenterDrawerView extends DrawerView<Component> {

    private ColorPickerAttribute colorPickerAttribute;
    private SliderAttribute radiusAttribute;

    public CenterDrawerView() {
		this(0, 0);
	}
    
    public CenterDrawerView(int x, int y) {
        super(x, y, VIEW_WIDTH, 90);
        this.className = CenterDrawerView.class.getName();
        
        title = "Center Drawer";

        colorPickerAttribute = new ColorPickerAttribute("Color");

        radiusAttribute = new SliderAttribute("Radius", 1, 1, 100);

        addAttribute(colorPickerAttribute);
        addAttribute(radiusAttribute);
    }

    public void drawResults(Graphics g) {
        g.setColor(colorPickerAttribute.getColor());
        for (Component component : results) {
            int cx = component.getX() + component.getW() / 2;
            int cy = component.getY() + component.getH() / 2;

            g.fillCircle(cx + drawX, cy + drawY, radiusAttribute.getCurrentValue());
        }
    }

}
