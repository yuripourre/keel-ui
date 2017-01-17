package com.prodec.keel.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.prodec.keel.application.FilterViewApplication;
import com.prodec.keel.application.Mode;
import com.prodec.keel.model.ComponentType;

import br.com.etyllica.awt.SVGColor;
import br.com.etyllica.core.Drawable;
import br.com.etyllica.core.collision.CollisionDetector;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.Layer;
import br.com.etyllica.theme.ThemeManager;

public abstract class PipelineComponent extends Layer implements Drawable {

	protected ComponentType type = ComponentType.UNKNOWN;
	public static final int NONE = -1;
		
	public static final PipelineComponentItem INVALID_ITEM = new PipelineComponentItem(NONE, false);  
	PipelineComponentItem selectedItem = INVALID_ITEM;
	PipelineComponentItem mouseOnItem = INVALID_ITEM;

	protected String title = "";

	protected static final int VIEW_WIDTH = 200;

	public static final int SOCKET_SIZE = 10;
	protected static final int ITEM_SPACING = 16;	
	protected static final int PADDING_TITLE_BAR = 6;
	protected static final int TITLE_BAR = 30;

	protected static final Color COLOR_TITLE = new Color(0xff, 0xff, 0xff, 0x44);
	protected static final Color COLOR_FILTER = new Color(0x45, 0x96, 0xe8);
	protected static final Color COLOR_VALIDATION = new Color(0xe8, 0x45, 0x96);
	
	//Drag Event
	private int lastX = 0;
	private int lastY = 0;
	private int dragX = 0;
	private int dragY = 0;
	private boolean move = false;
	protected boolean dragged = false;

	protected List<String> inItems = new ArrayList<String>();
	protected List<String> outItems = new ArrayList<String>();

	public PipelineComponent(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	@Override
	public void draw(Graphics g) {
		drawBrackground(g);
		drawTitleBar(g);
		//Draw Border
		g.drawRect(this);

		//Draw Sockets
		drawCommonAttributes(g);
	}

	protected void drawBrackground(Graphics g) {
		Color background = ThemeManager.getInstance().getTheme().getPanelColor();
		g.setColor(background);
		g.fillRect(this);
	}

	protected void drawTitleBar(Graphics g) {
		//Draw Title Bar
		g.setColor(COLOR_TITLE);
		g.fillRect(x, y, w, TITLE_BAR);

		//Draw Title
		g.setColor(Color.BLACK);
		g.drawLine(x, y + TITLE_BAR, x + w, y + TITLE_BAR);
		g.drawString(title, x + PADDING_TITLE_BAR, y + PADDING_TITLE_BAR + TITLE_BAR/2);
	}

	protected void drawCommonAttributes(Graphics g) {
		int ty = commonAttributesStart() - 2;
		int sepY = commonAttributesEnd();

		int count = 0;
		for (String item : inItems) {
			int ix = itemSocketX(count, true);
			int iy = itemSocketY(count, true);
			
			g.setColor(buildItemColor(count, true));
			drawSocket(g, ty, count, item, ix, iy);
			g.setColor(SVGColor.BLACK);
			drawInItemText(g, ty, count, item, ix);
			
			count++;
		}
		count = 0;
		for (String item : outItems) {
			int ix = itemSocketX(count, false);
			int iy = itemSocketY(count, false);
			
			g.setColor(buildItemColor(count, false));
			drawSocket(g, ty, count, item, ix, iy);
			g.setColor(SVGColor.BLACK);
			drawOutItemText(g, ty, count, item, ix);
			
			count++;
		}

		g.drawLine(x, sepY, x + w, sepY);
	}

	private void drawInItemText(Graphics g, int ty, int count, String item, int ix) {
		g.drawString(item, ix + 12, ty + ITEM_SPACING * count);
	}
	
	private void drawOutItemText(Graphics g, int ty, int count, String item, int ix) {
		int width = g.getFontMetrics().stringWidth(item);
		g.drawString(item, ix - width - 2, ty + ITEM_SPACING * count);
	}

	private void drawSocket(Graphics g, int ty, int count, String item, int ix,
			int iy) {
		g.fillOval(ix, iy, SOCKET_SIZE, SOCKET_SIZE);
		g.setColor(SVGColor.GRAY);
		g.drawOval(ix, iy + ITEM_SPACING * count, SOCKET_SIZE, SOCKET_SIZE);
	}
	
	private Color buildItemColor(int count, boolean inItem) {
		if (mouseOnItem.inItem == inItem && mouseOnItem.index == count) {
			return SVGColor.CADET_BLUE;
		} else if (selectedItem.inItem == inItem && selectedItem.index == count) {
			return SVGColor.GOLDENROD;
		} else {
			return SVGColor.GHOST_WHITE;
		}
	}
	
	public int itemSocketX(int index, boolean inItem) {
		if (inItem) {
			return x + 2;
		} else {
			return x + w - SOCKET_SIZE - 2;
		}
	}
	
	public int itemSocketX(PipelineComponentItem item) {
		return itemSocketX(item.index, item.inItem);
	}
	
	public int itemSocketY(int item, boolean inItem) {
		int fy = y + TITLE_BAR + 6;
		return fy + ITEM_SPACING * item;
	}
	
	public int itemSocketY(PipelineComponentItem item) {
		return itemSocketY(item.index, item.inItem);
	}

	public void updateKeyboard(KeyEvent event) {
		// TODO Auto-generated method stub

	}

	public void updateMouse(PointerEvent event) {
		if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {

			if (!dragged && mouseOnItem == INVALID_ITEM && !move && mouseOnTitle(event)) {
				move = true;
				dragged = true;
				dragX = event.getX();
				dragY = event.getY();
				lastX = x;
				lastY = y;
			}

		} else if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
			dragged = false;
			move = false;
			selectedItem = INVALID_ITEM;
		}

		if (move) {
			x = lastX + event.getX() - dragX;
			y = lastY + event.getY() - dragY;
		}

		if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
			if (mouseOnItem != INVALID_ITEM) {
				selectedItem = new PipelineComponentItem(mouseOnItem);
			}
		}
		if (!dragged) {
			checkMouseOnItem(event);
		}

	}

	private void checkMouseOnItem(PointerEvent event) {
		int mx = event.getX();
		int my = event.getY();

		mouseOnItem = INVALID_ITEM;
		for (int i = 0; i < inItems.size(); i++) {
			int sx = itemSocketX(i, true);
			int sy = itemSocketY(i, true);
			
			if (mx > sx && mx < sx + SOCKET_SIZE && my > sy && my < sy + SOCKET_SIZE) {
				mouseOnItem = new PipelineComponentItem(i, true);
				return;
			}
		}
		
		for (int i = 0; i < outItems.size(); i++) {
			int sx = itemSocketX(i, false);
			int sy = itemSocketY(i, false);

			if (mx > sx && mx < sx + SOCKET_SIZE && my > sy && my < sy + SOCKET_SIZE) {
				mouseOnItem = new PipelineComponentItem(i, false);
				return;
			}
		}
	}

	protected boolean mouseOnTitle(PointerEvent event) {
		//Avoid move while linking components
		if (FilterViewApplication.getMode() == Mode.SELECTION) {
			return false;
		}
		
		return CollisionDetector.colideRectPoint(x,  y,  w,  TITLE_BAR, event.getX(),  event.getY());
	}

	protected int commonAttributesStart() {
		return y + 18 + TITLE_BAR;
	}

	protected int commonAttributesEnd() {
		return y + TITLE_BAR + inItems.size() * 20;
	}

	public PipelineComponentItem getMouseOnItem() {
		return mouseOnItem;
	}

	public String getItemLabel(PipelineComponentItem item) {
		int index = item.index;
		
		if(item.inItem) {
			return inItems.get(index);	
		} else {
			return outItems.get(index);
		}
	}

	public void link(PipelineComponent from) {
		// TODO Auto-generated method stub
	}

	public boolean isValidLink(PipelineComponentItem fromItem,
			PipelineComponent to, PipelineComponentItem toItem) {
		// TODO Auto-generated method stub
		return false;
	}

	public PipelineComponentItem getSelectedItem() {
		// TODO Auto-generated method stub
		return selectedItem;
	}
	
	public boolean isMoving() {
		return move;
	}
	
}
