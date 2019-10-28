package com.shykhmat.jmetrics.sonar.plugin;

import org.sonar.api.Plugin;

import com.shykhmat.jmetrics.sonar.plugin.metric.JMetrics;
import com.shykhmat.jmetrics.sonar.plugin.page.JMetricsPageDefinition;
import com.shykhmat.jmetrics.sonar.plugin.sensor.JMetricsSensor;

/**
 * The entry point for SonarQube plug-in that contains a list of provided
 * extensions.
 */
public class JMetricsPlugin implements Plugin {

	@Override
	public void define(Context context) {
		context.addExtensions(JMetrics.class, JMetricsSensor.class, JMetricsPageDefinition.class);
	}
}
