
package com.shykhmat.jmetrics.core.metric;

/**
 * Exception that occurs during metric calculation.
 */
public class MetricException extends Exception {

	private static final long serialVersionUID = -1294407714803698071L;

	public MetricException(Exception e) {
		super(e);
	}

}
