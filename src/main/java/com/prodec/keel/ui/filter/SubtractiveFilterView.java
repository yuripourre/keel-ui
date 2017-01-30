package com.prodec.keel.ui.filter;

import java.awt.image.BufferedImage;

import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.filter.SubtractiveFilter;

import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.PathAttribute;
import com.prodec.keel.ui.FilterView;

public class SubtractiveFilterView extends FilterView {

    private SubtractiveFilter subtractiveFilter;

    public SubtractiveFilterView() {
		this(0, 0);
	}
    
    public SubtractiveFilterView(int x, int y) {
        super(x, y, VIEW_WIDTH, 130);
        this.className = SubtractiveFilterView.class.getName();
        
        this.title = "Subtractive Filter";
        this.subtractiveFilter = new SubtractiveFilter(w, h);
        this.filter = subtractiveFilter;

        addAttribute(new PathAttribute("Mask"));
    }

    @Override
    public void onValueChange(int attributeId) {
        Attribute attribute = getAttribute(attributeId);
        if (attribute.getId() == 0) {
        	PathAttribute pathAttribute = (PathAttribute) attribute;
        	BufferedImage image = ImageLoader.getInstance().getImage(pathAttribute.getPath());
        	subtractiveFilter.setMask(new BufferedImageSource(image));
        }
    }
    
}
