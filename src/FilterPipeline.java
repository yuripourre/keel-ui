
import br.com.etyllica.EtyllicaApplet;
import br.com.etyllica.core.context.Application;

import com.prodec.keel.application.FilterViewApplication;

public class FilterPipeline extends EtyllicaApplet {

	private static final long serialVersionUID = 1L;

	public FilterPipeline() {
		//super(1820, 1024);
		super(600, 1024);
	}

	@Override
	public Application startApplication() {
		initialSetup("../");
		return new FilterViewApplication(w,h);
	}	

}
