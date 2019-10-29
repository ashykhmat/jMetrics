import React from 'react';
import '../style.css';
import JMetricsApp from './components/JMetricsApp';

//You can access it at /project/extension/jmetrics/jmetrics_page?id={COMPONENT_ID}
window.registerExtension('jmetrics/jmetrics_page', (options) => {
	 return <JMetricsApp project={options.component} />;
});