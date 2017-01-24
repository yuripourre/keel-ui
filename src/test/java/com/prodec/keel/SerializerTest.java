package com.prodec.keel;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.etyllica.motion.filter.validation.MaxDimensionValidation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.serialization.AttributeSerializer;
import com.prodec.keel.serialization.PipelineComponentSerializer;
import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.filter.ColorFilterView;
import com.prodec.keel.ui.validation.MaxDimensionValidationView;

public class SerializerTest {

	Gson gson;
	
	@Before
	public void setUp() {		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(PipelineComponent.class, new PipelineComponentSerializer());
		builder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		gson = builder.create();
	}

	@Test
	public void testPipelineComponentSerialization() {
		MaxDimensionValidationView validationView = new MaxDimensionValidationView(200,30);
		
		String json = gson.toJson(validationView, PipelineComponent.class);
		
		Assert.assertEquals(106, json.length());
	}

}
