package com.prodec.keel.serialization;

import java.lang.reflect.Type;

import br.com.etyllica.motion.feature.Component;
import br.com.etyllica.motion.filter.color.ColorStrategy;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.AttributeType;
import com.prodec.keel.model.attribute.ColorPickerAttribute;
import com.prodec.keel.model.attribute.PathAttribute;
import com.prodec.keel.model.attribute.RegionAttribute;
import com.prodec.keel.model.attribute.SliderAttribute;

public class AttributeSerializer implements JsonSerializer<Attribute>, JsonDeserializer<Attribute> {

	private static final String JSON_LABEL = "label";
	private static final String JSON_TYPE = "type";
	private static final String JSON_ID = "id";
	
	private static final String JSON_MIN = "min";
	private static final String JSON_MAX = "max";
	private static final String JSON_VALUE = "value";
	
	private static final String JSON_COLOR = "color";
	private static final String JSON_PATH = "path";
	private static final String JSON_REGION = "region";

	private static final String HEX_FORMAT = "%02X";
	private static final String REGION_SEPARATOR = ", ";
	
	@Override
	public JsonElement serialize(Attribute attribute, Type type,
			JsonSerializationContext context) {

		JsonObject element = new JsonObject();
		
		//element.add(JSON_LABEL, new JsonPrimitive(attribute.getLabel()));
		//element.add(JSON_TYPE, new JsonPrimitive(attribute.getType().name()));
		element.add(JSON_ID, new JsonPrimitive(attribute.getId()));
		serializeCustomParams(element, attribute);

		return element;
	}

	private void serializeCustomParams(JsonObject element, Attribute attribute) {

		switch (attribute.getType()) {

		case SLIDER:
			SliderAttribute slider = (SliderAttribute) attribute;
			
			element.add(JSON_VALUE, new JsonPrimitive(slider.getCurrentValue()));
			element.add(JSON_MIN, new JsonPrimitive(slider.getMinValue()));
			element.add(JSON_MAX, new JsonPrimitive(slider.getMaxValue()));
			break;
			
		case COLOR_PICKER:
			ColorPickerAttribute colorPicker = (ColorPickerAttribute) attribute;
			
			int rgb = colorPicker.getColor();
			
			String r = String.format(HEX_FORMAT, ColorStrategy.getRed(rgb));
			String g = String.format(HEX_FORMAT, ColorStrategy.getGreen(rgb));
			String b = String.format(HEX_FORMAT, ColorStrategy.getBlue(rgb));
			
			String color = "#" + r + g + b;
			
			element.add(JSON_COLOR, new JsonPrimitive(color));
			break;
		case PATH:
			PathAttribute path = (PathAttribute) attribute;
			element.add(JSON_PATH, new JsonPrimitive(path.getPath()));
			break;
		case REGION:
			RegionAttribute region = (RegionAttribute) attribute;
			
			String x = Integer.toString(region.getRegion().getX());
			String y = Integer.toString(region.getRegion().getY());
			String w = Integer.toString(region.getRegion().getW());
			String h = Integer.toString(region.getRegion().getH());
			
			String regionText = "(" + x + REGION_SEPARATOR + y + REGION_SEPARATOR + w + REGION_SEPARATOR + h + ")";
			element.add(JSON_REGION, new JsonPrimitive(regionText));
			break;
		default:
			break;
		}

	}

	@Override
	public Attribute deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject node = element.getAsJsonObject();
		
		String label = node.get(JSON_LABEL).getAsString();
		
		AttributeType t = AttributeType.valueOf(node.get(JSON_TYPE).getAsString());
		Attribute attribute = buildAttribute(node, label, t);
		
		int id = node.get(JSON_ID).getAsInt();
		attribute.setId(id);
		
		return attribute;
	}
	
	private Attribute buildAttribute(JsonObject node, String label, AttributeType type) {
		switch (type) {
		case COLOR_PICKER:
			ColorPickerAttribute colorPicker = new ColorPickerAttribute(label);
			updateAttribute(colorPicker, node);
			return colorPicker;
		case SLIDER:
			SliderAttribute slider = new SliderAttribute(label);
			updateAttribute(slider, node);
			return slider;
		case PATH:
			PathAttribute path = new PathAttribute(label);
			updateAttribute(path, node);
			return path;
		case REGION:
			RegionAttribute region = new RegionAttribute(label);
			updateAttribute(region, node);
			return region;

		default:
			return null;
		}
	}

	public static void updateAttribute(Attribute attribute, JsonObject node) {
		switch (attribute.getType()) {
		case COLOR_PICKER:
			ColorPickerAttribute colorPicker = (ColorPickerAttribute) attribute;
			
			String colorText = node.get(JSON_COLOR).getAsString();
			int r = Integer.parseInt(colorText.substring(1, 3), 16) << 16;
			int g = Integer.parseInt(colorText.substring(3, 5), 16) << 8;
			int b = Integer.parseInt(colorText.substring(5, 7), 16);
			
			int rgb = r + g + b;
			colorPicker.setColor(rgb);
			break;
		case SLIDER:
			SliderAttribute slider = (SliderAttribute) attribute;
			slider.setMinValue(node.get(JSON_MIN).getAsInt());
			slider.setMaxValue(node.get(JSON_MAX).getAsInt());
			slider.setCurrentValue(node.get(JSON_VALUE).getAsInt());
			break;
		case PATH:
			PathAttribute path = (PathAttribute) attribute;
			path.setPath(node.get(JSON_PATH).getAsString());
			break;
		case REGION:
			RegionAttribute region = (RegionAttribute) attribute;
			String regionText = node.get(JSON_REGION).getAsString();			
			region.setRegion(buildComponent(regionText));
			break;
		case UNKNOWN:
			break;
		}
	}
	
	private static Component buildComponent(String regionText) {
		String region = regionText.substring(1, regionText.length() - 1);
		String[] parts = region.split(REGION_SEPARATOR);
		
		int x = Integer.parseInt(parts[0]);
		int y = Integer.parseInt(parts[1]);
		int w = Integer.parseInt(parts[2]);
		int h = Integer.parseInt(parts[3]);
		
		return new Component(x, y, w, h);
	}

}
