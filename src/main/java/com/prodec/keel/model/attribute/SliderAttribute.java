package com.prodec.keel.model.attribute;

import java.awt.Color;

import com.prodec.keel.ui.PipelineComponent;

import br.com.etyllica.core.collision.CollisionDetector;
import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.ui.theme.ThemeManager;

public class SliderAttribute extends Attribute {

	private static final int SLIDER_WIDTH = 80;
	private static final int RADIUS = 8;

	int currentValue;
	int maxValue;
	int minValue;

	//Mouse Stuff
	private boolean mouseOnButton = false;

	public SliderAttribute(String label) {
		super(label, AttributeType.SLIDER);
	}

	public SliderAttribute(String label, int currentValue, int minValue, int maxValue) {
		this(label);
		this.currentValue = currentValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public int getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(int currentValue) {
		this.currentValue = currentValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	@Override
	public void draw(Graphics g) {
		int ay = component.commonAttributesEnd() + PipelineComponent.ITEM_SPACING * (index + 1);

		g.setColor(Color.GRAY);
		int sliderX = sliderX();
		int sc = currentAsPoint();
		g.fillRect(sliderX, ay - 6, SLIDER_WIDTH, 4);

		g.setColor(ThemeManager.getInstance().getTheme().getBaseColor());
		g.fillRect(sliderX, ay - 6, sc, 4);

		if (mouseOnButton) {
			g.setColor(ThemeManager.getInstance().getTheme().getActiveColor());
		}
		g.fillCircle(sliderX+sc, ay-4, RADIUS);

		g.setColor(component.fontColor());
		g.drawString(label, component.getX() + 14, ay);
		g.drawString(Integer.toString(currentValue), component.getX()+component.getW() - 34, ay);
	}

	private int currentAsPoint() {
		int sc = currentValue*SLIDER_WIDTH/maxValue;
		return sc;
	}

	@Override
	public void updateMouse(PointerEvent event) {
		int mx = event.getX();
		int my = event.getY();

		int ay = component.commonAttributesEnd() + PipelineComponent.ITEM_SPACING * (index + 1);
		int sx = sliderX();
		
		if (mousePressed) {
			mouseOnButton = true;
		
			if(event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
				mousePressed = false;
				//Update component on mouseUp
				notifyChange();
				return;
			}
			
			if (mx < sx) {
				currentValue = minValue;
			} else if (mx >= sx + SLIDER_WIDTH) {
				currentValue = maxValue;	
			} else {
				currentValue = minValue + (mx-sx) * maxValue / SLIDER_WIDTH;
			}
			
		} else {
			if (CollisionDetector.colideCirclePoint(sx+currentAsPoint(), ay, RADIUS, mx, my)) {
				mouseOnButton = true;
				if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
					mousePressed = true;
				}
			} else {
				mouseOnButton = false;
			}
		}
	}

	private int sliderX() {
		return component.getX() + component.getW() - 120;
	}
	
}
