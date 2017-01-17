package com.prodec.keel.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.LinkPosition;

public class DrawerView extends PipelineComponent {

	private List<Component> results = new ArrayList<Component>();
	protected FilterView filterView = null;

	public DrawerView(int x, int y, int w, int h) {
		super(x, y, w, h);
		type = ComponentType.DRAWER;
		inItems.add("Input");
		outItems.add("Next");
	}

	@Override
	protected void drawBrackground(Graphics g) {
		Color background = COLOR_DRAWER;
		
		g.setColor(background);
		g.fillRect(this);
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

	public void drawResults(Graphics g) {
		for (Component component : results) {
			g.setColor(Color.BLACK);
			g.drawRect(component.getRectangle());
		}
	}

}
