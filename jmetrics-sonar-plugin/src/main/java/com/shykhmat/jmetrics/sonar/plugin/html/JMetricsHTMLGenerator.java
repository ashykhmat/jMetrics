
package com.shykhmat.jmetrics.sonar.plugin.html;

import java.util.HashMap;
import java.util.Map;

import com.shykhmat.jmetrics.core.api.JMetricsApi;
import com.shykhmat.jmetrics.core.metric.Status;
import com.shykhmat.jmetrics.core.report.ClassReport;
import com.shykhmat.jmetrics.core.report.MethodReport;
import com.shykhmat.jmetrics.core.report.ProjectReport;

/**
 * Class to generate HTML report with project metrics. Currently only
 * Maintainability index is displayed.
 */
public class JMetricsHTMLGenerator {
	private static final String REPORT_START = "<div class='scrollable-div'><div class='accordion'>";
	private static final String REPORT_END = "</div></div>";

	private static final String CLASS_SECTION_START = "<div class='accordion-section'><a class='accordion-section-title' href='#accordion-%d'><table class='full-width' style=''><tr><td class='large-width'>%s</td><td class='small-width'>MI: %f</td><td>%s<td></tr></table></a><div id='accordion-%d' class='accordion-section-content'><table class='full-width'><tr><td class='large-width'><b>Method</b></td><td class='small-width'><b>Maintainability Index</b></td></tr>";
	private static final String CLASS_SECTION_END = "</table></div></div>";

	private static final String METHOD_SECTION = "<tr><td class='large-width'>%s</td><td style='small-width'>%f</td><td>%s<td></tr>";

	private static final String ALERT_OK = "<span id='m_alert_status'><i class='icon-alert-ok'></id></span>";
	private static final String ALERT_ERROR = "<span id='m_alert_status'><i class='icon-alert-error'></id></span>";
	private static final String ALERT_WARNING = "<span id='m_alert_status'><i class='icon-alert-warn'></id></span>";

	private JMetricsApi jMetricsApi;
	private Map<Status, String> statusesIcons;

	public JMetricsHTMLGenerator(JMetricsApi jMetricsApi) {
		this.jMetricsApi = jMetricsApi;
		statusesIcons = new HashMap<>();
		statusesIcons.put(Status.OK, ALERT_OK);
		statusesIcons.put(Status.ERROR, ALERT_ERROR);
		statusesIcons.put(Status.WARNING, ALERT_WARNING);
	}

	public String toHTML(ProjectReport project) {
		StringBuilder html = new StringBuilder();
		html.append(REPORT_START);

		int i = 1;
		for (ClassReport concreteClass : project.getClasses()) {
			String classStatus = statusesIcons.get(
					jMetricsApi.getMaintainabilityIndexStatus(concreteClass.getMetrics().getMaintainabilityIndex()));
			html.append(String.format(CLASS_SECTION_START, i, concreteClass.getName(),
					concreteClass.getMetrics().getMaintainabilityIndex(), classStatus, i));
			for (MethodReport method : concreteClass.getMethods()) {
				String methodStatus = statusesIcons
						.get(jMetricsApi.getMaintainabilityIndexStatus(method.getMetrics().getMaintainabilityIndex()));
				html.append(String.format(METHOD_SECTION, convertSpecialCharacters(method.getName()),
						method.getMetrics().getMaintainabilityIndex(), methodStatus));
			}
			html.append(CLASS_SECTION_END);
			i++;
		}

		html.append(REPORT_END);
		return html.toString();
	}

	private String convertSpecialCharacters(String html) {
		return html.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
