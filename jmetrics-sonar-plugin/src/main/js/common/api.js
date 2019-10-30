import {getJSON} from 'sonar-request';

export function findJMetricsForProject(project) {
  return getJSON('/api/measures/component', {
	  componentKey: project.key,
	  metricKeys: "jmetrics_html,jmetrics_excel"
  }).then(function(response) {
	  let data = {
		  jmetricsHtml : "",
		  jmetricsExcel : ""
	  };
	  const numberOfMeasuresRetrieved = 2;
	  for (let i = 0; i < numberOfMeasuresRetrieved; i++) {
		  let measure = response.component.measures[i];
		  if (measure.metric === "jmetrics_html"){
			  data.jmetricsHtml = measure.value;
		  } else if (measure.metric === "jmetrics_excel"){
			  data.jmetricsExcel = measure.value;
		  }
	  }
	  return data;
  });
}
