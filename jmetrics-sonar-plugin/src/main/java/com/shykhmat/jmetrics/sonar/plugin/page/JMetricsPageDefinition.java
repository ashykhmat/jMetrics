package com.shykhmat.jmetrics.sonar.plugin.page;

import static org.sonar.api.web.page.Page.Scope.COMPONENT;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

/**
 * jMetrics plug-in page definition.
 */
public class JMetricsPageDefinition implements PageDefinition {

	@Override
	public void define(Context context) {
		context.addPage(Page.builder("jmetrics/jmetrics_page").setName("jMetrics Page").setScope(COMPONENT).build());
	}
}
