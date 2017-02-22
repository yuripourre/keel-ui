
import br.com.etyllica.Etyllica;
import br.com.etyllica.core.context.Application;

import com.prodec.keel.application.FilterViewApplication;
import com.prodec.keel.application.FilterViewLoaderApplication;
import com.prodec.keel.tools.video.VideoExtractor;
import com.prodec.keel.tools.video.VideoMaskGenerator;

public class Tools extends Etyllica {

	private static final long serialVersionUID = 1L;

	public Tools() {
		//super(1820, 1024);
		super(1024, 900);
	}

	public static void main(String[] args) {
		Tools app = new Tools();
		app.setTitle("Keel - Tools");
		app.init();
	}
	
	@Override
	public Application startApplication() {
		initialSetup("../");
		//return new VideoExtractor(w,h);
		return new VideoMaskGenerator(w,h);
	}

}
