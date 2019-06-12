package com.shykhmat.jmetrics.sonar.plugin;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.Extension;
import org.sonar.api.SonarPlugin;

import com.shykhmat.jmetrics.sonar.plugin.metric.JMetrics;
import com.shykhmat.jmetrics.sonar.plugin.sensor.JMetricsSensor;
import com.shykhmat.jmetrics.sonar.plugin.widget.JMetricsDashboardWidget;

/**
 * The entry point for SonarQube plug-in that contains a list of provided extensions.
 */
public class JMetricsPlugin extends SonarPlugin {

    @Override
    public List<Class<? extends Extension>> getExtensions() {
        return Arrays.asList(JMetricsSensor.class, JMetrics.class, JMetricsDashboardWidget.class);
    }
}
