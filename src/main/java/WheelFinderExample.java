


import br.com.etyllica.Etyllica;
import br.com.etyllica.core.context.Application;

import com.prodec.keel.application.WheelFinderApplication;

public class WheelFinderExample extends Etyllica {

	private static final long serialVersionUID = 1L;

	public WheelFinderExample() {
		super(800, 480);
	}
	
	public static void main(String[] args) {
		WheelFinderExample example = new WheelFinderExample();
		example.init();
	}

	@Override
	public Application startApplication() {
		initialSetup("../");
		
		return new WheelFinderApplication(w, h);
	}	

}
