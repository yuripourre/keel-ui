package com.prodec.keel.ui;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.motion.feature.Component;
import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.FilterListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DrawerView extends PipelineComponent implements FilterListener {

    protected List<Component> results = new ArrayList<Component>();

    public DrawerView(int x, int y, int w, int h) {
        super(x, y, w, h);
        type = ComponentType.DRAWER;
        inItems.add("Input");
        outItems.add("Next");
    }

    @Override
    protected Color buildBackgroundColor() {
        return COLOR_DRAWER;
    }

    @Override
    public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        switch (view.type) {
            case FILTER:
                FilterView filterView = ((FilterView) view);
                filterView.link(this, toItem, fromItem);
                break;
            case MODIFIER:
                ModifierView modifierView = ((ModifierView) view);
                modifierView.link(this, toItem, fromItem);
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
                filterView.unlink(this, fromItem, toItem);
                break;
            case CLASSIFIER:
                ClassifierView classifierView = ((ClassifierView) view);
                classifierView.unlink(this, fromItem, toItem);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isValidLink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

        if (to.type == ComponentType.FILTER) {
            return to.isValidLink(this, toItem, fromItem);
        } else if (to.type == ComponentType.CLASSIFIER) {
            return to.isValidLink(this, toItem, fromItem);
        } else if (to.type == ComponentType.MODIFIER) {
            return to.isValidLink(this, toItem, fromItem);
        }

        return false;
    }

    public void setResults(List<Component> results) {
        this.results.clear();
        this.results.addAll(results);
    }

    public abstract void drawResults(Graphics g);

    public void clear() {
        results.clear();
    }
}
