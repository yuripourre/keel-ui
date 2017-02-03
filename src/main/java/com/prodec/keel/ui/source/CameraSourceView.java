package com.prodec.keel.ui.source;

import java.awt.image.BufferedImage;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.camera.Camera;
import br.com.etyllica.motion.camera.CameraSarxosWebcam;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.feature.Component;

import com.prodec.keel.model.attribute.PathAttribute;
import com.prodec.keel.ui.SourceView;

public class CameraSourceView extends SourceView {

    PathAttribute pathAttribute;

    private Camera cam;
    private BufferedImage image;
    private String path = "";

    private boolean cameraFound = true;

    public CameraSourceView() {
		this(0, 0);
	}
    
    public CameraSourceView(int x, int y) {
        super(x, y, VIEW_WIDTH, 90);
        this.className = CameraSourceView.class.getName();
        
        title = "Camera Source";

        try {
            cam = new CameraSarxosWebcam();
            source = new BufferedImageSource();
            regionAttribute.getRegion().setBounds(0, 0, cam.getBufferedImage().getWidth(), cam.getBufferedImage().getHeight());
        } catch (NullPointerException e) {
            title = "No Camera";
            cameraFound = false;
            regionAttribute.getRegion().setBounds(0, 0, 0, 0);
        }

        pathAttribute = new PathAttribute("Path");
        addAttribute(pathAttribute);
    }

    @Override
    public void update(long now) {
        image = cam.getBufferedImage();
        ((BufferedImageSource) source).setImage(image);
        updateFilter();
    }

	@Override
    public void draw(Graphics g) {
        if (!cameraFound) {
           g.setAlpha(20);
        }

        super.draw(g);

        if (!cameraFound) {
            g.resetAlpha();
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;

        image = ImageLoader.getInstance().getImage("test1.jpg");
        source = new BufferedImageSource(image);

        regionAttribute.getRegion().setBounds(0, 0, image.getWidth(), image.getHeight());
    }

    public Component getRegion() {
        return regionAttribute.getRegion();
    }

    public void setRegion(Component region) {
        regionAttribute.setRegion(region);
    }

    @Override
    public void drawSource(Graphics g) {
        g.drawImage(image, 0, 0);
    }
}
