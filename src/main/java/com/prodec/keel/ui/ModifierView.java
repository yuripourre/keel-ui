package com.prodec.keel.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.FilterListener;

public abstract class ModifierView<IN, OUT> extends PipelineDataComponent implements FilterListener<IN> {

	DrawerView<IN> inputDrawerView;
    DrawerView<OUT> outputDrawerView;

    protected List<IN> results = new ArrayList<>();
    protected List<OUT> output = new ArrayList<>();

    public ModifierView(int x, int y, int w, int h) {
        super(x, y, w, h);
        type = ComponentType.MODIFIER;
        inputs.add("Component");
        inputs.add("Drawer");
        outputs.add("Component");
        outputs.add("Drawer");
    }

    @Override
    protected Color buildBackgroundColor() {
        return COLOR_MODIFIER;
    }

    @Override
    public void link(PipelineComponent view, PipelineComponentItem fromItem, PipelineComponentItem toItem) {

        switch (view.type) {
            case CLASSIFIER:
                ClassifierView<IN> classifierView = (ClassifierView<IN>) view;
                classifierView.linkModifier(this, toItem.index);
                break;
            case DRAWER:
            	if (fromItem.inItem) {
            		inputDrawerView = (DrawerView) view;
            		inputDrawerView.setResults(results);
            	} else {
            		outputDrawerView = (DrawerView) view;
                    outputDrawerView.setResults(output);	
            	}
                
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
                ClassifierView<IN> classifierView = (ClassifierView<IN>) view;
                classifierView.unlinkModifier(this, toItem.index);
                break;
            case DRAWER:
            	if(fromItem.inItem) {
            		if (inputDrawerView != null) {
            			inputDrawerView.clear();
            			inputDrawerView = null;
                    }
            	} else {
            		if (outputDrawerView != null) {
                        outputDrawerView.clear();
                        outputDrawerView = null;
                    }	
            	}                

                break;
            case FILTER:

                break;
            default:
                break;
        }
    }


    @Override
    public boolean isValidLink(PipelineComponent to, PipelineComponentItem fromItem, PipelineComponentItem toItem) {
        if (to.type == ComponentType.DRAWER) {
        	boolean inputDrawer = (fromItem.inItem && fromItem.index == 1 && toItem.inItem && toItem.index == 0);
        	boolean outputDrawer = (!fromItem.inItem && fromItem.index == 1 && toItem.inItem && toItem.index == 0);
            if (inputDrawer || outputDrawer) {
                return dataType == ((PipelineDataComponent)to).dataType;
            }
        } else if (to.type == ComponentType.CLASSIFIER) {
        	//Accept any output option
        	if (fromItem.inItem && fromItem.index == 0 && !toItem.inItem) {
        		return dataType == ((PipelineDataComponent)to).dataType;	
        	}
        } else if (to.type == ComponentType.FILTER) {
            return fromItem.inItem && fromItem.index == 0 && !toItem.inItem && toItem.index == 0;
        }
        return false;
    }

    @Override
    public void setResults(List<IN> results) {
        this.results.clear();
        this.results.addAll(results);

        propagateInput(this.results);
        modify(this.results);
        propagateOutput(this.output);
    }

    public abstract void modify(List<IN> results);

    public void propagateInput(List<IN> intput) {
    	if (inputDrawerView != null) {
        	inputDrawerView.setResults(results);
        }
    }
    
    public void propagateOutput(List<OUT> output) {
        if (outputDrawerView != null) {
            outputDrawerView.setResults(output);
        }
        //TODO If next != null
    }

    public void clear() {
        results.clear();
        propagateInput(results);
        propagateOutput(output);
    }
}
