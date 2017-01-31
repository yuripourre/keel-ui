package com.prodec.keel.model.attribute;

import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.core.ui.UIComponent;

import com.prodec.keel.ui.PipelineComponent;

public class Attribute implements UIComponent {

    protected int index;
    
    String label;
    AttributeType type = AttributeType.UNKNOWN;
    AttributeListener listener;
    PipelineComponent component;
    
    protected boolean mousePressed = false;

    public Attribute(String label, AttributeType type) {
        super();
        this.label = label;
        this.type = type;
    }

    public Attribute(String label, AttributeType type, AttributeListener listener) {
        super();
        this.label = label;
        this.type = type;
        this.listener = listener;
    }

    public AttributeType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public void setId(int id) {
        this.index = id;
    }

    public void setListener(AttributeListener listener) {
        this.listener = listener;
    }

    public int getId() {
        return index;
    }
    
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(long now) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMouse(PointerEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void setComponent(PipelineComponent pipelineComponent) {
		this.listener = pipelineComponent;
		this.component = pipelineComponent;
	}
	
    public boolean isMousePressed() {
		return mousePressed;
	}
    
    public void notifyChange() {
    	component.onValueChange(index);
    }
}
