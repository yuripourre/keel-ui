package com.prodec.keel.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.etyllica.motion.filter.color.ColorStrategy;
import br.com.etyllica.util.PathHelper;

import com.google.gson.Gson;
import com.prodec.keel.model.attribute.Attribute;
import com.prodec.keel.model.attribute.ColorPickerAttribute;
import com.prodec.keel.model.attribute.SliderAttribute;

public class AttributeSerializerTest {

	Gson gson;

	@Before
	public void setUp() {
		gson = JsonSerializerHelper.create();
	}

	@Test
	public void testSliderDeserialization() throws FileNotFoundException {
		String path = PathHelper.currentDirectory()+"../assets/test/";
		path += "max_validation_slider.kel";
		
		FileReader input = new FileReader(new File(path));
		
		Attribute attribute = gson.fromJson(input, Attribute.class);
		SliderAttribute slider = (SliderAttribute) attribute;
		Assert.assertEquals("Max Dimension", slider.getLabel());
		Assert.assertEquals(222, slider.getId());
		Assert.assertEquals(99, slider.getCurrentValue());
		Assert.assertEquals(0, slider.getMinValue());
		Assert.assertEquals(100, slider.getMaxValue());
	}
	
	@Test
	public void testColorPickerDeserialization() throws FileNotFoundException {
		String path = PathHelper.currentDirectory()+"../assets/test/";
		path += "color_picker.kel";
		
		FileReader input = new FileReader(new File(path));
		
		Attribute attribute = gson.fromJson(input, Attribute.class);
		ColorPickerAttribute colorPicker = (ColorPickerAttribute) attribute;
		Assert.assertEquals("Color", colorPicker.getLabel());
		Assert.assertEquals(16776960, colorPicker.getColor());
		Assert.assertEquals(0xFF, ColorStrategy.getRed(colorPicker.getColor()));
		Assert.assertEquals(0xFF, ColorStrategy.getGreen(colorPicker.getColor()));
		Assert.assertEquals(0x00, ColorStrategy.getBlue(colorPicker.getColor()));
	}
	
}
