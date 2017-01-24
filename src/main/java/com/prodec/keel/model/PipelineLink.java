package com.prodec.keel.model;

import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.PipelineComponentItem;

public class PipelineLink {
	
	PipelineComponent from;
	PipelineComponentItem fromItem = PipelineComponent.INVALID_ITEM;

	PipelineComponent to;
	PipelineComponentItem toItem = PipelineComponent.INVALID_ITEM;
	
	public PipelineComponent getFrom() {
		return from;
	}
	
	public void setFrom(PipelineComponent from) {
		this.from = from;
	}
	
	public PipelineComponentItem getFromItem() {
		return fromItem;
	}
		
	public PipelineComponent getTo() {
		return to;
	}
	
	public void setTo(PipelineComponent to) {
		this.to = to;
	}
	
	public PipelineComponentItem getToItem() {
		return toItem;
	}

	public boolean isValid() {
		boolean validComponent = from != to;
		boolean validItem = fromItem != PipelineComponent.INVALID_ITEM && toItem != PipelineComponent.INVALID_ITEM;
		
		if (!validComponent || !validItem) {
			return false;
		}
		
		return from.isValidLink(to, fromItem, toItem);
	}
	
	public void reset() {
		fromItem = PipelineComponent.INVALID_ITEM;
		toItem = PipelineComponent.INVALID_ITEM;
	}

	public void unlink() {
		from.unlink(to, fromItem, toItem);
	}
	
	public void link() {
		from.link(to, fromItem, toItem);
	}

	public void setFromItem(PipelineComponentItem fromItem) {
		this.fromItem = fromItem;
	}

	public void setToItem(PipelineComponentItem toItem) {
		this.toItem = toItem;
	}
	
}
