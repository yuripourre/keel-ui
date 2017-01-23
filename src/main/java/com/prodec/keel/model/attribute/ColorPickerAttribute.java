package com.prodec.keel.model.attribute;

import java.awt.*;

public class ColorPickerAttribute extends Attribute {

    int color;

    public ColorPickerAttribute(String label) {
        super(label, AttributeType.COLOR_PICKER);
        this.color = Color.BLACK.getRGB();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
