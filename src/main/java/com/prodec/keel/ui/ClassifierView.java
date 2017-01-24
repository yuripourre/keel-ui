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

    protected List<String> categories = new ArrayList<>();
    protected List<Component> results = new ArrayList<>();
    protected Map<String, List<Component>> classifications = new HashMap<>();
    protected Map<String, ModifierView> receivers = new HashMap<>();

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
                ModifierView modifierView = (ModifierView) view;
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
                ModifierView modifierView = (ModifierView) view;
                unlinkModifier(modifierView, fromItem.index);
                break;
            case CLASSIFIER:
                view.unlink(this, toItem, fromItem);
                break;
            default:
                break;
        }
    }

    public void linkModifier(ModifierView modifierView, int indexClass) {
        String classification = categories.get(indexClass);
        receivers.put(classification, modifierView);

        List<Component> classifyResults = classifications.get(classification);
        modifierView.setResults(classifyResults);
    }

    public void unlinkModifier(ModifierView modifierView, int indexClass) {
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
        classifications.put(category, new ArrayList<Component>());
        categories.add(category);
    }

    @Override
    public void setResults(List<Component> results) {
        this.results.clear();
        this.results.addAll(results);

        classify(this.results);
        propagate();
    }

    public abstract void classify(List<Component> results);

    public void propagate() {
        for (String category : categories) {
            if (receivers.containsKey(category)) {
                ModifierView outFilter = receivers.get(category);
                outFilter.setResults(classifications.get(category));
            }
        }
    }

    public void clear() {
        results.clear();

        for (String category : categories) {
            classifications.put(category, new ArrayList<Component>());
        }

        propagate();
    }

}
