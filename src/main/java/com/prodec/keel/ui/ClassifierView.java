package com.prodec.keel.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.LinkPosition;

public abstract class ClassifierView extends PipelineComponent {

	protected FilterView filterView = null;
	
	protected List<Component> results = new ArrayList<Component>();
	protected Map<Component, String> classifications = new HashMap<Component, String>();
	
	public ClassifierView(int x, int y, int w, int h) {
		super(x, y, w, h);
		type = ComponentType.CLASSIFIER;
		inItems.add("Input");
	}
	
	@Override
	protected Color buildBackgroundColor() {
		return COLOR_CLASSIFIER;
	}

	@Override
	public void link(PipelineComponent view, LinkPosition position) {
		switch (view.type) {
		case FILTER:
			filterView = ((FilterView) view);
			filterView.link(this, position);
			break;
		default:
			break;
		}
	}

	@Override
	public void unlink(PipelineComponent view, LinkPosition position) {
		switch (view.type) {
		case FILTER:
			filterView.unlink(this, position);
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean isValidLink(PipelineComponentItem fromItem,
			PipelineComponent to, PipelineComponentItem toItem) {

		if (to.type == ComponentType.FILTER) {
			return to.isValidLink(toItem, this, fromItem);
		}

		return false;
	}

	public void setResults(List<Component> results) {
		this.results.clear();
		this.results.addAll(results);
	}

	public abstract void classify(List<Component> results);
}
