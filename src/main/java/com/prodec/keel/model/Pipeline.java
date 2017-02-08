package com.prodec.keel.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;

import com.prodec.keel.ui.DrawerView;
import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.PipelineComponentItem;
import com.prodec.keel.ui.PipelineLinkView;
import com.prodec.keel.ui.SourceView;

public class Pipeline {

	private static Mode mode = Mode.NORMAL;
	
	PipelineLink currentLink = new PipelineLink();
	
    List<PipelineLinkView> links = new ArrayList<PipelineLinkView>();
    List<PipelineComponent> components = new ArrayList<PipelineComponent>();
    
    int index = 0;
    
    int x, y;
    int mx, my;
    private int lastX = 0;
	private int lastY = 0;
    int dragX, dragY;
    boolean drag = false;
    boolean move = false;
    
    public Pipeline() {
    	this(0, 0);
    }
    
    public Pipeline(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void add(PipelineComponent component) {
    	component.setIndex(index);
        components.add(component);
        index++;
    }

    public void add(PipelineLinkView link) {
        links.add(link);
    }

    public void removeLink(PipelineComponent component) {
        PipelineComponentItem item = component.getMouseOnItem();

        for (int i = links.size() - 1; i >= 0; i--) {
            PipelineLinkView view = links.get(i);
            PipelineLink link = view.getLink();
            if (link.getFrom() == component && link.getFromItem().equals(item) ||
                    link.getTo() == component && link.getToItem().equals(item)) {
                link.unlink();
                links.remove(i);

                break;
            }
        }
    }
    
    public void updateMouse(PointerEvent event) {
    	mx = event.getX();
    	my = event.getY();
    	
        for (PipelineComponent component : getComponents()) {
            component.updateMouse(event);

            if (component.isMoving()) {
                mode = Mode.SELECTION;
                return;
            } else if (component.isRemoveLink()) {
                removeLink(component);
                component.linkRemoved();
                return;
            }

            if (component.getSelectedItem() != PipelineComponent.INVALID_ITEM) {
                if (currentLink.getFromItem() == PipelineComponent.INVALID_ITEM) {
                    currentLink.setFrom(component);
                    currentLink.setFromItem(component.getMouseOnItem());
                    mode = Mode.SELECTION;
                } else if (component != currentLink.getFrom()) {
                    currentLink.setTo(component);
                    currentLink.setToItem(component.getMouseOnItem());
                }
            }
        }

        if (!drag && mode != Mode.SELECTION && event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
        	drag = true;
        	lastX = x;
        	lastY = y;
        	dragX = mx;
        	dragY = my;
        	
        	if (mode != Mode.SELECTION) {
        		move = true;
        		mode = Mode.SELECTION;
        	}
        	
        }
        
        if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
            linkComponents();
            resetLink();
            drag = false;
            move = false;
            mode = Mode.NORMAL;
        }
        
        if (move) {
        	x = lastX + mx - dragX;
        	y = lastY + my - dragY;
        	for (PipelineComponent component : components) {
                component.setOffset(x, y);
            }	
        }
    }

    private void resetLink() {
        currentLink.reset();
        mode = Mode.NORMAL;
    }

    public static Mode getMode() {
        return mode;
    }

    private void linkComponents() {
        if (currentLink.isValid()) {
            currentLink.link();
            add(new PipelineLinkView(currentLink));
            currentLink = new PipelineLink();
        }
    }

    public void updateKeyboard(KeyEvent event) {
        for (PipelineComponent component : components) {
            component.updateKeyboard(event);
        }
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        for (PipelineLinkView link : links) {
            link.drawLine(g);
        }

        for (PipelineComponent component : components) {
            if (ComponentType.SOURCE == component.getType()) {
                ((SourceView) component).drawSource(g);
            }
            if (ComponentType.DRAWER == component.getType()) {
                ((DrawerView<?>) component).drawResults(g);
            }
        }

        for (PipelineComponent component : components) {
            component.draw(g);
        }

        for (PipelineLinkView link : links) {
            link.drawJoints(g);
        }
    }

	public List<PipelineLinkView> getLinks() {
		return links;
	}

	public void setLinks(List<PipelineLinkView> links) {
		this.links = links;
	}

	public List<PipelineComponent> getComponents() {
		return components;
	}

	public void setComponents(List<PipelineComponent> components) {
		this.components = components;
		index = components.size();
	}

	public PipelineComponent getComponent(int index) {
		for (PipelineComponent component : components) {
			if (component.getIndex() == index) {
				return component;
			}
		}
		return null;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void setDrawPosition(int drawX, int drawY) {
		for (PipelineComponent component : components) {
			component.setDrawPosition(drawX, drawY);
		}
	}
    
}
