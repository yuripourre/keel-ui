package com.prodec.keel.ui;

import java.awt.Color;

import br.com.etyllica.motion.core.strategy.ComponentValidationStrategy;

import com.prodec.keel.model.ComponentType;

public class ValidationView extends PipelineComponent {

	protected FilterView filterView = null;
	protected ValidationView previous = null;
	protected ValidationView next = null;

	protected ComponentValidationStrategy validation;

	public ValidationView(int x, int y, int w, int h) {
		super(x, y, w, h);
		type = ComponentType.VALIDATION;
		inputs.add("Filter");
		outputs.add("Next");
	}
	
	@Override
	protected Color buildBackgroundColor() {
		return COLOR_VALIDATION;
	}

	public ComponentValidationStrategy getValidation() {
		return validation;
	}

	@Override
	public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		
		ValidationView root;
		switch (view.type) {
		case VALIDATION:

			if (fromNext(fromItem, toItem)) {
				previous = ((ValidationView) view);
				previous.next = this;
			} else if (toNext(fromItem, toItem)) {
				next = ((ValidationView) view);
				next.previous = this;
			}

			link(fromItem, toItem);
			
			break;
		case FILTER:
			root = root();
			root.filterView = ((FilterView) view);
			link(fromItem, toItem);
			
			break;
		default:
			break;
		}
	}
	
	private boolean fromNext(PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		return (fromItem.inItem && fromItem.index == 0 && !toItem.inItem && toItem.index == 0);
	}
	
	private boolean toNext(PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		return (!fromItem.inItem && fromItem.index == 0 && toItem.inItem && toItem.index == 0);
	}

	@Override
	public void unlink(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		
		ValidationView root;
		
		switch (view.type) {
		case VALIDATION:
			ValidationView it = (ValidationView)view;

			root = root();

			if (previous == it) {
				previous = null;
				it.next = null;
				root = it;
			} else if (next == it) {
				next = null;
				it.previous = null;
				root = this;
			}
			
			if (root.filterView != null) {
				root.filterView.link(root, fromItem, toItem);
				root.filterView.resetFilter();
			}
			
			break;
		case FILTER:
			view.unlink(root(), fromItem, toItem);
			unlink(fromItem, toItem);
			break;
		default:
			break;
		}
	}
	
	private void link(PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		ValidationView root = root();
		if (root.filterView != null) {
			root.filterView.link(root, fromItem, toItem);
			root.filterView.resetFilter();
		}
	}
	
	private void unlink(PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		ValidationView root = root();
		if (root.filterView != null) {
			root.filterView.unlink(root, fromItem, toItem);
			root.filterView.resetFilter();
		}
	}
	
	protected ValidationView root() {
		if (previous != null) {
			return previous.root();
		} else {
			return this;
		}
	}


	@Override
	public boolean isValidLink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

		if (to.type == ComponentType.VALIDATION) {
			boolean toNext = toNext(fromItem, toItem);
			boolean fromNext = fromNext(fromItem, toItem);

			boolean validRoot = next != to && previous != to;
						
			return validRoot && (toNext || fromNext);

		} else if (to.type == ComponentType.FILTER) {
			return to.isValidLink(this, toItem, fromItem);
		}

		return false;
	}

	public ValidationView getNext() {
		return next;
	}
	
	protected void resetFilter() {
		FilterView filter = root().filterView;
		if (filter != null) {
			filter.updateValidations();
		}
	}
}
