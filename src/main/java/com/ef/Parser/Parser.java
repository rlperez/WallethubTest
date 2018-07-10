package com.ef.Parser;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {

    private static final String DURATION_KEY = "duration";
    private static final String THRESHOLD_KEY = "threshold";
    private static final String START_DATE_KEY = "startDate";

    public static void main(String[] args) {
        Map<String, String> parsedArguments = Arrays.stream(args)
                .filter(arg -> arg.startsWith("--"))
                .map(arg -> arg.split("="))
                .collect(Collectors.toMap(arg -> arg[0].replaceFirst("--", ""), arg -> arg[1]));

    }
}
