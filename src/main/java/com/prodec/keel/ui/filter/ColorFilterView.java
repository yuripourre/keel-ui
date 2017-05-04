package com.prodec.keel.ui.filter;

import java.awt.Color;

import br.com.etyllica.keel.filter.ColorFilter;

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
        this.colorFilter = new ColorFilter(w, h, Color.BLACK, 100);
        this.filter = colorFilter;

        ColorPickerAttribute colorPickerAttribute = new ColorPickerAttribute("Color");
        colorPickerAttribute.setColor(colorFilter.getColor());

        addAttribute(colorPickerAttribute);
        addAttribute(new SliderAttribute("Tolerance", colorFilter.getTolerance(), 1, 255));
    }

    @Override
    public void onValueChange(int attributeId) {
        Attribute attribute = getAttribute(attributeId);
        if (attribute.getId() == 0) {
        	ColorPickerAttribute colorPicker = (ColorPickerAttribute) attribute;
            colorFilter.setColor(colorPicker.getColor());
            resetFilter();
        } else if (attribute.getId() == 1) {
        	SliderAttribute slider = (SliderAttribute) attribute;
            colorFilter.setTolerance(slider.getCurrentValue());
            resetFilter();
        }
    }
    
}
