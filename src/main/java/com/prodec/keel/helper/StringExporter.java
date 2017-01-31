package com.prodec.keel.helper;

import com.google.gson.Gson;
import com.prodec.keel.model.Pipeline;

public class StringExporter {

	public static void export(Pipeline pipeline) {
		Gson gson = JsonSerializerHelper.create();
		String json = gson.toJson(pipeline, Pipeline.class);
		System.out.println(json);
	}
	
}
