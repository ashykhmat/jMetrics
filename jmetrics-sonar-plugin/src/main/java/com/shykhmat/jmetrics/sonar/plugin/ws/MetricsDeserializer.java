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
		metrics.setCyclomaticComplexity(getSafetyIntValue(node, "cyclomaticComplexity"));
		metrics.setLinesOfCode(getSafetyIntValue(node, "linesOfCode"));
		metrics.setMaintainabilityIndex(getSafetyDoubleValue(node, "maintainabilityIndex"));
		return metrics;
	}

	private HalsteadComplexityMetrics deserializeHalsteadMetrics(JsonNode node) {
		double programLength = getSafetyDoubleValue(node, "programLength");
		double programVocabulary = getSafetyDoubleValue(node, "programVocabulary");
		double estimatedLength = getSafetyDoubleValue(node, "estimatedLength");
		double purityRatio = getSafetyDoubleValue(node, "purityRatio");
		double volume = getSafetyDoubleValue(node, "volume");
		double difficulty = getSafetyDoubleValue(node, "difficulty");
		double programEffort = getSafetyDoubleValue(node, "programEffort");
		double programmingTime = getSafetyDoubleValue(node, "programmingTime");
		double n1 = getSafetyDoubleValue(node, "n1");
		double n2 = getSafetyDoubleValue(node, "n2");
		double N1 = getSafetyDoubleValue(node, "N1");
		double N2 = getSafetyDoubleValue(node, "N2");
		HalsteadComplexityMetrics halsteadMetrics = new HalsteadComplexityMetrics(programLength, programVocabulary,
				estimatedLength, purityRatio, volume, difficulty, programEffort, programmingTime, n1, n2, N1, N2, null,
				null);
		return halsteadMetrics;
	}

	private double getSafetyDoubleValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode == null) {
			return 0.;
		} else {
			return fieldNode.asDouble(0.);
		}
	}

	private int getSafetyIntValue(JsonNode node, String fieldName) {
		JsonNode fieldNode = node.get(fieldName);
		if (fieldNode == null) {
			return 0;
		} else {
			return fieldNode.asInt(0);
		}
	}
}
