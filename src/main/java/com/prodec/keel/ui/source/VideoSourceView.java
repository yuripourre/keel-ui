package com.prodec.keel.ui.source;

import java.awt.image.BufferedImage;

import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.core.source.BufferedImageSource;

import com.prodec.keel.model.attribute.PathAttribute;
import com.prodec.keel.ui.SourceView;

public class VideoSourceView extends SourceView {

    PathAttribute pathAttribute;

    private BufferedImage image;
    private String path = "";

    public VideoSourceView() {
		this(0, 0);
	}
    
    public VideoSourceView(int x, int y) {
        super(x, y, VIEW_WIDTH, 90);
        this.className = VideoSourceView.class.getName();
        
        title = "Video Source";

        pathAttribute = new PathAttribute("Path");
        addAttribute(pathAttribute);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;

        image = ImageLoader.getInstance().getImage(path);
        source = new BufferedImageSource(image);

        regionAttribute.getRegion().setBounds(0, 0, image.getWidth(), image.getHeight());
        
        updateFilter();
    }

    @Override
    public void drawSource(Graphics g) {
        //g.drawImage(image, image.getWidth()/2, image.getHeight()/2);
    	g.drawImage(image, drawX, drawY);
    }
}
