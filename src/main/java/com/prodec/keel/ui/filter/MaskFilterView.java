package com.prodec.keel.ui.filter;

import java.awt.image.BufferedImage;

import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.filter.MaskFilter;

import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.PathAttribute;
import com.prodec.keel.model.attribute.SliderAttribute;
import com.prodec.keel.ui.FilterView;

public class MaskFilterView extends FilterView {

    private MaskFilter subtractiveFilter;

    public MaskFilterView() {
		this(0, 0);
	}
    
    public MaskFilterView(int x, int y) {
        super(x, y, VIEW_WIDTH, 130);
        this.className = MaskFilterView.class.getName();
        
        this.title = "Mask Filter";
        this.subtractiveFilter = new MaskFilter(w, h);
        this.filter = subtractiveFilter;

        addAttribute(new PathAttribute("Mask"));
        addAttribute(new SliderAttribute("Tolerance", subtractiveFilter.getTolerance(), 0, 255));
    }

    @Override
    public void onValueChange(int attributeId) {
        Attribute attribute = getAttribute(attributeId);
        if (attribute.getId() == 0) {
        	PathAttribute pathAttribute = (PathAttribute) attribute;
        	BufferedImage image = ImageLoader.getInstance().getImage(pathAttribute.getPath());
        	subtractiveFilter.setMask(new BufferedImageSource(image));
        	resetFilter();
        } else if (attribute.getId() == 1) {
        	SliderAttribute slider = (SliderAttribute) attribute;
        	subtractiveFilter.setTolerance(slider.getCurrentValue());
        	resetFilter();
        }
    }
    
}
