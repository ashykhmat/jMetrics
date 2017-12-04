package com.shykhmat.jmetrics.core.metric.halstead.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shykhmat.jmetrics.core.metric.CodePart;

/**
 * Class to calculate operators and operands metrics, that are required for
 * Halstead metrics calculation.
 */
public class HalsteadMetricsCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(HalsteadMetricsCalculator.class);
    private static final String[] SYMBOLS = { "+", "++", "-", "--", "*", ".", ";", "/", "%", "!", ">", "<", ">=", "<=", "==", "=", ":", "~" };
    private static final String[][] BRACES = { { "{", "}" }, { "(", ")" }, { "[", "]" }, { "<", ">" } };

    public HalsteadComplexityMetrics calculate(CodePart codePart) throws Exception {
        Map<String, Integer> operators = new HashMap<>();
        Map<String, Integer> operands = new HashMap<>();
        List<String> tokens = tokenize(codePart);
        LOGGER.debug("Analyzing code part keywords");
        analyzeKeywords(tokens, operators, operands);
        LOGGER.debug("Analyzing code part symbols");
        analyzeSymbols(tokens, operators);
        LOGGER.debug("Analyzing code part braces");
        analyzeBraces(tokens, operators);
        LOGGER.debug("Analyzing code part literals");
        analyzeLiterals(tokens, operators, operands);
        LOGGER.debug("Calculating final Halstead Metrics");
        HalsteadComplexityMetrics halsteadMetrics = new HalsteadComplexityMetrics(operators, operands);
        return halsteadMetrics.calculateMetric(codePart);
    }

    private List<String> tokenize(CodePart codePart) {
        List<String> tokens = new LinkedList<>();
        CharStream charStream = CharStreams.fromString(codePart.getCodeToAnalyze().toString());
        Java9Lexer jlexer = new Java9Lexer(charStream);
        CommonTokenStream jtokenStream = new CommonTokenStream(jlexer);
        Java9Parser jparser = new Java9Parser(jtokenStream);
        switch (codePart.getCodeType()) {
        case INTERFACE: {
            jparser.interfaceDeclaration();
            break;
        }
        case CLASS: {
            jparser.classDeclaration();
            break;
        }
        case ENUM: {
            jparser.enumDeclaration();
            break;
        }
        case ANNOTATION: {
            jparser.annotationTypeDeclaration();
            break;
        }
        case METHOD: {
            jparser.methodDeclaration();
            break;
        }
        case CONSTRUCTOR: {
            jparser.constructorDeclaration();
            break;
        }
        default: {
            return tokens;
        }
        }
        TokenStream tokenStream = jparser.getTokenStream();
        for (int i = 0; i < tokenStream.size(); i++) {
            tokens.add(tokenStream.get(i).getText().toString());
        }
        return tokens;
    }

    private void analyzeKeywords(List<String> tokens, Map<String, Integer> operators, Map<String, Integer> operands) {
        for (String token : tokens) {
            if (Java9Keywords.isKeyword(token)) {
                Integer tokenCount = operators.get(token);
                operators.put(token, tokenCount == null ? 1 : ++tokenCount);
            } else if (token.charAt(0) == '"') {
                Integer tokenCount = operands.get(token);
                operands.put(token, tokenCount == null ? 1 : ++tokenCount);
            }
        }
    }

    private void analyzeSymbols(List<String> tokens, Map<String, Integer> operators) {
        for (String symbol : SYMBOLS) {
            int count = this.countSymbols(symbol, tokens);
            if (count > 0) {
                Integer symbolCount = operators.get(symbol);
                operators.put(symbol, symbolCount == null ? count : symbolCount + count);
            }
        }
    }

    private int countSymbols(String symbol, List<String> tokens) {
        int count = 0;
        for (String token : tokens) {
            if (token.equals(symbol)) {
                ++count;
            }
        }
        return count;
    }

    private void analyzeBraces(List<String> tokens, Map<String, Integer> operators) {
        for (String[] brace : BRACES) {
            int count = this.countBraces(brace, tokens);
            if (count > 0) {
                String braces = brace[0] + " " + brace[1];
                Integer bracesCount = operators.get(braces);
                operators.put(braces, bracesCount == null ? count : bracesCount + count);
            }
        }
    }

    private int countBraces(String brace[], List<String> tokens) {
        int counter = 0, startCount = 0, endCount = 0;
        for (String token : tokens) {
            if (token.equals(brace[0])) {
                ++startCount;
            } else if (token.equals(brace[1])) {
                ++endCount;
            }
        }
        if (startCount == endCount) {
            counter = startCount;
        }
        return counter;
    }

    private void analyzeLiterals(List<String> tokens, Map<String, Integer> operators, Map<String, Integer> operands) {
        for (Iterator<String> iterator = tokens.iterator(); iterator.hasNext();) {
            String token = iterator.next().toString();
            if (operators.containsKey(token) || operands.containsKey(token) || token.equals("<EOF>")) {
                iterator.remove();
            }
        }
        for (String token : tokens) {
            Integer tokenCount = operands.get(token);
            operands.put(token, tokenCount == null ? 1 : ++tokenCount);
        }
    }
}
