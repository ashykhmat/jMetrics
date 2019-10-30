package com.shykhmat.jmetrics.sonar.plugin.ws;

import java.io.IOException;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.report.Metrics;

/**
 * Utility class that provides possibility to convert object from/to json.
 */
public final class JsonConverter {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonConverter.class);
	private static final ObjectMapper OBJECT_MAPPER = initObjectMapper();

	private static ObjectMapper initObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("SimpleModule", new Version(1, 0, 0, null));
		module.addDeserializer(Metrics.class, new MetricsDeserializer());
		objectMapper.registerModule(module);
		return objectMapper;
	}

	private JsonConverter() {
	}

	public static String toJson(Object object) {
		try {
			return OBJECT_MAPPER.writeValueAsString(object);
		} catch (IOException e) {
			LOGGER.error("Cannot convert object {} to json due to an error {}", object, e);
			return null;
		}
	}

	public static <T> T toObject(String json, Class<T> type) {
		try {
			return OBJECT_MAPPER.readValue(json, type);
		} catch (IOException e) {
			LOGGER.error("Cannot convert json {} to object of type {} due to an error {}", json, type, e);
			return null;
		}
	}

}
