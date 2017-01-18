package com.prodec.keel.ui;

import java.awt.Color;

import br.com.etyllica.motion.core.strategy.ComponentValidationStrategy;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.LinkPosition;

public class ValidationView extends PipelineComponent {

	protected FilterView filterView = null;
	protected ValidationView previous = null;
	protected ValidationView next = null;

	protected ComponentValidationStrategy validation;

	public ValidationView(int x, int y, int w, int h) {
		super(x, y, w, h);
		type = ComponentType.VALIDATION;
		inItems.add("Filter");
		outItems.add("Next");
	}
	
	@Override
	protected Color buildBackgroundColor() {
		return COLOR_VALIDATION;
	}

	public ComponentValidationStrategy getValidation() {
		return validation;
	}

	@Override
	public void link(PipelineComponent view, LinkPosition position) {
		switch (view.type) {
		case VALIDATION:
			if (LinkPosition.TO == position) {
				next = ((ValidationView) view);
				next.previous = this;
			} else {
				previous = ((ValidationView) view);
				previous.next = this;
			}

			updateFilter(root());
			
			break;
		case FILTER:
			ValidationView rootView = root();
			rootView.filterView = ((FilterView) view);
			rootView.filterView.link(rootView, LinkPosition.TO);
			break;
		default:
			break;
		}
	}

	@Override
	public void unlink(PipelineComponent view, LinkPosition position) {
		switch (view.type) {
		case VALIDATION:
			ValidationView it = (ValidationView)view;
			
			ValidationView root = root();
			
			if (previous == it) {
				previous = null;
				it.next = null;
			} else if (next == it) {
				next = null;
				it.previous = null;
			}
			
			updateFilter(root);
			
			break;
		case FILTER:
			ValidationView rootView = root();
			FilterView filterView = ((FilterView) view);
			filterView.unlink(rootView, position);
			break;
		default:
			break;
		}
	}
	
	private ValidationView root() {
		if (previous != null) {
			return previous.root();
		} else {
			return this;
		}
	}
	
	private void updateFilter(ValidationView root) {
		if (root.filterView != null) {
			root.filterView.link(root, LinkPosition.TO);
		}
	}

	@Override
	public boolean isValidLink(PipelineComponentItem fromItem,
			PipelineComponent to, PipelineComponentItem toItem) {

		if (to.type == ComponentType.VALIDATION) {
			boolean toNext = !fromItem.inItem && fromItem.index == 0 
					&& toItem.inItem && toItem.index == 0;
			boolean fromNext = fromItem.inItem && fromItem.index == 0 
					&& !toItem.inItem && toItem.index == 0;

			boolean validRoot = next != to && previous != to;
						
			return validRoot && (toNext || fromNext);

		} else if (to.type == ComponentType.FILTER) {
			return to.isValidLink(toItem, this, fromItem);
		}

		return false;
	}

	public ValidationView getNext() {
		return next;
	}
}
