package com.prodec.keel.tools.video;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.AWTHoldFrameGrab;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.util.PathHelper;

import com.prodec.keel.tools.video.helper.ScreenComparator;
import com.prodec.keel.tools.video.helper.ScreenGenerator;

public class VideoMaskGenerator extends Application {

	private int speed = 5;

	private boolean isGenerating;

	private int progress = 0;
	private int current = 0;

	private File file;

	public VideoMaskGenerator(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

	}

	int count = 0;
	@Override
	public void draw(Graphics g) {
		if (isGenerating) {
			String text = "Generating ("+current+"/"+progress+")";
			g.drawStringShadow(text, this);
		} else {
			g.drawStringShadow("Drop your files here", this);	
		}
	}

	private void generateImage(final File file) {
		isGenerating = true;

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					int frameNumber = 0;
					
					AWTHoldFrameGrab frameGrab = AWTHoldFrameGrab.createFrameGrab(file);
					BufferedImage frame = frameGrab.getFrame(frameNumber);
										
					int totalFrames = frameGrab.getTotalFrames();

					ScreenComparator comparator = new ScreenComparator(frame);

					int frames = totalFrames/speed;
					progress = frames;

					for (int i = 0; i < frames; i++) {
						current = i;
						comparator.compareScreens(ScreenGenerator.generateScreen(frameGrab, i*speed));
					}

					BufferedImage img = comparator.generateImage();

					String path = PathHelper.currentDirectory()+"../assets/test"+File.separator;
					String id = Long.toString(System.currentTimeMillis());
					String directory = path + id;
					System.out.println(directory);

					File file = new File(directory + ".jpg");
					ImageIO.write(img, "jpg", file);
					
					frameGrab.close();
					isGenerating = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JCodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}).start();

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
		generateImage(file);
	}

}
