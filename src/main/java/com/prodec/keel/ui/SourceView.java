package com.prodec.keel.ui;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.core.source.ImageSource;
import br.com.etyllica.motion.feature.Component;
import com.prodec.keel.model.ComponentType;

import java.awt.*;


public abstract class SourceView extends PipelineComponent {

    protected FilterView filterView;
    protected Component region;
    protected ImageSource source;

    public SourceView(int x, int y, int w, int h) {
        super(x, y, w, h);
        type = ComponentType.SOURCE;
        outItems.add("Output");

        region = new Component();
    }

    @Override
    protected Color buildBackgroundColor() {
        return COLOR_SOURCE;
    }

    @Override
    public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        switch (view.type) {
            case FILTER:
                filterView = ((FilterView) view);
                filterView.source = source;
                filterView.region = region;
                filterView.resetFilter();
                break;
            default:
                break;
        }
    }

    @Override
    public void unlink(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        switch (view.type) {
            case FILTER:
                FilterView filterView = ((FilterView) view);
                filterView.source = null;
                filterView.region = null;
                filterView.resetFilter();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isValidLink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

        if (to.type == ComponentType.FILTER) {
            return !fromItem.getInItem() && fromItem.getIndex() == 0 &&
                    toItem.getInItem() && toItem.getIndex() == 0;
        }

        return false;
    }

    public abstract void drawSource(Graphics g);
}
