package com.shykhmat.jmetrics.core.metric.halstead.internal;

/**
 * Class that contains Java 9 reserved keywords. Official documentation:
 * https://docs.oracle.com/javase/specs/jls/se9/html/jls-3.html#jls-3.9
 * 
 */
public final class Java9Keywords {
    private static String[] KEYWORDS = { "abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package", "synchronized", "boolean", "do", "goto", "private", "this", "break", "double", "implements", "protected", "throw", "byte",
            "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile",
            "const", "float", "native", "super", "while", "open", "module", "requires", "transitive", "exports", "opens", "to", "uses", "provides", "with" };

    private Java9Keywords() {

    }

    public static boolean isKeyword(String keyword) {
        boolean result = false;
        for (int i = 0; i < KEYWORDS.length; i++) {
            if (KEYWORDS[i].equals(keyword)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
