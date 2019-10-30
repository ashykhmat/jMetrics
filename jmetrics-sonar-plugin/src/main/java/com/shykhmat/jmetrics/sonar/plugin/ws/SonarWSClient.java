package com.shykhmat.jmetrics.sonar.plugin.ws;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.CoreProperties;
import org.sonar.api.config.Configuration;
import org.sonarqube.ws.Measures.ComponentWsResponse;
import org.sonarqube.ws.Measures.Measure;
import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import org.sonarqube.ws.client.measures.ComponentRequest;

import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Class that access SonarQube via provided REST API.
 */
public class SonarWSClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(SonarWSClient.class);
	private final WsClient wsClient;

	public SonarWSClient(Configuration configuration) {
		LOGGER.debug("Initializing Sonar REST Client.");
		HttpConnector httpConnector = HttpConnector.newBuilder().url(getBaseUrl(configuration))
				.credentials(configuration.get(CoreProperties.LOGIN).orElse(null),
						configuration.get(CoreProperties.PASSWORD).orElse(null))
				.build();
		wsClient = WsClientFactories.getDefault().newClient(httpConnector);
	}

	public ProjectReport getPreviousProjectReport(String component) {
		ComponentWsResponse response = getComponentMeasures(component, Collections.singletonList("jmetrics"));
		if (response.hasComponent()) {
			Optional<Measure> measure = response.getComponent().getMeasuresList().stream().findFirst();
			if (measure.isPresent()) {
				return JsonConverter.toObject(measure.get().getValue(), ProjectReport.class);
			}
		}
		return null;
	}

	private ComponentWsResponse getComponentMeasures(String component, List<String> metricKeys) {
		ComponentRequest request = new ComponentRequest();
		request.setComponent(component);
		request.setMetricKeys(metricKeys);
		return wsClient.measures().component(request);
	}

	private String getBaseUrl(Configuration configuration) {
		String baseUrl = configuration.hasKey(CoreProperties.SERVER_BASE_URL)
				? configuration.get(CoreProperties.SERVER_BASE_URL).orElse(null)
				: configuration.get("sonar.host.url").orElse(null);

		if (baseUrl == null) {
			LOGGER.warn("No SonarQube host configured. Using default.");
			baseUrl = "http://localhost:9000";
		}
		if (!baseUrl.endsWith("/")) {
			baseUrl += "/";
		}
		return baseUrl;
	}
}
