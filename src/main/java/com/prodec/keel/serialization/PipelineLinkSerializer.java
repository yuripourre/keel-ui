package com.prodec.keel.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prodec.keel.model.Pipeline;
import com.prodec.keel.model.PipelineLink;
import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.PipelineComponentItem;
import com.prodec.keel.ui.PipelineLinkView;

public class PipelineLinkSerializer implements JsonSerializer<PipelineLink> {
	
	public static final String JSON_FROM = "from";
	public static final String JSON_TO = "to";
	public static final String JSON_DIRECTION = "direction";
	public static final String JSON_OPTION = "option";
	
	public static final String JSON_IN = "in";
	public static final String JSON_OUT = "out";

	@Override
	public JsonElement serialize(PipelineLink link, Type type,
			JsonSerializationContext context) {

		JsonObject element = new JsonObject();
		
		element.add(JSON_FROM, serializeLinkedComponent(link.getFrom(), link.getFromItem()));
		element.add(JSON_TO, serializeLinkedComponent(link.getTo(), link.getToItem()));
		
		return element;
	}
	
	private JsonObject serializeLinkedComponent(PipelineComponent component, PipelineComponentItem item) {
		JsonObject link = new JsonObject();
		
		link.add(PipelineComponentSerializer.JSON_ID, new JsonPrimitive(component.getIndex()));
		link.add(JSON_DIRECTION, new JsonPrimitive(direction(item)));
		link.add(JSON_OPTION, new JsonPrimitive(item.getIndex()));
		
		return link;
	}
	
	private String direction(PipelineComponentItem item) {
		if(item.getInItem()) {
			return JSON_IN;
		} else {
			return JSON_OUT;
		}
	}
	
	public static void deserialize(Pipeline pipeline, JsonArray linksNode) {
		for (int i = 0; i < linksNode.size(); i++) {
			JsonObject node = linksNode.get(i).getAsJsonObject();
			
			PipelineLink link = new PipelineLink();
			
			JsonObject fromNode = node.get(JSON_FROM).getAsJsonObject();
			link.setFromItem(buildComponentItem(fromNode));
			link.setFrom(buildComponent(pipeline, fromNode));
			
			JsonObject toNode = node.get(JSON_TO).getAsJsonObject();
			link.setToItem(buildComponentItem(toNode));
			link.setTo(buildComponent(pipeline, toNode));
			
			link.link();
			pipeline.add(new PipelineLinkView(link));
		}
	}
	
	private static PipelineComponentItem buildComponentItem(JsonObject componentNode) {
		int option = componentNode.get(JSON_OPTION).getAsInt();
		boolean inItem = "in".equals(componentNode.get(JSON_DIRECTION).getAsString());
		
		return new PipelineComponentItem(option, inItem);
	}
	
	private static PipelineComponent buildComponent(Pipeline pipeline, JsonObject componentNode) {
		int id = componentNode.get(PipelineComponentSerializer.JSON_ID).getAsInt();
		return pipeline.getComponent(id);
	}

}
