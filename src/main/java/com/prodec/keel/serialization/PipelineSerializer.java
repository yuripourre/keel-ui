package com.prodec.keel.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.model.PipelineLink;
import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.PipelineLinkView;

public class PipelineSerializer implements JsonSerializer<Pipeline> {
	
	private static final String JSON_COMPONENTS = "components";
	private static final String JSON_LINKS = "links";

	@Override
	public JsonElement serialize(Pipeline pipeline, Type type,
			JsonSerializationContext context) {

		JsonObject element = new JsonObject();
		
		serializeComponents(pipeline, context, element);
		serializeLinks(pipeline, context, element);
		
		return element;
	}

	private void serializeComponents(Pipeline pipeline,
			JsonSerializationContext context, JsonObject element) {
		JsonArray components = new JsonArray();
		
		for (PipelineComponent component : pipeline.getComponents()) {
			components.add(context.serialize(component, PipelineComponent.class));
		}
		
		element.add(JSON_COMPONENTS, components);
	}

	private void serializeLinks(Pipeline pipeline,
			JsonSerializationContext context, JsonObject element) {
		JsonArray links = new JsonArray();
		
		for (PipelineLinkView linkView : pipeline.getLinks()) {
			
			PipelineLink link = linkView.getLink();
			
			links.add(context.serialize(link, PipelineLink.class));
		}
		
		element.add(JSON_LINKS, links);
	}
}
