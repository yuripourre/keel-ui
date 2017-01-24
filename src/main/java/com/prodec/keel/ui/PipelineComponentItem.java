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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (inItem ? 1231 : 1237);
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PipelineComponentItem other = (PipelineComponentItem) obj;
		if (inItem != other.inItem)
			return false;
		if (index != other.index)
			return false;
		return true;
	}
	
	
}
