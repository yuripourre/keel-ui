package com.prodec.keel.tools.video;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.AWTHoldFrameGrab;

import br.com.etyllica.awt.stroke.DashedStroke;
import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.Layer;
import br.com.etyllica.util.PathHelper;

public class VideoExtractor extends Application {

	private int speed = 200;
	
	private int totalFrames = 0;
	private int frameNumber = 0;
	private BufferedImage frame;

	private static final int NONE = -1;

	private int dx = NONE;
	private int dy = NONE;
	private int dw = 1;
	private int dh = 1;
	private boolean selection = false;

	private File file;
	private AWTHoldFrameGrab frameGrab;

	private final DashedStroke dash = new DashedStroke();

	private boolean shouldExport = false;

	public VideoExtractor(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(frame, 0, 0);
		if (shouldExport) {
			exportToFile(g.getBimg());
			return;
		}
		
		g.drawStringShadow("Speed: "+speed, 10, 70);
		g.drawStringShadow("Frame: "+frameNumber+" / "+totalFrames, 10, 90);

		drawSelection(g);
	}

	private void drawSelection(Graphics g) {
		if (dx > 0) {
			g.setColor(Color.BLACK);
			g.setStroke(dash);
			g.drawRect(getRectangle());	
		}
	}
	
	private Layer getRectangle() {
		int rx = dx;
		int ry = dy;
		int rw = dw;
		int rh = dh;

		if (dw < 0) {
			rx = dx + dw;
			rw = -dw;
		}
		if (dh < 0) {
			ry = dy + dh;
			rh = -dh;
		}
		return new Layer(rx, ry, rw, rh);
	}

	@Override
	public void updateMouse(PointerEvent event) {
		if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
			if (!selection) {
				dx = event.getX();
				dy = event.getY();
				dw = 1;
				dh = 1;
				selection = true;	
			}			
		} else if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
			selection = false;
		}

		if (selection) {
			dw = event.getX() - dx;
			dh = event.getY() - dy;
		}

		super.updateMouse(event);
	}


	@Override
	public void updateKeyboard(KeyEvent event) {
		if (event.isKeyDown(KeyEvent.VK_UP)) {
			speed+=5;
		}
		if (event.isKeyDown(KeyEvent.VK_DOWN)) {
			speed-=5;
		}
		if (event.isKeyDown(KeyEvent.VK_LEFT)) {
			frameNumber-=speed;
			updateFrame();
		}
		if (event.isKeyDown(KeyEvent.VK_RIGHT)) {
			frameNumber+=speed;
			updateFrame();
		}
		if (event.isKeyDown(KeyEvent.VK_ENTER)) {
			shouldExport = true;
		}
	}

	private void exportToFile(BufferedImage image) {

		Layer rect = getRectangle();
		
		BufferedImage img = new BufferedImage( 
				rect.getW(), rect.getH(), BufferedImage.TYPE_INT_RGB );

		for (int y = 0; y < rect.getH(); y++) {
			for (int x = 0; x < rect.getW(); x++) {
				img.setRGB(x, y, image.getRGB(rect.getX() + x, rect.getY() + y));
			}
		}

		try {
			String path = PathHelper.currentDirectory()+"export"+File.separator;
			String id = Long.toString(System.currentTimeMillis());
			String directory = path + id;
			System.out.println(directory);
			
			File file = new File(directory + ".jpg");
			ImageIO.write(img, "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			shouldExport = false;
		}
	}

	@Override
	public void dropFiles(int x, int y, List<File> files) {
		file = files.get(0);
		updateFrame();
	}

	private void updateFrame() {
		if (file == null) {
			return;
		}
		
		try {
			frameGrab = AWTHoldFrameGrab.createFrameGrab(file);
			totalFrames = frameGrab.getTotalFrames();
			frame = frameGrab.getFrame(frameNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JCodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		frameGrab.close();
	}
}
