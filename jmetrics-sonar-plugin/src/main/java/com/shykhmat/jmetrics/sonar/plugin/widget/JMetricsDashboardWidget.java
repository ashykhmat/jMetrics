package com.shykhmat.jmetrics.sonar.plugin.widget;

import org.sonar.api.web.AbstractRubyTemplate;
import org.sonar.api.web.Description;
import org.sonar.api.web.RubyRailsWidget;
import org.sonar.api.web.UserRole;

/**
 * jMetrics plug-in widget definition.
 */
@UserRole(UserRole.USER)
@Description("Shows metrics for java project")
public class JMetricsDashboardWidget extends AbstractRubyTemplate implements RubyRailsWidget {

    @Override
    public String getId() {
        return "jmetrics";
    }

    @Override
    public String getTitle() {
        return "jMetrics";
    }

    @Override
    protected String getTemplatePath() {
        return "/widget/jmetrics_widget.html.erb";
    }
}
