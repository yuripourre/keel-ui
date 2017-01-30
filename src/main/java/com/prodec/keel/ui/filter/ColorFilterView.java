package com.prodec.keel.ui.filter;

import java.awt.Color;

import br.com.etyllica.motion.filter.ColorFilter;

import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.ColorPickerAttribute;
import com.prodec.keel.model.attribute.SliderAttribute;
import com.prodec.keel.ui.FilterView;

public class ColorFilterView extends FilterView {

    private ColorFilter colorFilter;

    public ColorFilterView() {
		this(0, 0);
	}
    
    public ColorFilterView(int x, int y) {
        super(x, y, VIEW_WIDTH, 130);
        this.className = ColorFilterView.class.getName();
        
        this.title = "Color Filter";
        this.colorFilter = new ColorFilter(w, h, Color.YELLOW, 100);
        this.filter = colorFilter;

        ColorPickerAttribute colorPickerAttribute = new ColorPickerAttribute("Color");
        colorPickerAttribute.setColor(colorFilter.getColor());

        addAttribute(colorPickerAttribute);
        addAttribute(new SliderAttribute("Tolerance", colorFilter.getTolerance(), 0, 255));
    }

    @Override
    public void onValueChange(int attributeId) {
        Attribute attribute = getAttribute(attributeId);
        if (attribute.getId() == 0) {
            colorFilter.setColor(colorFilter.getColor());
            resetFilter();
        }
    }
    
}
