package com.prodec.keel.model.attribute;

import com.prodec.keel.ui.PipelineComponent;

import br.com.etyllica.core.graphics.Graphics;

public class SliderAttribute extends Attribute {

    int currentValue;
    int maxValue;
    int minValue;

    public SliderAttribute(String label) {
        super(label, AttributeType.SLIDER);
    }
    
    public SliderAttribute(String label, int currentValue, int minValue, int maxValue) {
        super(label, AttributeType.SLIDER);
        this.currentValue = currentValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    @Override
	public void draw(Graphics g) {
		int sepY = component.commonAttributesEnd();
		g.drawString(label, component.getX() + 14, sepY + PipelineComponent.ITEM_SPACING * (index + 1));
		g.drawString(Integer.toString(currentValue), component.getX() + 154, sepY + PipelineComponent.ITEM_SPACING * (index + 1));
	}
}
