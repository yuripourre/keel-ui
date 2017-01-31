package com.prodec.keel.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.FilterListener;

public abstract class ClassifierView<T> extends PipelineDataComponent implements FilterListener<T> {

    protected FilterView filterInputView = null;

    protected List<String> categories = new ArrayList<>();
    protected List<T> results = new ArrayList<>();
    protected Map<String, List<T>> classifications = new HashMap<>();
    protected Map<String, ModifierView<T, ?>> receivers = new HashMap<>();

    public ClassifierView(int x, int y, int w, int h) {
        super(x, y, w, h);
        type = ComponentType.CLASSIFIER;
        inputs.add("Input");
    }

    @Override
    protected Color buildBackgroundColor() {
        return COLOR_CLASSIFIER;
    }

    @Override
    public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        switch (view.type) {
            case FILTER:
                clear();
                view.link(this, toItem, fromItem);
                break;
            case MODIFIER:
                ModifierView<T, ?> modifierView = (ModifierView) view;
                linkModifier(modifierView, fromItem.index);
                break;
            default:
                break;
        }
    }

    @Override
    public void unlink(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        switch (view.type) {
            case FILTER:
                filterInputView.unlink(this, fromItem, toItem);
                clear();
                break;
            case MODIFIER:
                //Remove modifier from filters
                ModifierView<T, ?> modifierView = (ModifierView) view;
                unlinkModifier(modifierView, fromItem.index);
                break;
            case CLASSIFIER:
                view.unlink(this, toItem, fromItem);
                break;
            default:
                break;
        }
    }

    public void linkModifier(ModifierView<T, ?> modifierView, int indexClass) {
        String classification = categories.get(indexClass);
        receivers.put(classification, modifierView);

        List<T> classifyResults = classifications.get(classification);
        modifierView.setResults(classifyResults);
    }

    public void unlinkModifier(ModifierView<T, ?> modifierView, int indexClass) {
        String classification = categories.get(indexClass);
        receivers.remove(classification);

        modifierView.clear();
    }

    @Override
    public boolean isValidLink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

        if (to.type == ComponentType.FILTER) {
            if (!fromItem.inItem && toItem.inItem && toItem.index == 0) {
                return true;
            }

            return to.isValidLink(this, toItem, fromItem);
        } else if (to.type == ComponentType.MODIFIER) {
            return to.isValidLink(this, toItem, fromItem);
        }

        return false;
    }

    protected void addCategory(String category) {
        outputs.add(category);
        classifications.put(category, new ArrayList<T>());
        categories.add(category);
    }

    @Override
    public void setResults(List<T> results) {
        this.results.clear();
        this.results.addAll(results);

        classify(this.results);
        propagate();
    }

    public abstract void classify(List<T> results);

    public void propagate() {
        for (String category : categories) {
            if (receivers.containsKey(category)) {
                ModifierView<T, ?> outFilter = receivers.get(category);
                outFilter.setResults(classifications.get(category));
            }
        }
    }

    public void clear() {
        results.clear();

        for (String category : categories) {
            classifications.put(category, new ArrayList<T>());
        }

        propagate();
    }

}
