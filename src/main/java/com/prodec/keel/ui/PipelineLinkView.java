package com.prodec.keel.ui;

import java.awt.Color;

import br.com.etyllica.core.graphics.Graphics;

import com.prodec.keel.model.PipelineLink;

public class PipelineLinkView {

	private static final Color COLOR_BORDER = new Color(0x1c, 0x1c, 0x1c, 0xe5);
	private static final Color COLOR_LINE = new Color(0x64, 0x64, 0x64, 0xe5);
	
	private static final int RADIUS_SOCKET = PipelineComponent.SOCKET_SIZE/2;
	private static final int RADIUS_JOINT = PipelineComponent.SOCKET_SIZE/4;
		
	private PipelineLink link;
	
	public PipelineLinkView(PipelineLink link) {
		this.link = link;
	}

	public void drawLine(Graphics g) {
		PipelineComponent from = link.getFrom();

		int px = from.itemSocketX(link.getFromItem());
		int py = from.itemSocketY(link.getFromItem());
		
		PipelineComponent to = link.getTo();

		int qx = to.itemSocketX(link.getToItem());
		int qy = to.itemSocketY(link.getToItem());
		
		g.setColor(COLOR_BORDER);
		g.setLineWidth(3.6f);
		g.drawLine(px + RADIUS_SOCKET, py + RADIUS_SOCKET, qx + RADIUS_SOCKET, qy + RADIUS_SOCKET);
		g.setLineWidth(1);
		g.setColor(COLOR_LINE);
		g.drawLine(px + RADIUS_SOCKET, py + RADIUS_SOCKET, qx + RADIUS_SOCKET, qy + RADIUS_SOCKET);
	}
	
	public void drawJoints(Graphics g) {
		PipelineComponent from = link.getFrom();

		int px = from.itemSocketX(link.getFromItem());
		int py = from.itemSocketY(link.getFromItem());
		
		PipelineComponent to = link.getTo();

		int qx = to.itemSocketX(link.getToItem());
		int qy = to.itemSocketY(link.getToItem());
		
		g.setColor(COLOR_LINE);
		
		g.fillCircle(px + RADIUS_SOCKET, py + RADIUS_SOCKET, RADIUS_JOINT);
		g.fillCircle(qx + RADIUS_SOCKET, qy + RADIUS_SOCKET, RADIUS_JOINT);
	}
	
}
