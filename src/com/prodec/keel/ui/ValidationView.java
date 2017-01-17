package com.prodec.keel.ui;

import java.awt.Color;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.core.strategy.ComponentValidationStrategy;

import com.prodec.keel.model.ComponentType;

public class ValidationView extends PipelineComponent {

	protected FilterView filterView = null;
	protected ValidationView next = null;
	
	protected ComponentValidationStrategy validation;
	
	public ValidationView(int x, int y, int w, int h) {
		super(x, y, w, h);
		type = ComponentType.VALIDATION;
		inItems.add("Filter");
		outItems.add("Next");
	}
	
	@Override
	protected void drawBrackground(Graphics g) {
		Color background = COLOR_VALIDATION;
		g.setColor(background);
		g.fillRect(this);
	}

	public ComponentValidationStrategy getValidation() {
		return validation;
	}
	
	@Override
	public void link(PipelineComponent view) {
		switch (view.type) {
		case VALIDATION:
			next = ((ValidationView) view);
			if (filterView != null) {
				filterView.link(this);
			}
			break;
		case FILTER:
			filterView = ((FilterView) view); 
			filterView.link(this);
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean isValidLink(PipelineComponentItem fromItem,
			PipelineComponent to, PipelineComponentItem toItem) {
		
		if (to.type == ComponentType.VALIDATION) {
			boolean toNext = fromItem.inItem && fromItem.index == 0 
					&& !toItem.inItem && toItem.index == 0;
			boolean fromNext = toItem.inItem && toItem.index == 0 
					&& !fromItem.inItem && fromItem.index == 0;
			
			return toNext || fromNext;
			
		} else if (to.type == ComponentType.FILTER) {
			return to.isValidLink(toItem, this, fromItem);
		}
		
		return false;
	}

	public ValidationView getNext() {
		return next;
	}
}
