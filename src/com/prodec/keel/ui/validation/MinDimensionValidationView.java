package com.prodec.keel.ui.validation;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.filter.validation.MinDimensionValidation;

import com.prodec.keel.ui.ValidationView;

public class MinDimensionValidationView extends ValidationView {

	MinDimensionValidation minDimensionValidation;
	
	public MinDimensionValidationView(int x, int y) {
		super(x, y, VIEW_WIDTH, 70);
		this.title = "Min Dimension Validation";
		minDimensionValidation = new MinDimensionValidation(10);
		this.validation = minDimensionValidation;
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
				
		int sepY = commonAttributesEnd();
		
		//Draw Attributes
		g.drawString("Min Dimension", x + 14, sepY + ITEM_SPACING);
		g.drawString(Integer.toString(minDimensionValidation.getDimension()), x + 154, sepY + ITEM_SPACING);
	}
}
