package com.prodec.keel.ui;

import br.com.etyllica.motion.feature.Component;
import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.FilterListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ModifierView extends PipelineComponent implements FilterListener {

    DrawerView drawerView;

    protected List<Component> results = new ArrayList<>();
    protected List<Object> output = new ArrayList<>();

    public ModifierView(int x, int y, int w, int h) {
        super(x, y, w, h);
        type = ComponentType.MODIFIER;
        inItems.add("Input");
        inItems.add("Drawer");
        outItems.add("Output");
    }

    @Override
    protected Color buildBackgroundColor() {
        return COLOR_MODIFIER;
    }

    @Override
    public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

        switch (view.type) {
            case CLASSIFIER:
                ClassifierView classifierView = (ClassifierView) view;
                List<Component> classifyResults = classifierView.getResultsByIndex(toItem.index);
                setResults(classifyResults);

                if (drawerView != null) {
                    drawerView.setResults(results);
                }
                classifierView.resetFilter();
                break;
            case DRAWER:
                drawerView = (DrawerView) view;
                drawerView.setResults(results);
                break;
            case FILTER:
                FilterView filterView = (FilterView) view;
                filterView.link(view, toItem, fromItem);
                break;
            default:
                break;
        }
    }

    @Override
    public void unlink(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

        switch (view.type) {
            case CLASSIFIER:
                results.clear();
            case DRAWER:
                if (drawerView != null) {
                    drawerView.clear();
                    drawerView = null;
                }

                break;
            case FILTER:

                break;
            default:
                break;
        }
    }


    @Override
    public boolean isValidLink(PipelineComponentItem fromItem,
                               PipelineComponent to, PipelineComponentItem toItem) {
        if (to.type == ComponentType.DRAWER) {
            if (fromItem.inItem && fromItem.index == 1 && toItem.inItem && toItem.index == 0) {
                return true;
            }
        } else if (to.type == ComponentType.CLASSIFIER) {
            if (fromItem.inItem && fromItem.index == 0 && !toItem.inItem) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setResults(List<Component> results) {
        this.results.clear();
        this.results.addAll(results);

        modify(this.results);
        propagate(this.results);
    }

    public abstract void modify(List<Component> results);

    public void propagate(List<Component> results) {

    }

}
