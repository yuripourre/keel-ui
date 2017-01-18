package com.prodec.keel.ui.drawer;

import java.awt.Color;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.ui.DrawerView;

public class RectDrawerView extends DrawerView {

	Color color;
	
	public RectDrawerView(int x, int y) {
		super(x, y, VIEW_WIDTH, 80);
		title = "Rectangular Drawer";
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
				
		//Draw Attributes
		drawColorPickerAttribute(g, "Color", 0, color);
	}
	
	public void drawResults(Graphics g) {
		for (Component component : results) {
			g.setColor(color);
			g.drawRect(component.getRectangle());
		}
	}
	
}
