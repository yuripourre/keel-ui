package com.prodec.keel.ui.filter;

import java.awt.Color;

import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.filter.ColorFilter;

import com.prodec.keel.ui.FilterView;

public class ColorFilterView  extends FilterView {
	
	ColorFilter colorFilter;
	
	public ColorFilterView(int x, int y, ColorFilter filter) {
		super(x, y, VIEW_WIDTH, 130);
		this.title = "Color Filter";
		this.filter = filter;
		this.colorFilter = filter;
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
				
		int sepY = commonAttributesEnd();
		
		//Draw Attributes
		g.drawString("Color", x + 14, sepY + ITEM_SPACING);
		g.setColor(colorFilter.getColor());
		g.fillRect(x + w - 14, sepY + 6, 12, 12);
		
		g.setColor(Color.BLACK);
		g.drawString("Tolerance", x + 14, sepY + ITEM_SPACING * 2);
		
		g.drawString(Integer.toString(colorFilter.getTolerance()), x + 154, sepY + ITEM_SPACING * 2);
	}

	@Override
	public void updateMouse(PointerEvent event) {
		super.updateMouse(event);
		
		// TODO Auto-generated method stub
	}
}
