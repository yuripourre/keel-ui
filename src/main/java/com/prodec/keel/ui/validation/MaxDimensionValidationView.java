package com.prodec.keel.ui.validation;

import br.com.etyllica.motion.filter.validation.MaxDimensionValidation;

import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.SliderAttribute;
import com.prodec.keel.ui.ValidationView;

public class MaxDimensionValidationView extends ValidationView {

    SliderAttribute sliderAttribute;
    MaxDimensionValidation maxDimensionValidation;

    public MaxDimensionValidationView() {
		this(0, 0);
	}
    
    public MaxDimensionValidationView(int x, int y) {
        super(x, y, VIEW_WIDTH, 70);
        this.className = MaxDimensionValidationView.class.getName();
        
        this.title = "Max Dimension Validation";
        maxDimensionValidation = new MaxDimensionValidation(80);
        this.validation = maxDimensionValidation;

        sliderAttribute = new SliderAttribute("Max Dimension", 80, 1, 300);
        addAttribute(sliderAttribute);
    }

    public void onValueChange(int attributeId) {
        Attribute attribute = getAttribute(attributeId);
        if (attribute.getId() == sliderAttribute.getId()) {
            maxDimensionValidation.setDimension(sliderAttribute.getCurrentValue());
            //TODO Reset Filter
        }
    }
}
