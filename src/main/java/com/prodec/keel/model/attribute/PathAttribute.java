package com.prodec.keel.model.attribute;

import com.prodec.keel.ui.PipelineComponent;

import br.com.etyllica.core.graphics.Graphics;

public class PathAttribute extends Attribute {

    String path;

    public PathAttribute(String label) {
        super(label, AttributeType.PATH);
        this.path = "";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public void draw(Graphics g) {
		int sepY = component.commonAttributesEnd();
		g.drawString(label, component.getX() + 14, sepY + PipelineComponent.ITEM_SPACING * (index + 1));
		g.drawString(path, component.getX() + 154, sepY + PipelineComponent.ITEM_SPACING * (index + 1));
	}
}
