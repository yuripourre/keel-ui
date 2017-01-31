package com.prodec.keel.application;

import java.awt.Color;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.gui.theme.ThemeManager;

import com.google.gson.Gson;
import com.prodec.keel.helper.JsonSerializerHelper;
import com.prodec.keel.helper.StringExporter;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.ui.classifier.SquareClassifierView;
import com.prodec.keel.ui.drawer.CenterDrawerView;
import com.prodec.keel.ui.drawer.HullDrawerView;
import com.prodec.keel.ui.drawer.RectDrawerView;
import com.prodec.keel.ui.filter.ColorFilterView;
import com.prodec.keel.ui.filter.SubtractiveFilterView;
import com.prodec.keel.ui.modifier.ConvexHullModifierView;
import com.prodec.keel.ui.modifier.DummyModifierView;
import com.prodec.keel.ui.source.ImageSourceView;
import com.prodec.keel.ui.validation.MaxDimensionValidationView;
import com.prodec.keel.ui.validation.MinDimensionValidationView;

public class FilterViewApplication extends Application {

    Pipeline pipeline;

    public FilterViewApplication(int w, int h) {
        super(w, h);
    }

    @Override
    public void load() {
        ImageSourceView sourceView = new ImageSourceView(575, 410);
        sourceView.setPath("bonfim.png");
        sourceView.setPath("bonfim2.png");
        //sourceView.setPath("test1.jpg");

        //CameraSourceView cameraView = new CameraSourceView(575, 410);
        //pipeline.add(cameraView);
        pipeline = new Pipeline();

        pipeline.add(sourceView);
        buildPipeline(pipeline);
        
        setupUI();
    }
    
    public static void buildPipeline(Pipeline pipeline) {
    	pipeline.add(new ColorFilterView(20, 310));
        pipeline.add(new MaxDimensionValidationView(275, 310));
        pipeline.add(new MinDimensionValidationView(275, 410));
        pipeline.add(new RectDrawerView(20, 450));
        pipeline.add(new SquareClassifierView(20, 540));
        pipeline.add(new DummyModifierView(20, 650));
        pipeline.add(new ConvexHullModifierView(610, 540));
        pipeline.add(new HullDrawerView(610, 650));
        pipeline.add(new CenterDrawerView(275, 650));
        pipeline.add(new SubtractiveFilterView(-310, 310));
    }

    private void setupUI() {
        //ThemeManager.getInstance().getTheme().setTextColor(Color.WHITE);
        ThemeManager.getInstance().getTheme().setTextColor(Color.BLACK);
    }

    @Override
    public void draw(Graphics g) {
    	pipeline.draw(g);
    }

    @Override
    public void updateMouse(PointerEvent event) {
    	pipeline.updateMouse(event);
    }

    @Override
    public void updateKeyboard(KeyEvent event) {
    	pipeline.updateKeyboard(event);
    	
    	if(event.isKeyDown(KeyEvent.VK_ENTER)) {
    		StringExporter.export(pipeline);
    	}
    }

}
