package com.prodec.keel.ui;

import com.prodec.keel.model.DataType;

public abstract class PipelineDataComponent extends PipelineComponent {

	protected DataType dataType =  DataType.FEATURE;
	
	public PipelineDataComponent(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

}
