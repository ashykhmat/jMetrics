
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

	private static final String CLASS_SECTION_START = "<div class='accordion-section'><a class='accordion-section-title' href='#accordion-%d' onclick='handleAccordion(event);'><table class='full-width' style=''><tr><td class='large-width'>%s</td><td class='small-width'>MI: %f</td><td>%s<td></tr></table></a><div id='accordion-%d' class='accordion-section-content'><table class='full-width'><tr><td class='large-width'><b>Method</b></td><td class='small-width'><b>Maintainability Index</b></td></tr>";
	private static final String CLASS_SECTION_END = "</table></div></div>";

	private static final String METHOD_SECTION = "<tr><td class='large-width'>%s</td><td style='small-width'>%f</td><td>%s<td></tr>";

	private static final String ALERT_OK = "<span id='m_alert_status'><svg class='little-spacer-right' height='18' version='1.1' viewBox='0 0 14 14' width='18' xml:space='preserve' xmlns:xlink='http://www.w3.org/1999/xlink' style='fill-rule: evenodd; clip-rule: evenodd; stroke-linejoin: round; stroke-miterlimit: 1.41421;'><path d='M12.03 6.734a.49.49 0 0 0-.14-.36l-.71-.702a.48.48 0 0 0-.352-.15.474.474 0 0 0-.35.15l-3.19 3.18-1.765-1.766a.479.479 0 0 0-.35-.15.479.479 0 0 0-.353.15l-.71.703a.482.482 0 0 0-.14.358c0 .14.046.258.14.352l2.828 2.828c.098.1.216.15.35.15.142 0 .26-.05.36-.15l4.243-4.242a.475.475 0 0 0 .14-.352l-.001.001zM14 8c0 1.09-.268 2.092-.805 3.012a5.96 5.96 0 0 1-2.183 2.183A5.863 5.863 0 0 1 8 14a5.863 5.863 0 0 1-3.012-.805 5.96 5.96 0 0 1-2.183-2.183A5.863 5.863 0 0 1 2 8c0-1.09.268-2.092.805-3.012a5.96 5.96 0 0 1 2.183-2.183A5.863 5.863 0 0 1 8 2c1.09 0 2.092.268 3.012.805a5.96 5.96 0 0 1 2.183 2.183C13.732 5.908 14 6.91 14 8z' style='fill: rgb(51, 204, 51);'></path></svg></span>";
	private static final String ALERT_ERROR = "<span id='m_alert_status'><svg class='little-spacer-right' height='19' version='1.1' viewBox='0 0 26 34' width='19' xml:space='preserve' xmlns:xlink='http://www.w3.org/1999/xlink' style='fill-rule: evenodd; clip-rule: evenodd; stroke-linejoin: round; stroke-miterlimit: 1.41421;'><g transform=\"translate(0.000000,36.000000) scale(0.100000,-0.100000)\" fill=\"rgb(212, 51, 63)\" stroke=\"none\"><path d=\"M86 281 c-89 -59 -88 -198 3 -246 38 -19 104 -19 142 0 89 47 93 193 6 245 -42 26 -113 26 -151 1z m103 -57 c30 29 31 29 51 11 l20 -19 -30 -31 -31 -32 30 -25 29 -25 -20 -21 -21 -20 -25 29 -25 30 -32 -31 -31 -30 -19 20 c-18 20 -18 21 11 51 l29 30 -31 29 -32 28 20 20 20 20 28 -32 29 -31 30 29z\"/></g></svg>";
	private static final String ALERT_WARNING = "<span id='m_alert_status'><svg class='little-spacer-right' height='18' version='1.1' viewBox='0 0 14 14' width='18' xml:space='preserve' xmlns:xlink='http://www.w3.org/1999/xlink' style='fill-rule: evenodd; clip-rule: evenodd; stroke-linejoin: round; stroke-miterlimit: 1.41421;'><path d='M8 14c-3.311 0-6-2.689-6-6s2.689-6 6-6 6 2.689 6 6-2.689 6-6 6zM7 9h2V4H7v5zm0 3h2v-2H7v2z' style='fill: rgb(204, 204, 0); fill-rule: nonzero;'></path></svg></span>";

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
