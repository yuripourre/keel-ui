package com.prodec.keel.serialization;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.etyllica.util.PathHelper;

import com.google.gson.Gson;
import com.prodec.keel.application.FilterViewApplication;
import com.prodec.keel.helper.JsonSerializerHelper;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.validation.MaxDimensionValidationView;

public class SerializerTest {

	Gson gson;
	
	@Before
	public void setUp() {
		gson = JsonSerializerHelper.create();
	}
	
	@Test
	public void testPipelineComponentSerialization() {
		MaxDimensionValidationView validationView = new MaxDimensionValidationView(200,30);
		
		String json = gson.toJson(validationView, PipelineComponent.class);
		Assert.assertEquals(300, json.length());
	}
	
	@Test
	public void testPipelineSerialization() {
		Pipeline pipeline = new Pipeline();
		FilterViewApplication.buildPipeline(pipeline);
		
		String json = gson.toJson(pipeline, Pipeline.class);
		Assert.assertEquals(2410, json.length());
	}
	
	@Test
	public void testPipelineDeserialization() throws FileNotFoundException {
		String path = PathHelper.currentDirectory()+"../assets/test/";
		path += "sample.kel";
		
		FileReader input = new FileReader(new File(path));
						
		Pipeline pipeline = gson.fromJson(input, Pipeline.class);
		Assert.assertEquals(8, pipeline.getComponents().size());
		Assert.assertEquals(3, pipeline.getLinks().size());
	}
	
}
