package com.prodec.keel.model.attribute;

import java.awt.Color;

import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.gui.theme.ThemeManager;

import com.prodec.keel.ui.PipelineComponent;

public class ColorPickerAttribute extends Attribute {

	int mx, my;
	private boolean onMouse = false;
	private boolean waitingColor = false;
	private boolean shouldPickColor = false;
	
	private static final int SIZE = 12;
    int color;

    public ColorPickerAttribute(String label) {
        super(label, AttributeType.COLOR_PICKER);
        this.color = Color.BLACK.getRGB();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    
    @Override
    public void updateMouse(PointerEvent event) {
    	mx = event.getX();
    	my = event.getY();
    	onMouse = false;
    	if ((mx > rectX() && mx < rectX() + SIZE) &&
    	   (my > rectY() && my < rectY() + SIZE)) {
    		onMouse = true;
    	}
    	
    	if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
    		if (onMouse) {
    			mousePressed = true;
    			waitingColor = true;
    			return;
    		} else if(waitingColor && !mousePressed) {
    			shouldPickColor = true;
    		}
    	}
    	
    	if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
    		mousePressed = false;
    	}
    }
    
    @Override
    public void draw(Graphics g) {
    	int rx = rectX();
    	int ry = rectY();
        int sepY = component.commonAttributesEnd();
        g.drawString(label, component.getX() + 14, sepY + PipelineComponent.ITEM_SPACING * (index + 1));
        
        g.setColor(color);
        g.fillRect(rx, ry, SIZE, SIZE);
        
        if (onMouse || waitingColor) {
        	g.setColor(ThemeManager.getInstance().getTheme().getActiveSelectionColor());
        } else {
        	g.setColor(ThemeManager.getInstance().getTheme().getBorderColor());
        }
        
        g.drawRect(rx, ry, SIZE, SIZE);
        g.setColor(component.fontColor());
        
        if(shouldPickColor) {
        	pickColor(g);
        }
    }
    
    private void pickColor(Graphics g) {
    	color = g.getBimg().getRGB(mx, my);
    	shouldPickColor = false;
    	waitingColor = false;
    	notifyChange();
	}

	private int rectX() {
    	return component.getX() + component.getW() - 14;
    }
    
    private int rectY() {
    	return component.commonAttributesEnd() + 6;
    }
    
}
