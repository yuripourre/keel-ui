package com.prodec.keel.serialization;

import java.lang.reflect.Type;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.ui.PipelineComponent;

public class PipelineComponentSerializer implements JsonSerializer<PipelineComponent>, JsonDeserializer<PipelineComponent> {

	public static final String JSON_ID = "id";
	public static final String JSON_TYPE = "type";
	public static final String JSON_TITLE = "title";
	private static final String JSON_CLASS = "class";
	private static final String JSON_X = "x";
	private static final String JSON_Y = "y";
	
	private static final String JSON_PARAMS = "params";

	@Override
	public JsonElement serialize(PipelineComponent component, Type type,
			JsonSerializationContext context) {

		JsonObject element = new JsonObject();
		element.add(JSON_ID, new JsonPrimitive(component.getIndex()));

		element.add(JSON_TITLE, new JsonPrimitive(component.getTitle()));
		element.add(JSON_CLASS, new JsonPrimitive(component.getClassName()));
		element.add(JSON_X, new JsonPrimitive(component.getX()));
		element.add(JSON_Y, new JsonPrimitive(component.getY()));
		
		serializeParams(component, element, context);

		return element;
	}

	private void serializeParams(PipelineComponent component, JsonObject element, JsonSerializationContext context) {
		JsonArray array = new JsonArray();

		for (Attribute attribute : component.getAttributes().values()) {
			array.add(context.serialize(attribute, Attribute.class));
		}

		element.add(JSON_PARAMS, array);
	}

	@Override
	public PipelineComponent deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject node = element.getAsJsonObject();

		Set<Class<?>> registeredTypes = Register.registeredTypes();

		PipelineComponent component = null;
		try {
			component = deserializeComponent(node, registeredTypes);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return component;
	}

	private PipelineComponent deserializeComponent(JsonObject node, Set<Class<?>> registeredTypes) throws InstantiationException, IllegalAccessException {
		JsonElement element = node.get(JSON_CLASS);

		String className = element.getAsString();
		int x = node.get(JSON_X).getAsInt();
		int y = node.get(JSON_Y).getAsInt();
		int id = node.get(JSON_ID).getAsInt();

		for(Class<?> type : registeredTypes) {
			if (compare(type, className)) {
				PipelineComponent component = (PipelineComponent) type.newInstance();
				component.setIndex(id);
				component.setX(x);
				component.setY(y);
				
				JsonArray params = node.get(JSON_PARAMS).getAsJsonArray();
				
				for (int i = 0; i < params.size(); i++) {
					JsonObject attributeNode = params.get(i).getAsJsonObject();
					int attributeId = attributeNode.get(JSON_ID).getAsInt();
					Attribute attribute = component.getAttributes().get(attributeId);
					
					AttributeSerializer.updateAttribute(attribute, attributeNode);
					i++;
				}
				
				return component;
			}
		}

		return null;
	}

	private boolean compare(Class<?> a, String b) {
		return a.getName().equals(b);
	}

}
