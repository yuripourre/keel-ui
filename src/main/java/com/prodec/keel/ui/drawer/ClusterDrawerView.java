package com.prodec.keel.ui.drawer;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.classifier.cluster.Cluster;

import com.prodec.keel.model.DataType;
import com.prodec.keel.model.attribute.ColorPickerAttribute;
import com.prodec.keel.model.attribute.SliderAttribute;
import com.prodec.keel.ui.DrawerView;

public class ClusterDrawerView extends DrawerView<Cluster> {

	private SliderAttribute radiusAttribute;
    private ColorPickerAttribute colorPickerAttribute;

    public ClusterDrawerView() {
		this(0, 0);
	}
    
    public ClusterDrawerView(int x, int y) {
        super(x, y, VIEW_WIDTH, 90);
        this.className = ClusterDrawerView.class.getName();
        dataType = DataType.CLUSTER;
        
        title = "Cluster Drawer";

        colorPickerAttribute = new ColorPickerAttribute("Color");
        addAttribute(colorPickerAttribute);
        
        radiusAttribute = new SliderAttribute("Radius", 4, 1, 20);
        addAttribute(radiusAttribute);
    }
    
    public void drawResults(Graphics g) {
        g.setColor(colorPickerAttribute.getColor());
        for (Cluster cluster : results) {
        	g.drawCircle(cluster.centroid, radiusAttribute.getCurrentValue());
        	
        	//Draw Points
        	/*List<Point2D> points = component.getPoints();
        	for (int i = 0; i < points.size(); i++) {
        		Point2D point = points.get(i);
        		int next = i+1;
        		next %= points.size();
        		Point2D nextPoint = points.get(next); 
        		
        		g.drawLine(point, nextPoint);
        		g.fillCircle(point, radiusAttribute.getCurrentValue());
        	}*/
        }
    }

}
