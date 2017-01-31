package com.prodec.keel.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.core.graphics.Graphics;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.FilterListener;

public abstract class DrawerView<T> extends PipelineDataComponent implements FilterListener<T> {

    protected DrawerView<T> previous = null;
    protected DrawerView<T> next = null;
    protected List<T> results = new ArrayList<T>();

    public DrawerView(int x, int y, int w, int h) {
        super(x, y, w, h);
        type = ComponentType.DRAWER;
        inputs.add("Input");
        outputs.add("Next");
    }

    @Override
    protected Color buildBackgroundColor() {
        return COLOR_DRAWER;
    }

    @Override
    public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        switch (view.type) {
            case DRAWER:

                if (fromNext(fromItem, toItem)) {
                    previous = ((DrawerView) view);
                    previous.next = this;
                } else if (toNext(fromItem, toItem)) {
                    next = ((DrawerView) view);
                    next.previous = this;
                }
                propagateResults();

                break;
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

    private void propagateResults() {
        DrawerView<T> root = root();
        if (!root.results.isEmpty()) {
            if (root.next != null) {
                root.next.setResults(results);
            }
        }
    }

    private boolean fromNext(PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        return (fromItem.inItem && fromItem.index == 0 && !toItem.inItem && toItem.index == 0);
    }

    private boolean toNext(PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        return (!fromItem.inItem && fromItem.index == 0 && toItem.inItem && toItem.index == 0);
    }

    @Override
    public void unlink(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        switch (view.type) {
            case DRAWER:
                DrawerView it = (DrawerView) view;

                root().next.setResults(new ArrayList<T>());
                if (previous == it) {
                    previous = null;
                    it.next = null;
                } else if (next == it) {
                    next = null;
                    it.previous = null;
                }
                root().propagateResults();

                break;
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

    protected DrawerView<T> root() {
        if (previous != null) {
            return previous.root();
        } else {
            return this;
        }
    }

    @Override
    public boolean isValidLink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

        if (to.type == ComponentType.DRAWER) {
        	if (dataType != ((PipelineDataComponent)to).dataType) {
        		return false;
        	}
        	
        	boolean toNext = toNext(fromItem, toItem);
            boolean fromNext = fromNext(fromItem, toItem);

            boolean validRoot = next != to && previous != to;
            
            return validRoot && (toNext || fromNext);

        } else if (to.type == ComponentType.FILTER) {
            return to.isValidLink(this, toItem, fromItem);
        } else if (to.type == ComponentType.CLASSIFIER) {
            return to.isValidLink(this, toItem, fromItem);
        } else if (to.type == ComponentType.MODIFIER) {
            return to.isValidLink(this, toItem, fromItem);
        }

        return false;
    }

    public void setResults(List<T> results) {
        this.results.clear();
        this.results.addAll(results);

        DrawerView<T> next = this.next;
        while (next != null) {
            next.setResults(results);
            next = next.next;
        }
    }

    public abstract void drawResults(Graphics g);

    public void clear() {
        results.clear();
    }
}
