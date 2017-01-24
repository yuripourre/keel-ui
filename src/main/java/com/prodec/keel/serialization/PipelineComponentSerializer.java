package com.prodec.keel.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.ui.PipelineComponent;

public class PipelineComponentSerializer implements JsonSerializer<PipelineComponent> {

	private static final String JSON_INPUTS = "inputs";
	private static final String JSON_OUTPUTS = "outputs";
	private static final String JSON_PARAMS = "params";
	
	@Override
	public JsonElement serialize(PipelineComponent component, Type type,
			JsonSerializationContext context) {

		JsonObject element = new JsonObject();
		
		serializeInputs(component, element);
		serializeOutputs(component, element);
		serializeParams(component, element, context);
		
		return element;
	}

	private void serializeInputs(PipelineComponent component, JsonObject element) {
		JsonArray array = new JsonArray();
		
		for (String input : component.getInputs()) {
			array.add(new JsonPrimitive(input));
		}
		
		element.add(JSON_INPUTS, array);
	}
	
	private void serializeOutputs(PipelineComponent component, JsonObject element) {
		JsonArray array = new JsonArray();
		
		for (String output : component.getOutputs()) {
			array.add(new JsonPrimitive(output));
		}
		
		element.add(JSON_OUTPUTS, array);
	}
	
	private void serializeParams(PipelineComponent component, JsonObject element, JsonSerializationContext context) {
		JsonArray array = new JsonArray();
		
		for (Attribute attribute : component.getAttributes().values()) {
			array.add(context.serialize(attribute, Attribute.class));
		}
		
		element.add(JSON_PARAMS, array);
	}
		
}
