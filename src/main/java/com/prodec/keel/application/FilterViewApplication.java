package com.prodec.keel.application;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.gui.theme.ThemeManager;
import com.prodec.keel.model.ComponentType;
import com.prodec.keel.model.Mode;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.model.PipelineLink;
import com.prodec.keel.ui.*;
import com.prodec.keel.ui.classifier.SquareClassifierView;
import com.prodec.keel.ui.drawer.CenterDrawerView;
import com.prodec.keel.ui.drawer.RectDrawerView;
import com.prodec.keel.ui.filter.ColorFilterView;
import com.prodec.keel.ui.modifier.DummyModifierView;
import com.prodec.keel.ui.source.CameraSourceView;
import com.prodec.keel.ui.source.ImageSourceView;
import com.prodec.keel.ui.validation.MaxDimensionValidationView;
import com.prodec.keel.ui.validation.MinDimensionValidationView;

import java.awt.*;

public class FilterViewApplication extends Application {

    private static Mode mode = Mode.NORMAL;

    PipelineLink currentLink = new PipelineLink();

    Pipeline pipeline;

    public FilterViewApplication(int w, int h) {
        super(w, h);
    }

    @Override
    public void load() {

        ImageSourceView sourceView = new ImageSourceView(575, 310);
        sourceView.setPath("test1.jpg");

        //CameraSourceView cameraView = new CameraSourceView(575, 410);
        //pipeline.add(cameraView);
        pipeline = new Pipeline();

        pipeline.add(sourceView);
        pipeline.add(new ColorFilterView(20, 310));
        pipeline.add(new MaxDimensionValidationView(275, 310));
        pipeline.add(new MinDimensionValidationView(275, 410));
        pipeline.add(new RectDrawerView(20, 450));
        pipeline.add(new SquareClassifierView(20, 540));
        pipeline.add(new DummyModifierView(20, 650));
        pipeline.add(new CenterDrawerView(275, 650));

        setupUI();
    }

    private void setupUI() {
        //ThemeManager.getInstance().getTheme().setTextColor(Color.WHITE);
        ThemeManager.getInstance().getTheme().setTextColor(Color.BLACK);
    }

    @Override
    public void draw(Graphics g) {

        g.setColor(Color.BLACK);
        for (PipelineLinkView link : pipeline.getLinks()) {
            link.drawLine(g);
        }

        for (PipelineComponent component : pipeline.getComponents()) {
            if (ComponentType.SOURCE == component.getType()) {
                ((SourceView) component).drawSource(g);
            }
            if (ComponentType.DRAWER == component.getType()) {
                ((DrawerView) component).drawResults(g);
            }
        }

        for (PipelineComponent component : pipeline.getComponents()) {
            component.draw(g);
        }

        for (PipelineLinkView link : pipeline.getLinks()) {
            link.drawJoints(g);
        }
    }

    @Override
    public void updateMouse(PointerEvent event) {
        for (PipelineComponent component : pipeline.getComponents()) {
            component.updateMouse(event);

            if (component.isMoving()) {
                mode = Mode.SELECTION;
                return;
            } else if (component.isRemoveLink()) {
                pipeline.removeLink(component);
                component.linkRemoved();
                return;
            }

            if (component.getSelectedItem() != PipelineComponent.INVALID_ITEM) {
                if (currentLink.getFromItem() == PipelineComponent.INVALID_ITEM) {
                    currentLink.setFrom(component);
                    mode = Mode.SELECTION;
                } else if (component != currentLink.getFrom()) {
                    currentLink.setTo(component);
                }
            }
        }

        if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
            linkComponents();
            resetLink();
        }
    }

    private void resetLink() {
        currentLink.reset();
        mode = Mode.NORMAL;
    }

    public static Mode getMode() {
        return mode;
    }

    private void linkComponents() {
        if (currentLink.isValid()) {
            currentLink.link();
            pipeline.add(new PipelineLinkView(currentLink));
            currentLink = new PipelineLink();
        }
    }

    @Override
    public void updateKeyboard(KeyEvent event) {
        for (PipelineComponent component : pipeline.getComponents()) {
            component.updateKeyboard(event);
        }
    }

}
