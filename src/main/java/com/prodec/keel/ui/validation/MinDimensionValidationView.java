package com.prodec.keel.ui.validation;

import br.com.etyllica.motion.filter.validation.MinDimensionValidation;
import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.SliderAttribute;
import com.prodec.keel.ui.ValidationView;

public class MinDimensionValidationView extends ValidationView {

    SliderAttribute sliderAttribute;
    MinDimensionValidation minDimensionValidation;

    public MinDimensionValidationView(int x, int y) {
        super(x, y, VIEW_WIDTH, 70);
        this.title = "Min Dimension Validation";
        minDimensionValidation = new MinDimensionValidation(10);
        this.validation = minDimensionValidation;

        sliderAttribute = new SliderAttribute("Min Dimension", 80, 1, 300);
        addAttribute(sliderAttribute);
    }

    public void onValueChange(int attributeId) {
        Attribute attribute = getAttribute(attributeId);
        if (attribute.getId() == sliderAttribute.getId()) {
            minDimensionValidation.setDimension(sliderAttribute.getCurrentValue());
            //TODO Reset Filter
        }
    }
}
