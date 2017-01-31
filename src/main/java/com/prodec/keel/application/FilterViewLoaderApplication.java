package com.prodec.keel.application;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.gui.theme.ThemeManager;
import br.com.etyllica.util.PathHelper;

import com.google.gson.Gson;
import com.prodec.keel.helper.JsonSerializerHelper;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.ui.classifier.SquareClassifierView;
import com.prodec.keel.ui.drawer.CenterDrawerView;
import com.prodec.keel.ui.drawer.RectDrawerView;
import com.prodec.keel.ui.filter.ColorFilterView;
import com.prodec.keel.ui.modifier.DummyModifierView;
import com.prodec.keel.ui.source.ImageSourceView;
import com.prodec.keel.ui.validation.MaxDimensionValidationView;
import com.prodec.keel.ui.validation.MinDimensionValidationView;

public class FilterViewLoaderApplication extends Application {

    Pipeline pipeline;

    public FilterViewLoaderApplication(int w, int h) {
        super(w, h);
    }

    @Override
    public void load() {
        pipeline = new Pipeline();
        
        setupUI();
    }
    
    public static void buildPipeline(Pipeline pipeline) {
    	pipeline.add(new ColorFilterView(20, 310));
        pipeline.add(new MaxDimensionValidationView(275, 310));
        pipeline.add(new MinDimensionValidationView(275, 410));
        pipeline.add(new RectDrawerView(20, 450));
        pipeline.add(new SquareClassifierView(20, 540));
        pipeline.add(new DummyModifierView(20, 650));
        pipeline.add(new CenterDrawerView(275, 650));
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
    		Gson gson = JsonSerializerHelper.create();
    		String json = gson.toJson(pipeline, Pipeline.class);
    		System.out.println(json);
    	}
    	
    }
    
    @Override
    public void dropFiles(int x, int y, List<File> files) {
    	
    	Gson gson = JsonSerializerHelper.create();
    	
    	for (File file : files) {
    		String path = file.getAbsolutePath();
    		
			try {
				FileReader input = new FileReader(new File(path));
				pipeline = gson.fromJson(input, Pipeline.class);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    

}
