package com.prodec.keel.ui.filter;

import java.awt.Color;

import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.motion.filter.ColorFilter;

import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.ColorPickerAttribute;
import com.prodec.keel.model.attribute.SliderAttribute;
import com.prodec.keel.ui.FilterView;

public class ColorFilterView extends FilterView {

    ColorFilter colorFilter;

    public ColorFilterView(int x, int y) {
        super(x, y, VIEW_WIDTH, 130);
        this.title = "Color Filter";
        this.colorFilter = new ColorFilter(w, h, Color.YELLOW, 100);
        this.filter = colorFilter;

        ColorPickerAttribute colorPickerAttribute = new ColorPickerAttribute("Color");
        colorPickerAttribute.setColor(colorFilter.getColor());

        addAttribute(colorPickerAttribute);
        addAttribute(new SliderAttribute("Tolerance", 0, 100, 255));
    }

    @Override
    public void updateMouse(PointerEvent event) {
        super.updateMouse(event);

        // TODO Auto-generated method stub
    }

    @Override
    public void onValueChange(int attributeId) {
        Attribute attribute = getAttribute(attributeId);
        if (attribute.getId() == 0) {
            colorFilter.setColor(colorFilter.getColor());
        }
    }

}
