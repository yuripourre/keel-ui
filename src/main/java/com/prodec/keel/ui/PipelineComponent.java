package com.prodec.keel.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.etyllica.awt.SVGColor;
import br.com.etyllica.core.collision.CollisionDetector;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.core.ui.UIComponent;
import br.com.etyllica.gui.theme.ThemeManager;
import br.com.etyllica.layer.Layer;
import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.Mode;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.AttributeListener;

public abstract class PipelineComponent extends Layer implements UIComponent, AttributeListener {

	protected ComponentType type = ComponentType.UNKNOWN;
	public static final int NONE = -1;
		
	public static final PipelineComponentItem INVALID_ITEM = new PipelineComponentItem(NONE, false);  
	PipelineComponentItem selectedItem = INVALID_ITEM;
	PipelineComponentItem mouseOnItem = INVALID_ITEM;

	protected String title = "";

	protected static final int VIEW_WIDTH = 240;

	public static final int SOCKET_SIZE = 10;
	public static final int ITEM_SPACING = 16;
	protected static final int PADDING_TITLE_BAR = 6;
	protected static final int TITLE_BAR = 30;
	protected static final int BORDER_ROUNDNESS = 18;

	protected static final Color COLOR_TITLE = new Color(0xff, 0xff, 0xff, 0x44);
	protected static final Color COLOR_FILTER = new Color(0x45, 0x96, 0xe8, 0xe5);
	protected static final Color COLOR_VALIDATION = new Color(0x45, 0xe8, 0x96, 0xe5);
	protected static final Color COLOR_MODIFIER = new Color(0xe8, 0x45, 0x45, 0xe5);
	protected static final Color COLOR_DRAWER = new Color(0x45, 0x45, 0x45, 0xe5);
	protected static final Color COLOR_CLASSIFIER = new Color(0xe8, 0xe8, 0x45, 0xe5);
	protected static final Color COLOR_SOURCE = new Color(0x45, 0x08, 0x96, 0xe5);
	
	//Drag Event
	private int lastX = 0;
	private int lastY = 0;
	private int dragX = 0;
	private int dragY = 0;
	private boolean move = false;
	protected boolean dragged = false;
	
	private boolean removeLink = false;

	protected List<String> inputs = new ArrayList<String>();
	protected List<String> outputs = new ArrayList<String>();

    private Map<Integer, Attribute> attributes = new HashMap<>();
    
    private int index;
    private int attributeCount = 0;
    
    protected String className;

	public PipelineComponent(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	@Override
	public void draw(Graphics g) {
		drawBrackground(g);
		drawTitleBar(g);
		//Draw Border
		drawBorder(g);

		//Draw Sockets
		drawCommonAttributes(g);
        drawParameters(g);
	}

    private void drawParameters(Graphics g) {
        for (Attribute attribute : attributes.values()) {
        	attribute.draw(g);
        }
    }

    protected void drawBrackground(Graphics g) {
		Color background = buildBackgroundColor();
		g.setColor(background);
		
		g.fillRoundRect(x, y, w, h, BORDER_ROUNDNESS, BORDER_ROUNDNESS);
	}
	
	protected void drawBorder(Graphics g) {
		Color background = Color.BLACK;
		g.setColor(background);
		
		g.drawRoundRect(x, y, w, h, BORDER_ROUNDNESS, BORDER_ROUNDNESS);
	}

	protected Color buildBackgroundColor() {
		return ThemeManager.getInstance().getTheme().getPanelColor();
	}

	protected void drawTitleBar(Graphics g) {
		//Draw Title Bar
		g.setColor(COLOR_TITLE);
		int halfBorder = BORDER_ROUNDNESS/2;
		g.fillArc(x, y, BORDER_ROUNDNESS, BORDER_ROUNDNESS, 180, -90);
		g.fillArc(x + w - BORDER_ROUNDNESS, y, BORDER_ROUNDNESS, BORDER_ROUNDNESS, 0, 90);
		g.fillRect(x + halfBorder, y, w - BORDER_ROUNDNESS, halfBorder);
		g.fillRect(x, y + halfBorder, w + 1, TITLE_BAR - halfBorder);

		//Draw Title
		g.setColor(fontColor());
		g.drawLine(x, y + TITLE_BAR, x + w - 1, y + TITLE_BAR);
		g.drawString(title, x + PADDING_TITLE_BAR, y + PADDING_TITLE_BAR + TITLE_BAR/2);
	}

	protected void drawCommonAttributes(Graphics g) {
		int ty = commonAttributesStart() - 2;
		int sepY = commonAttributesEnd();

		int count = 0;
		for (String item : inputs) {
			int ix = itemSocketX(count, true);
			int iy = itemSocketY(count, true);
			
			g.setColor(buildItemColor(count, true));
			drawSocket(g, ty, item, ix, iy);
			g.setColor(fontColor());
			drawInItemText(g, ty, count, item, ix);
			
			count++;
		}
		
		count = 0;
		for (String item : outputs) {
			int ix = itemSocketX(count, false);
			int iy = itemSocketY(count, false);
			
			g.setColor(buildItemColor(count, false));
			drawSocket(g, ty, item, ix, iy);
			g.setColor(fontColor());
			drawOutItemText(g, ty, count, item, ix);
			
			count++;
		}

		g.drawLine(x, sepY, x + w - 1, sepY);
	}

	private void drawInItemText(Graphics g, int ty, int count, String item, int ix) {
		g.drawString(item, ix + 12, ty + ITEM_SPACING * count);
	}
	
	private void drawOutItemText(Graphics g, int ty, int count, String item, int ix) {
		int width = g.getFontMetrics().stringWidth(item);
		g.drawString(item, ix - width - 2, ty + ITEM_SPACING * count);
	}

	private void drawSocket(Graphics g, int ty, String item, int ix, int iy) {
		g.fillOval(ix, iy, SOCKET_SIZE, SOCKET_SIZE);
		g.setColor(SVGColor.GRAY);
		g.drawOval(ix, iy, SOCKET_SIZE, SOCKET_SIZE);
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

	public void update(long now) {

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
		
		if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_RIGHT) && mouseOnItem != INVALID_ITEM) {
			removeLink = true;
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
		
		for (Attribute attribute : attributes.values()) {
        	attribute.updateMouse(event);
        }
	}

	private void checkMouseOnItem(PointerEvent event) {
		int mx = event.getX();
		int my = event.getY();

		mouseOnItem = INVALID_ITEM;
		for (int i = 0; i < inputs.size(); i++) {
			int sx = itemSocketX(i, true);
			int sy = itemSocketY(i, true);
			
			if (mx > sx && mx < sx + SOCKET_SIZE && my > sy && my < sy + SOCKET_SIZE) {
				mouseOnItem = new PipelineComponentItem(i, true);
				return;
			}
		}
		
		for (int i = 0; i < outputs.size(); i++) {
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
		if (Pipeline.getMode() == Mode.SELECTION) {
			return false;
		}
		
		return CollisionDetector.colideRectPoint(x,  y,  w,  TITLE_BAR, event.getX(),  event.getY());
	}

	protected int commonAttributesStart() {
		return y + 18 + TITLE_BAR;
	}

	public int commonAttributesEnd() {
		int size = inputs.size();
		if (outputs.size() > inputs.size()) {
			size = outputs.size(); 
		}
		return y + TITLE_BAR + size * 20;
	}

	public PipelineComponentItem getMouseOnItem() {
		return mouseOnItem;
	}

	public String getItemLabel(PipelineComponentItem item) {
		int index = item.index;
		
		if(item.inItem) {
			return inputs.get(index);	
		} else {
			return outputs.get(index);
		}
	}

	public void unlink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		// TODO Auto-generated method stub
		
	}
	
	public void link(PipelineComponent from, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
		// TODO Auto-generated method stub
	}

	public boolean isValidLink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
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
	
	public boolean isRemoveLink() {
		return removeLink;
	}
	
	public void linkRemoved() {
		removeLink = false;
	}

	public ComponentType getType() {
		return type;
	}

	//Draw Attributes Method
	protected void drawColorPickerAttribute(Graphics g, String label, int order, int color) {
		int sepY = commonAttributesEnd();
		g.drawString(label, x + 14, sepY + ITEM_SPACING * (order + 1));
		g.setColor(color);
		g.fillRect(x + w - 14, sepY + 6, 12, 12);
		
		g.setColor(fontColor());
	}
	
	protected void drawColorPickerAttribute(Graphics g, String label, int order, Color color) {
		int sepY = commonAttributesEnd();
		g.drawString(label, x + 14, sepY + ITEM_SPACING * (order + 1));
		g.setColor(color);
		g.fillRect(x + w - 14, sepY + 6, 12, 12);
		
		g.setColor(fontColor());
	}
	
	protected void drawSliderAttribute(Graphics g, String label, int order, int value) {
		int sepY = commonAttributesEnd();
		g.drawString(label, x + 14, sepY + ITEM_SPACING * (order + 1));
		g.drawString(Integer.toString(value), x + 154, sepY + ITEM_SPACING * (order + 1));
	}

	protected void drawRegionAttribute(Graphics g, String label, int order, Component region) {
		int sepY = commonAttributesEnd();
		g.drawString(label, x + 14, sepY + ITEM_SPACING * (order + 1));

		String regionText = region.getX()+", "+region.getY()+", "+region.getW()+", "+region.getH();
		g.drawString(regionText, x + 154, sepY + ITEM_SPACING * (order + 1));
	}

	protected void drawFileDialogAttribute(Graphics g, String label, int order, String path) {
		int sepY = commonAttributesEnd();
		g.drawString(label, x + 14, sepY + ITEM_SPACING * (order + 1));
		g.drawString(path, x + 154, sepY + ITEM_SPACING * (order + 1));
	}
	
	public Color fontColor() {
		return ThemeManager.getInstance().getTheme().getTextColor();
	}

    protected void addAttribute(Attribute attribute) {
        attribute.setComponent(this);

        int attributeId = attributeCount;
        attribute.setId(attributeId);
        attributes.put(attributeId, attribute);
        
        attributeCount++;
    }

    protected Attribute getAttribute(int attributeId) {
        return attributes.get(attributeId);
    }

    @Override
    public void onValueChange(int attributeId) {

    }
    
	public String getTitle() {
		return title;
	}

	public List<String> getInputs() {
		return inputs;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	public Map<Integer, Attribute> getAttributes() {
		return attributes;
	}
	
	public String getClassName() {
		return className;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
		
}
