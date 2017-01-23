package com.prodec.keel.model.attribute;

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
}
