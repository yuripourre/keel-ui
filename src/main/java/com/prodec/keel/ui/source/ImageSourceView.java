package com.prodec.keel.ui.source;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import br.com.etyllica.motion.feature.Component;
import com.prodec.keel.ui.SourceView;

import java.awt.image.BufferedImage;

public class ImageSourceView extends SourceView {

    private BufferedImage image;
    private String path = "";

    public ImageSourceView(int x, int y) {
        super(x, y, VIEW_WIDTH, 90);
        title = "Image Source";
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);

        //Draw Attributes
        drawRegionAttribute(g, "Region", 0, region);
        drawFileDialogAttribute(g, "Path", 1, path);
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
