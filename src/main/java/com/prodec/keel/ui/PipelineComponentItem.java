package com.prodec.keel.ui;

public class PipelineComponentItem {
	
	int index = PipelineComponent.NONE;
	
	boolean inItem = true;
		
	public PipelineComponentItem(PipelineComponentItem mOnItem) {
		index = mOnItem.index;
		inItem = mOnItem.inItem;
	}

	public PipelineComponentItem(int index, boolean inItem) {
		this.index = index;
		this.inItem = inItem;
	}

	public int getIndex() {
		return index;
	}
	
	public boolean getInItem() {
		return inItem;
	}
}
