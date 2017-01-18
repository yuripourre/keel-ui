package com.prodec.keel.ui;

import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.feature.Component;
import br.com.etyllica.motion.filter.TrackingFilter;
import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.FilterListener;

import java.awt.*;
import java.util.List;

public abstract class FilterView extends PipelineComponent {

    //TODO REMOVE, USE SOURCE_VIEW INSTEAD
    public BufferedImageSource source;
    public Component component;

    private FilterListener listener;

    private DrawerView drawerView;
    private ValidationView validationView;
    protected TrackingFilter filter;

    public FilterView(int x, int y, int w, int h) {
        super(x, y, w, h);
        type = ComponentType.FILTER;
        inItems.add("Feature");
        inItems.add("Validation");
        inItems.add("Drawer");

        outItems.add("Result");
    }

    @Override
    protected Color buildBackgroundColor() {
        return COLOR_FILTER;
    }

    @Override
    public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        if (view.type == ComponentType.VALIDATION) {
            validationView = (ValidationView) view;
            validationView.filterView = this;
            filter.clearValidations();
            addValidation(validationView);
        } else if (view.type == ComponentType.DRAWER) {
            drawerView = (DrawerView) view;
        } else if (view.type == ComponentType.CLASSIFIER) {
            ClassifierView classifierView = (ClassifierView) view;
            listener = classifierView;
            classifierView.filterInputView = this;
            resetFilter();
        } else if (view.type == ComponentType.MODIFIER) {
            ModifierView modifierView = (ModifierView) view;
            listener = modifierView;
        }
    }

    @Override
    public void unlink(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        if (view.type == ComponentType.VALIDATION) {
            validationView = (ValidationView) view;
            validationView.filterView = null;
            filter.clearValidations();
        } else if (view.type == ComponentType.DRAWER) {
            drawerView.clear();
            drawerView = null;
        } else if (view.type == ComponentType.CLASSIFIER ) {
            ClassifierView classifierView = (ClassifierView) view;
            classifierView.filterInputView = null;
            listener = null;
        } else if (view.type == ComponentType.MODIFIER ) {
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
    public boolean isValidLink(PipelineComponentItem fromItem,
                               PipelineComponent to, PipelineComponentItem toItem) {

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
        List<Component> results = filter.filter(source, component);
        System.out.println(results.size());

        //TODO Get filterView by source
        if (drawerView != null) {
            drawerView.setResults(results);
        }

        if (listener != null) {
            listener.setResults(results);
        }
    }

}
