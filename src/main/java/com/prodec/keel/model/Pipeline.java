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

    public void add(PipelineComponent component) {
        components.add(component);
    }

    public void add(PipelineLinkView link) {
        links.add(link);
    }

    public List<PipelineComponent> getComponents() {
        return components;
    }

    public List<PipelineLinkView> getLinks() {
        return links;
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
                    mode = Mode.SELECTION;
                } else if (component != currentLink.getFrom()) {
                    currentLink.setTo(component);
                }
            }
        }

        if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
            linkComponents();
            resetLink();
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
                ((DrawerView) component).drawResults(g);
            }
        }

        for (PipelineComponent component : components) {
            component.draw(g);
        }

        for (PipelineLinkView link : links) {
            link.drawJoints(g);
        }
    }
}
