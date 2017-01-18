package com.prodec.keel.ui;

import br.com.etyllica.motion.feature.Component;
import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.FilterListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ClassifierView extends PipelineComponent implements FilterListener {

    protected FilterView filterInputView = null;
    protected FilterView filterOutputView = null;

    protected List<String> categories = new ArrayList<>();
    protected List<Component> results = new ArrayList<>();
    protected Map<String, List<Component>> classifications = new HashMap<>();
    protected Map<String, FilterView> outFilters = new HashMap<>();

    public ClassifierView(int x, int y, int w, int h) {
        super(x, y, w, h);
        type = ComponentType.CLASSIFIER;
        inItems.add("Input");
    }

    @Override
    protected Color buildBackgroundColor() {
        return COLOR_CLASSIFIER;
    }

    @Override
    public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        switch (view.type) {
            case FILTER:
                if (fromItem.inItem) {
                    filterInputView = ((FilterView) view);
                    filterInputView.link(this, toItem, fromItem);
                } else {
                    String classification = categories.get(toItem.index);
                    outFilters.put(classification, filterInputView);
                }

                break;
            case MODIFIER:
                view.link(this, toItem, fromItem);
                resetFilter();
                break;
            default:
                break;
        }
    }

    @Override
    public void unlink(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        switch (view.type) {
            case FILTER:
                if (fromItem.inItem) {
                    filterInputView.unlink(this, fromItem, toItem);
                }
                break;
            case MODIFIER:
                view.unlink(this, toItem, fromItem);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isValidLink(PipelineComponentItem fromItem,
                               PipelineComponent to, PipelineComponentItem toItem) {

        if (to.type == ComponentType.FILTER) {
            if (!fromItem.inItem && toItem.inItem && toItem.index == 0) {
                return true;
            }

            return to.isValidLink(toItem, this, fromItem);
        } else if (to.type == ComponentType.MODIFIER) {
            return to.isValidLink(toItem, this, fromItem);
        }

        return false;
    }

    protected void addCategory(String category) {
        outItems.add(category);
        classifications.put(category, new ArrayList<>());
        categories.add(category);
    }

    @Override
    public void setResults(List<Component> results) {
        this.results.clear();
        this.results.addAll(results);

        classify(this.results);
        propagate(this.results);
    }

    public abstract void classify(List<Component> results);

    public void propagate(List<Component> results) {

    }

    public List<Component> getResultsByIndex(int index) {
        String category = categories.get(index);
        List<Component> results = classifications.get(category);

        return results;
    }

    public void resetFilter() {
        if (filterInputView != null) {
            filterInputView.resetFilter();
        }
    }
}
