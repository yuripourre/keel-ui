package com.prodec.keel.model.attribute;

import com.prodec.keel.ui.PipelineComponent;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.feature.Component;

public class RegionAttribute extends Attribute {

    Component region;

    public RegionAttribute(String label) {
        super(label, AttributeType.REGION);
        this.region = new Component();
    }

    public Component getRegion() {
        return region;
    }

    public void setRegion(Component region) {
        this.region = region;
    }
    
    @Override
    public void draw(Graphics g) {
		int sepY = component.commonAttributesEnd();
		g.drawString(label, component.getX() + 14, sepY + PipelineComponent.ITEM_SPACING * (index + 1));

		String regionText = region.getX()+", "+region.getY()+", "+region.getW()+", "+region.getH();
		g.drawString(regionText, component.getX() + component.getW() / 2 - 30, sepY + PipelineComponent.ITEM_SPACING * (index + 1));
	}
}
