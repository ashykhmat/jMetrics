package com.shykhmat.jmetrics.sonar.plugin.ws;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.shykhmat.jmetrics.core.report.HalsteadComplexityMetrics;
import com.shykhmat.jmetrics.core.report.Metrics;
import com.shykhmat.jmetrics.sonar.plugin.metric.ComparedMetrics;

public class MetricsDeserializer extends JsonDeserializer<Metrics> {

	@Override
	public Metrics deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		ObjectCodec codec = parser.getCodec();
		JsonNode node = codec.readTree(parser);
		ComparedMetrics metrics = new ComparedMetrics();
		metrics.setHalsteadMetrics(deserializeHalsteadMetrics(node));
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

	private HalsteadComplexityMetrics deserializeHalsteadMetrics(JsonNode node) {
		double programLength = node.get("programLength").asDouble(0.);
		double programVocabulary = node.get("programVocabulary").asDouble(0.);
		double estimatedLength = node.get("estimatedLength").asDouble(0.);
		double purityRatio = node.get("purityRatio").asDouble(0.);
		double volume = node.get("volume").asDouble(0.);
		double difficulty = node.get("difficulty").asDouble(0.);
		double programEffort = node.get("programEffort").asDouble(0.);
		double programmingTime = node.get("programmingTime").asDouble(0.);
		double n1 = node.get("n1").asDouble(0.);
		double n2 = node.get("n2").asDouble(0.);
		double N1 = node.get("N1").asDouble(0.);
		double N2 = node.get("N2").asDouble(0.);
		HalsteadComplexityMetrics halsteadMetrics = new HalsteadComplexityMetrics(programLength, programVocabulary,
				estimatedLength, purityRatio, volume, difficulty, programEffort, programmingTime, n1, n2, N1, N2, null,
				null);
		return halsteadMetrics;
	}

}
