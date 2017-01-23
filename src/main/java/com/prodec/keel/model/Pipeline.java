package com.prodec.keel.model;

import com.prodec.keel.ui.FilterView;
import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.PipelineComponentItem;
import com.prodec.keel.ui.PipelineLinkView;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {

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
}
