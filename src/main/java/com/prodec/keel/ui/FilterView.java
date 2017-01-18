package com.prodec.keel.ui;

import java.awt.Color;

import br.com.etyllica.motion.filter.TrackingFilter;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.LinkPosition;

public abstract class FilterView extends PipelineComponent {
 	
	private DrawerView drawerView;
	private ValidationView validationView;
	protected TrackingFilter filter;
	
	public FilterView(int x, int y, int w, int h) {
		super(x, y, w, h);
		type = ComponentType.FILTER;
		inItems.add("Feature");
		inItems.add("Validation");
		inItems.add("Drawer");
		
		outItems.add("Result");
	}
	
	@Override
	protected Color buildBackgroundColor() {
		return COLOR_FILTER;
	}
	
	@Override
	public void link(PipelineComponent view, LinkPosition position) {
		if (view.type == ComponentType.VALIDATION) {
			validationView = (ValidationView) view;
			validationView.filterView = this;
			filter.clearValidations();
			addValidation(validationView);
		} else if (view.type == ComponentType.DRAWER) {
			drawerView = (DrawerView) view;
			drawerView.filterView = this;
		}
	}
	
	@Override
	public void unlink(PipelineComponent view, LinkPosition position) {
		if (view.type == ComponentType.VALIDATION) {
			validationView = (ValidationView) view;
			validationView.filterView = null;
			filter.clearValidations();
		}
	}
	
	private void addValidation(ValidationView validationView) {
		filter.addValidation(validationView.getValidation());
		if (validationView.getNext() != null) {
			addValidation(validationView.getNext());
		}
	}
	
	@Override
	public boolean isValidLink(PipelineComponentItem fromItem,
			PipelineComponent to, PipelineComponentItem toItem) {
		
		if (fromItem.getInItem() && fromItem.getIndex() == 1) {
			return to.type == ComponentType.VALIDATION && toItem.getInItem() && toItem.getIndex() == 0;
		}
		
		if (fromItem.getInItem() && fromItem.getIndex() == 2) {
			return to.type == ComponentType.DRAWER && toItem.getInItem() && toItem.getIndex() == 0;
		}
		
		return false;
	}


	public DrawerView getDrawer() {
		return drawerView;
	}
}
