package com.prodec.keel.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.model.PipelineLink;
import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.serialization.AttributeSerializer;
import com.prodec.keel.serialization.PipelineComponentSerializer;
import com.prodec.keel.serialization.PipelineLinkSerializer;
import com.prodec.keel.serialization.PipelineSerializer;
import com.prodec.keel.ui.PipelineComponent;

public class JsonSerializerHelper {

	public static Gson create() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.registerTypeAdapter(Attribute.class, new AttributeSerializer());
		builder.registerTypeAdapter(Pipeline.class, new PipelineSerializer());
		builder.registerTypeAdapter(PipelineComponent.class, new PipelineComponentSerializer());
		builder.registerTypeAdapter(PipelineLink.class, new PipelineLinkSerializer());
		
		return builder.create();
	}
	
}
