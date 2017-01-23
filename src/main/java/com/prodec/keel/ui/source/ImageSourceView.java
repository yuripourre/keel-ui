package com.prodec.keel.ui.source;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.loader.image.ImageLoader;
import br.com.etyllica.motion.core.source.BufferedImageSource;
import com.prodec.keel.model.attribute.PathAttribute;
import com.prodec.keel.ui.SourceView;

import java.awt.image.BufferedImage;

public class ImageSourceView extends SourceView {

    PathAttribute pathAttribute;

    private BufferedImage image;
    private String path = "";

    public ImageSourceView(int x, int y) {
        super(x, y, VIEW_WIDTH, 90);
        title = "Image Source";

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
    }

    @Override
    public void drawSource(Graphics g) {
        g.drawImage(image, 0, 0);
    }
}
