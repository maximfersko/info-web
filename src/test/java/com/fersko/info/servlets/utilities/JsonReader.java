package com.fersko.info.servlets.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonReader {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static <T> T readJsonFromFile(String filePath, Class<T> valueType) throws IOException {
		InputStream inputStream = JsonReader.class.getResourceAsStream(filePath);
		return objectMapper.readValue(inputStream, valueType);
	}

}
