package com.prodec.keel.ui.filter;

import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.filter.ColorFilter;
import com.prodec.keel.ui.FilterView;

import java.awt.*;

public class ColorFilterView extends FilterView {

    ColorFilter colorFilter;

    public ColorFilterView(int x, int y) {
        super(x, y, VIEW_WIDTH, 130);
        this.title = "Color Filter";
        this.colorFilter = new ColorFilter(w, h, Color.YELLOW, 100);
        this.filter = colorFilter;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        //Draw Attributes
        drawColorPickerAttribute(g, "Color", 0, colorFilter.getColor());
        drawSliderAttribute(g, "Tolerance", 1, colorFilter.getTolerance());
    }

    @Override
    public void updateMouse(PointerEvent event) {
        super.updateMouse(event);

        // TODO Auto-generated method stub
    }

}
