
import br.com.etyllica.Etyllica;
import br.com.etyllica.core.context.Application;

import com.prodec.keel.application.FilterViewApplication;

public class FilterPipeline extends Etyllica {

	private static final long serialVersionUID = 1L;

	public FilterPipeline() {
		//super(1820, 1024);
		super(900, 900);
	}

	public static void main(String[] args) {
		FilterPipeline app = new FilterPipeline();
		app.setTitle("Keel");
		app.init();
	}
	
	@Override
	public Application startApplication() {
		initialSetup("../");
		return new FilterViewApplication(w,h);
	}	

}
