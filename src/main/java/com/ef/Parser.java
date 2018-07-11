package com.ef;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

public class Parser {

    private static final String DURATION_KEY = "duration";
    private static final String THRESHOLD_KEY = "threshold";
    private static final String START_DATE_KEY = "startDate";
    private static final String FILE_PATH_KEY = "file";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static final int DATETIME_INDEX = 0;
    private static final int IP_INDEX = 1;

    public static void main(String[] args) {
        Map<String, String> parsedArguments = Arrays.stream(args)
                .filter(arg -> arg.startsWith("--"))
                .map(arg -> arg.split("="))
                .collect(Collectors.toMap(arg ->
                        arg[0].replaceFirst("--", ""), arg -> arg[1].toUpperCase()));

        try {
            LocalDateTime date = LocalDateTime.parse(parsedArguments.get(START_DATE_KEY), FORMATTER);
            Duration duration = Duration.valueOf(parsedArguments.get(DURATION_KEY));
            int threshold = Integer.parseInt(parsedArguments.get(THRESHOLD_KEY));
            String filePath = parsedArguments.getOrDefault(FILE_PATH_KEY, "access.log");

            Set<String> ips = getIpAddresses(filePath, date, duration, threshold);
            logIpAddresses(ips);

        } catch (IllegalArgumentException | DateTimeParseException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public static Set<String> getIpAddresses(String filePath, LocalDateTime startDate, Duration duration, int threshold) throws IOException {
        LocalDateTime endDate = getEndDate(startDate, duration);

        Map<String, Long> ipCounts = Files.lines(Paths.get(filePath))
                .map(l -> l.split("\\|"))
                .filter(l -> {
                    LocalDateTime logDate = LocalDateTime.parse(l[DATETIME_INDEX], FORMATTER);
                    return endDate.isAfter(logDate) && startDate.isBefore(logDate);
                })
                .collect(Collectors.groupingBy(l -> l[IP_INDEX], counting()));

        return ipCounts.entrySet()
                .stream()
                .filter(count -> count.getValue() >= threshold)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    private static void logIpAddresses(Set<String> ips) {
        ips.forEach(System.out::println);
    }

    private static LocalDateTime getEndDate(LocalDateTime startDate, Duration duration) {
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
}
