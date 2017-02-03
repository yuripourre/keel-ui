package com.prodec.keel.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.core.source.ImageSource;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.attribute.RegionAttribute;

public abstract class SourceView extends PipelineComponent {
	
	protected ImageSource source;
	protected RegionAttribute regionAttribute;
	
	protected List<FilterView> outputViews = new ArrayList<FilterView>();

	public SourceView(int x, int y, int w, int h) {
		super(x, y, w, h);
		type = ComponentType.SOURCE;
		outputs.add("Output");

		regionAttribute = new RegionAttribute("Region");
		addAttribute(regionAttribute);
	}

	@Override
	protected Color buildBackgroundColor() {
		return COLOR_SOURCE;
	}

	@Override
	public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		switch (view.type) {
		case FILTER:
			outputViews.add((FilterView) view);
			updateFilter();
			break;
		default:
			break;
		}
	}

	@Override
	public void unlink(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		switch (view.type) {
		case FILTER:
			FilterView filterView = ((FilterView) view);
			filterView.source = null;
			filterView.region = null;
			filterView.resetFilter();
			outputViews.remove(view);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean isValidLink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

		if (to.type == ComponentType.FILTER) {
			return !fromItem.getInItem() && fromItem.getIndex() == 0 &&
					toItem.getInItem() && toItem.getIndex() == 0;
		}

		return false;
	}

	public abstract void drawSource(Graphics g);

	protected void updateFilter() {
		if (outputViews.isEmpty()) {
			return;
		}
		
		for (FilterView outputView : outputViews) {
			outputView.source = source;
			outputView.region = regionAttribute.getRegion();
			outputView.resetFilter();	
		}
	}
}
