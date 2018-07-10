package com.ef.Parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {

    private static final String DURATION_KEY = "duration";
    private static final String THRESHOLD_KEY = "threshold";
    private static final String START_DATE_KEY = "startDate";
    private static final String FILE_PATH_KEY = "file";

    public static void main(String[] args) {
        Map<String, String> parsedArguments = Arrays.stream(args)
                .filter(arg -> arg.startsWith("--"))
                .map(arg -> arg.split("="))
                .collect(Collectors.toMap(arg ->
                        arg[0].replaceFirst("--", ""),
                        arg -> arg[1].toUpperCase()));

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
            LocalDate date = LocalDate.parse(parsedArguments.get(START_DATE_KEY), formatter);
            Duration duration = Duration.valueOf(parsedArguments.get(DURATION_KEY));
            int threshold = Integer.parseInt(parsedArguments.get(THRESHOLD_KEY));
            String filePath = parsedArguments.getOrDefault("file", "access.log");

        } catch (IllegalArgumentException | DateTimeParseException exception) {
            exception.printStackTrace();
        }
    }
}
