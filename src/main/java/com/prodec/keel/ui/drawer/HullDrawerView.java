package com.prodec.keel.ui.drawer;

import java.util.List;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.core.linear.Point2D;
import br.com.etyllica.motion.feature.hull.HullComponent;

import com.prodec.keel.model.DataType;
import com.prodec.keel.model.attribute.ColorPickerAttribute;
import com.prodec.keel.model.attribute.SliderAttribute;
import com.prodec.keel.ui.DrawerView;

public class HullDrawerView extends DrawerView<HullComponent> {

	private SliderAttribute radiusAttribute;
    private ColorPickerAttribute colorPickerAttribute;

    public HullDrawerView() {
		this(0, 0);
	}
    
    public HullDrawerView(int x, int y) {
        super(x, y, VIEW_WIDTH, 90);
        this.className = HullDrawerView.class.getName();
        dataType = DataType.HULL;
        
        title = "Hull Drawer";

        colorPickerAttribute = new ColorPickerAttribute("Color");
        addAttribute(colorPickerAttribute);
        
        radiusAttribute = new SliderAttribute("Radius", 4, 1, 20);
        addAttribute(radiusAttribute);
    }
    
    public void drawResults(Graphics g) {
        g.setColor(colorPickerAttribute.getColor());
        for (HullComponent component : results) {
        	List<Point2D> points = component.getPoints();
        	for (int i = 0; i < points.size(); i++) {
        		Point2D point = points.get(i);
        		int next = i+1;
        		next %= points.size();
        		Point2D nextPoint = points.get(next); 
        		
        		g.drawLine((int)point.getX() + drawX,(int) point.getY() + drawY,(int) nextPoint.getX() + drawX,(int) nextPoint.getY() + drawY);
        		g.fillCircle(point.getX() + drawX, point.getY() + drawY, radiusAttribute.getCurrentValue());
        	}
        }
    }

}
