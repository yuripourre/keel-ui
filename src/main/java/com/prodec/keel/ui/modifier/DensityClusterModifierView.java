package com.prodec.keel.ui.modifier;

import java.util.List;

import br.com.etyllica.keel.classifier.cluster.Cluster;
import br.com.etyllica.keel.classifier.cluster.DBScan;
import br.com.etyllica.keel.feature.Component;

import com.prodec.keel.model.DataType;
import com.prodec.keel.model.attribute.SliderAttribute;
import com.prodec.keel.ui.ModifierView;

public class DensityClusterModifierView extends ModifierView<Component, Cluster> {

	private DBScan clusterer;
	
	public DensityClusterModifierView() {
		this(0, 0);
	}
	
    public DensityClusterModifierView(int x, int y) {
        super(x, y, VIEW_WIDTH, 114);
        this.className = DensityClusterModifierView.class.getName();
        dataType = DataType.CLUSTER;
        title = "Cluster Modifier";
        
        int maxDistance = 20;
        int minPoints = 5;
        clusterer = new DBScan(maxDistance, minPoints);
                
        outputs.remove(0);
        outputs.add(0, "List of Clusters");
        
        addAttribute(new SliderAttribute("Max Distance", maxDistance, 1, 999));
        addAttribute(new SliderAttribute("Min Points", minPoints, 1, 999));
    }

    @Override
    public void modify(List<Component> results) {
    	for (Component component : results) {
    		List<Cluster> clusters = clusterer.cluster(component.getPoints());        
            for (Cluster cluster : clusters) {
            	output.add(cluster);
            }	
    	}
    }
}
