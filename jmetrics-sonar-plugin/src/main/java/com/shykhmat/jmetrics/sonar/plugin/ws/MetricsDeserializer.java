package com.shykhmat.jmetrics.sonar.plugin.ws;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.shykhmat.jmetrics.core.report.Metrics;
import com.shykhmat.jmetrics.sonar.plugin.metric.ComparedMetrics;

public class MetricsDeserializer extends JsonDeserializer<Metrics> {

	@Override
	public Metrics deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		ObjectCodec codec = parser.getCodec();
		JsonNode node = codec.readTree(parser);
		ComparedMetrics metrics = new ComparedMetrics();
		metrics.setHalsteadVolume(node.get("halsteadVolume").asDouble(0.));
		metrics.setCyclomaticComplexity(node.get("cyclomaticComplexity").asInt(0));
		metrics.setLinesOfCode(node.get("linesOfCode").asInt(0));
		metrics.setMaintainabilityIndex(node.get("maintainabilityIndex").asDouble(0.));
		metrics.setCyclomaticComplexityStatus(
				ComparedMetrics.Status.valueOf(node.get("cyclomaticComplexityStatus").asText()));
		metrics.setHalsteadVolumeStatus(ComparedMetrics.Status.valueOf(node.get("halsteadVolumeStatus").asText()));
		metrics.setLinesOfCodeStatus(ComparedMetrics.Status.valueOf(node.get("linesOfCodeStatus").asText()));
		metrics.setMaintainabilityIndexStatus(
				ComparedMetrics.Status.valueOf(node.get("maintainabilityIndexStatus").asText()));
		return metrics;
	}

}
