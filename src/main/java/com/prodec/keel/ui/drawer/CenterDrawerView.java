package com.prodec.keel.ui.drawer;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.feature.Component;
import com.prodec.keel.ui.DrawerView;

import java.awt.*;

public class CenterDrawerView extends DrawerView {

	Color color = Color.BLACK;
	int radius = 5;

	public CenterDrawerView(int x, int y) {
		super(x, y, VIEW_WIDTH, 90);
		title = "Center Drawer";
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
				
		//Draw Attributes
		drawColorPickerAttribute(g, "Color", 0, color);
		drawSliderAttribute(g, "Radius", 1, radius);
	}
	
	public void drawResults(Graphics g) {
		g.setColor(color);
		for (Component component : results) {
			int cx = component.getX() + component.getW()/2;
			int cy = component.getY() + component.getH()/2;

			g.fillCircle(cx, cy, radius);
		}
	}
	
}
