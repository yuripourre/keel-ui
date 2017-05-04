package com.prodec.keel.application;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.util.PathHelper;

import com.prodec.keel.helper.JsonLoaderHelper;
import com.prodec.keel.helper.StringExporter;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.ui.source.ImageSourceView;

public class CarViewApplication extends Application {

    Pipeline pipeline;

    public CarViewApplication(int w, int h) {
        super(w, h);
    }

    @Override
    public void load() {
    	String carPath = "cars/1485968690020.jpg";
    	        
        pipeline = JsonLoaderHelper.load(PathHelper.currentDirectory()+"assets/filters/car.kel");
        
        ImageSourceView source = (ImageSourceView) pipeline.getComponent(0);
        source.setPath(carPath);
        
        pipeline.setDrawPosition(20,60);
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
