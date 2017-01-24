package com.prodec.keel.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prodec.keel.model.PipelineLink;
import com.prodec.keel.ui.PipelineComponent;
import com.prodec.keel.ui.PipelineComponentItem;

public class PipelineLinkSerializer implements JsonSerializer<PipelineLink> {
	
	private static final String JSON_FROM = "from";
	private static final String JSON_TO = "to";
	private static final String JSON_DIRECTION = "direction";
	private static final String JSON_OPTION = "option";

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
		
		link.add(PipelineComponentSerializer.JSON_ID, new JsonPrimitive(component.getId()));
		link.add(JSON_DIRECTION, new JsonPrimitive(direction(item)));
		link.add(JSON_OPTION, new JsonPrimitive(item.getIndex()));
		
		return link;
	}
	
	private String direction(PipelineComponentItem item) {
		if(item.getInItem()) {
			return "in";
		} else {
			return "out";
		}
	}
}
