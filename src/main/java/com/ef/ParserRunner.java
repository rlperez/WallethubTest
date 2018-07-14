package com.ef;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Component
public class Parser implements CommandLineRunner {

    private static final String DURATION_KEY = "duration";
    private static final String THRESHOLD_KEY = "threshold";
    private static final String START_DATE_KEY = "startDate";
    private static final String FILE_PATH_KEY = "file";
    private static final DateTimeFormatter ARG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

    private ParserService parserService;

    @Autowired
    public Parser(ParserService parserService) {
        this.parserService = parserService;
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, String> parsedArguments = Arrays.stream(args)
                .filter(arg -> arg.startsWith("--"))
                .map(arg -> arg.split("="))
                .collect(Collectors.toMap(arg ->
                        arg[0].replaceFirst("--", ""), arg -> arg[1].toUpperCase()));

        try {
            LocalDateTime date = LocalDateTime.parse(parsedArguments.get(START_DATE_KEY), ARG_DATE_FORMATTER);
            Duration duration = Duration.valueOf(parsedArguments.get(DURATION_KEY));
            int threshold = Integer.parseInt(parsedArguments.get(THRESHOLD_KEY));
            String filePath = parsedArguments.get(FILE_PATH_KEY);

            ParserService parserService = getParserService();
            parserService.persistLogFileToDb(filePath, date, duration, threshold);
        } catch (IllegalArgumentException | DateTimeParseException | IOException exception) {
            exception.printStackTrace();
        }
    }
}
