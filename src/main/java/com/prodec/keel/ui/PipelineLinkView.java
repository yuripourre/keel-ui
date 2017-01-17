package com.prodec.keel.ui;

import br.com.etyllica.core.Drawable;
import br.com.etyllica.core.graphics.Graphics;

import com.prodec.keel.model.PipelineLink;

public class PipelineLinkView implements Drawable {

	private PipelineLink link;
	
	public PipelineLinkView(PipelineLink link) {
		this.link = link;
	}

	@Override
	public void draw(Graphics g) {
		PipelineComponent from = link.getFrom();

		int px = from.itemSocketX(link.getFromItem());
		int py = from.itemSocketY(link.getFromItem());
		
		PipelineComponent to = link.getTo();

		int qx = to.itemSocketX(link.getToItem());
		int qy = to.itemSocketY(link.getToItem());
		
		int socketRadius = PipelineComponent.SOCKET_SIZE/2;
		
		g.drawLine(px + socketRadius, py + socketRadius, qx + socketRadius, qy + socketRadius);
	}
	
}
