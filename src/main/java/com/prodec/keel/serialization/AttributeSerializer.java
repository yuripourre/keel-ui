package com.prodec.keel.serialization;

import java.lang.reflect.Type;

import br.com.etyllica.motion.filter.color.ColorStrategy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.ColorPickerAttribute;
import com.prodec.keel.model.attribute.PathAttribute;
import com.prodec.keel.model.attribute.RegionAttribute;
import com.prodec.keel.model.attribute.SliderAttribute;

public class AttributeSerializer implements JsonSerializer<Attribute> {

	private static final String JSON_LABEL = "label";
	private static final String JSON_TYPE = "type";
	private static final String JSON_VALUE = "value";

	@Override
	public JsonElement serialize(Attribute attribute, Type type,
			JsonSerializationContext context) {

		JsonObject element = new JsonObject();
		element.add(JSON_LABEL, new JsonPrimitive(attribute.getLabel()));
		element.add(JSON_TYPE, new JsonPrimitive(attribute.getType().name()));
		element.add(JSON_VALUE, new JsonPrimitive(value(attribute)));

		return element;
	}

	private String value(Attribute attribute) {

		switch (attribute.getType()) {

		case SLIDER:
			SliderAttribute slider = (SliderAttribute) attribute;
			return Integer.toString(slider.getCurrentValue());
		case COLOR_PICKER:
			ColorPickerAttribute colorPicker = (ColorPickerAttribute) attribute;
			
			int rgb = colorPicker.getColor();
			
			int r = ColorStrategy.getRed(rgb);
			int g = ColorStrategy.getGreen(rgb);
			int b = ColorStrategy.getBlue(rgb);
			
			return "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
		case PATH:
			PathAttribute path = (PathAttribute) attribute;
			return path.getPath();
		case REGION:
			RegionAttribute region = (RegionAttribute) attribute;
			
			String x = Integer.toString(region.getRegion().getX());
			String y = Integer.toString(region.getRegion().getY());
			String w = Integer.toString(region.getRegion().getW());
			String h = Integer.toString(region.getRegion().getH());
			
			return "(" + x+", "+y+", "+w+", "+h+")";
		default:
			return "";
		}

	}

}
