package com.prodec.keel.ui.validation;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.filter.validation.MaxDimensionValidation;

import com.prodec.keel.ui.ValidationView;

public class MaxDimensionValidationView extends ValidationView {

	MaxDimensionValidation maxDimensionValidation;
	
	public MaxDimensionValidationView(int x, int y) {
		super(x, y, VIEW_WIDTH, 70);
		this.title = "Max Dimension Validation";
		maxDimensionValidation = new MaxDimensionValidation(80);
		this.validation = maxDimensionValidation;
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
				
		int sepY = commonAttributesEnd();
		
		//Draw Attributes
		g.drawString("Max Dimension", x + 14, sepY + ITEM_SPACING);
		g.drawString(Integer.toString(maxDimensionValidation.getDimension()), x + 154, sepY + ITEM_SPACING);
	}
}
