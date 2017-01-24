package com.prodec.keel;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.prodec.keel.application.FilterViewApplication;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.serialization.JsonSerializerHelper;
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
		Assert.assertEquals(113, json.length());
	}
	
	@Test
	public void testPipelineSerialization() {
		Pipeline pipeline = new Pipeline();
		FilterViewApplication.buildPipeline(pipeline);
		
		String json = gson.toJson(pipeline, Pipeline.class);
		Assert.assertEquals(868, json.length());
	}

}
