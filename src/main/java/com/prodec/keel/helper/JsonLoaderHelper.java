package com.prodec.keel.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.prodec.keel.model.Pipeline;

public class JsonLoaderHelper {

	public static Pipeline load(String path) {
		Gson gson = JsonSerializerHelper.create();
		
		FileReader input = null;
		try {
			input = new FileReader(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File not found: "+path);
		}
		
		return gson.fromJson(input, Pipeline.class);	
	}
	
}
