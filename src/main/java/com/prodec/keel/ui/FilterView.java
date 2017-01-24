package com.prodec.keel.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.motion.core.source.ImageSource;
import br.com.etyllica.motion.feature.Component;
import br.com.etyllica.motion.filter.TrackingFilter;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.FilterListener;

public abstract class FilterView extends PipelineComponent {

    public ImageSource source;
    public Component region;

    private FilterListener listener;

    private DrawerView drawerView;
    private ValidationView validationView;
    protected TrackingFilter filter;
    
    protected List<Component> results = new ArrayList<Component>();
    
    public FilterView(int x, int y, int w, int h) {
        super(x, y, w, h);
        type = ComponentType.FILTER;
        inputs.add("Source");
        inputs.add("Validation");
        inputs.add("Drawer");

        outputs.add("Output");
    }

    @Override
    protected Color buildBackgroundColor() {
        return COLOR_FILTER;
    }

    @Override
    public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        if (view.type == ComponentType.SOURCE) {
            view.link(this, toItem, fromItem);
        } else if (view.type == ComponentType.VALIDATION) {
            validationView = (ValidationView) view;
            validationView.filterView = this;
            filter.clearValidations();
            addValidation(validationView);
            resetFilter();
        } else if (view.type == ComponentType.DRAWER) {
            drawerView = (DrawerView) view;
            propagate(results);
        } else if (view.type == ComponentType.CLASSIFIER) {
            ClassifierView classifierView = (ClassifierView) view;
            listener = classifierView;
            classifierView.filterInputView = this;
            propagate(results);
        } else if (view.type == ComponentType.MODIFIER) {
            ModifierView modifierView = (ModifierView) view;
            listener = modifierView;
            resetFilter();
        }
    }

    @Override
    public void unlink(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        if (view.type == ComponentType.SOURCE) {
            view.unlink(this, toItem, fromItem);
        } else if (view.type == ComponentType.VALIDATION) {
            validationView = (ValidationView) view;
            validationView.filterView = null;
            filter.clearValidations();
            resetFilter();
        } else if (view.type == ComponentType.DRAWER) {
            drawerView.clear();
            drawerView = null;
        } else if (view.type == ComponentType.CLASSIFIER) {
            ClassifierView classifierView = (ClassifierView) view;
            classifierView.filterInputView = null;
            classifierView.clear();
            listener = null;
        } else if (view.type == ComponentType.MODIFIER) {
            listener = null;
        }
    }

    private void addValidation(ValidationView validationView) {
        filter.addValidation(validationView.getValidation());
        if (validationView.getNext() != null) {
            addValidation(validationView.getNext());
        }
    }

    @Override
    public boolean isValidLink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

        if (to.type == ComponentType.SOURCE) {
            return to.isValidLink(this, toItem, fromItem);
        }

        if (!fromItem.getInItem() && fromItem.getIndex() == 0) {
            return to.type == ComponentType.CLASSIFIER && toItem.getInItem() && toItem.getIndex() == 0;
        }

        if (fromItem.getInItem() && fromItem.getIndex() == 1) {
            return to.type == ComponentType.VALIDATION && toItem.getInItem() && toItem.getIndex() == 0;
        }

        if (fromItem.getInItem() && fromItem.getIndex() == 2) {
            return to.type == ComponentType.DRAWER && toItem.getInItem() && toItem.getIndex() == 0;
        }

        return false;
    }

    public void resetFilter() {
        System.out.println("Reset Filter");

        if (source != null) {
        	results.clear();
            results.addAll(filter.filter(source, region));
            propagate(results);
        } else {
            if (drawerView != null) {
                drawerView.clear();
            }

            if (listener != null) {
                listener.clear();
            }
        }
    }
    
    private void propagate(List<Component> results) {
    	if (drawerView != null) {
            drawerView.setResults(results);
        }

        if (listener != null) {
            listener.setResults(results);
        }
    }

}
