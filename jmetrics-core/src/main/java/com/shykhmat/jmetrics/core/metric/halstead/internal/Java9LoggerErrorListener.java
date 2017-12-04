package com.shykhmat.jmetrics.core.metric.halstead.internal;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to log errors during Java code parsing.
 */
public class Java9LoggerErrorListener extends BaseErrorListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Java9LoggerErrorListener.class);

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        LOGGER.warn("line {} : {}", charPositionInLine, msg);
    }
}
