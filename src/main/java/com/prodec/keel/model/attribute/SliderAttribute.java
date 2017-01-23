package com.prodec.keel.model.attribute;

public class SliderAttribute extends Attribute {

    int currentValue;
    int maxValue;
    int minValue;

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
}
