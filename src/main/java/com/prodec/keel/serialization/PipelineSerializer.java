package com.prodec.keel.serialization;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.model.PipelineLink;
import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.PipelineLinkView;

public class PipelineSerializer implements JsonSerializer<Pipeline>, JsonDeserializer<Pipeline> {
	
	private static final String VERSION = "1.0";
	
	private static final String JSON_COMPONENTS = "components";
	private static final String JSON_LINKS = "links";
	private static final String JSON_VERSION = "version";
	private static final String JSON_X = "x";
	private static final String JSON_Y = "y";

	@Override
	public JsonElement serialize(Pipeline pipeline, Type type,
			JsonSerializationContext context) {

		JsonObject element = new JsonObject();
		
		element.add(JSON_VERSION, new JsonPrimitive(VERSION));
		element.add(JSON_X, new JsonPrimitive(pipeline.getX()));
		element.add(JSON_Y, new JsonPrimitive(pipeline.getY()));
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

	@Override
	public Pipeline deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject node = element.getAsJsonObject();
		
		Type t = new TypeToken<List<PipelineComponent>>() {}.getType();
		List<PipelineComponent> components = context.deserialize(node.get(JSON_COMPONENTS), t);
		
		int x = node.get(JSON_X).getAsInt();
		int y = node.get(JSON_Y).getAsInt();
		
		Pipeline pipeline = new Pipeline(x, y);
		pipeline.setComponents(components);
		
		JsonArray linksNode = node.get(JSON_LINKS).getAsJsonArray();
		PipelineLinkSerializer.deserialize(pipeline, linksNode);
				
		return pipeline;
	}

}
