package com.prodec.keel.ui.source;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.camera.Camera;
import br.com.etyllica.motion.camera.CameraSarxosWebcam;
import br.com.etyllica.motion.camera.CameraV4L4J;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.feature.Component;
import com.prodec.keel.ui.SourceView;

import java.awt.image.BufferedImage;

public class CameraSourceView extends SourceView {

    private Camera cam;
    private BufferedImage image;
    private String path = "";

    private boolean cameraFound = true;

    public CameraSourceView(int x, int y) {
        super(x, y, VIEW_WIDTH, 90);
        title = "Camera Source";

        try {
            cam = new CameraSarxosWebcam();
            source = new BufferedImageSource();
            region.setBounds(0, 0, cam.getBufferedImage().getWidth(), cam.getBufferedImage().getHeight());
        } catch (NullPointerException e) {
            title = "No Camera";
            cameraFound = false;
            region.setBounds(0, 0, 0, 0);
        }
    }

    public void update(long now) {
        image = cam.getBufferedImage();
        ((BufferedImageSource) source).setImage(image);
        filterView.resetFilter();
    }

    @Override
    public void draw(Graphics g) {
        if (!cameraFound) {
           g.setAlpha(20);
        }

        super.draw(g);

        //Draw Attributes
        drawRegionAttribute(g, "Region", 0, region);
        drawFileDialogAttribute(g, "Path", 1, path);

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

        region.setBounds(0, 0, image.getWidth(), image.getHeight());
    }

    public Component getRegion() {
        return region;
    }

    public void setRegion(Component region) {
        this.region = region;
    }

    @Override
    public void drawSource(Graphics g) {
        g.drawImage(image, 0, 0);
    }
}
