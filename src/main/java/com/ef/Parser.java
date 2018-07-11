package com.ef;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;

public class Parser {

    private static final String DURATION_KEY = "duration";
    private static final String THRESHOLD_KEY = "threshold";
    private static final String START_DATE_KEY = "startDate";
    private static final String FILE_PATH_KEY = "file";
    private static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter ARG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
    private static final String DEFAULT_FILE_PATH = "access.log";
    private static final String COMMENT_TEMPLATE = "IP %s was encountered %d times between times %s and %s.";

    private static final int DATETIME_INDEX = 0;
    private static final int IP_INDEX = 1;

    public static void main(String[] args) {
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

            Parser parser = new Parser();
            Stream<String> fileLines = parser.getFileLineStream(filePath);
            Set<LogEntry> logEntries = parser.getIpAddresses(fileLines, date, duration, threshold);
            parser.logIpAddresses(logEntries);
        } catch (IllegalArgumentException | DateTimeParseException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public Set<LogEntry> getIpAddresses(Stream<String> lines, LocalDateTime startDate, Duration duration, int threshold) throws IOException {
        LocalDateTime endDate = getEndDate(startDate, duration);
        Map<String, Long> ipCounts = lines
                .map(l -> l.split("\\|"))
                .filter(l -> {
                    LocalDateTime logDate = LocalDateTime.parse(l[DATETIME_INDEX], LOG_DATE_FORMATTER);
                    return (endDate.isAfter(logDate) && startDate.isBefore(logDate)) ||
                            (endDate.isEqual(logDate) || startDate.isEqual(logDate));
                })
                .collect(Collectors.groupingBy(l -> l[IP_INDEX], counting()));

        return ipCounts.entrySet()
                .stream()
                .filter(count -> count.getValue() >= threshold)
                .map(entry -> new LogEntry(
                        0,
                        entry.getKey(),
                        String.format(COMMENT_TEMPLATE, entry.getKey(), entry.getValue(), startDate, endDate)))
                .collect(Collectors.toSet());
    }

    private void logIpAddresses(Set<LogEntry> logEntries) {
        logEntries.forEach(entry -> System.out.println(entry.getIp()));
    }

    private LocalDateTime getEndDate(LocalDateTime startDate, Duration duration) {
        LocalDateTime endDate;
        switch (duration) {
            case DAILY:
                endDate = startDate.plusDays(1);
                break;
            case HOURLY:
                endDate = startDate.plusHours(1);
                break;
            default:
                endDate = startDate;
        }

        return endDate;
    }

    private Stream<String> getFileLineStream(String filePath) throws IOException {
        if (filePath != null && !filePath.isEmpty()) {
            return Files.lines(Paths.get(filePath));
        } else {
            InputStream inputStream = Parser.class.getClassLoader().getResourceAsStream(DEFAULT_FILE_PATH);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader.lines();
        }
    }
}
