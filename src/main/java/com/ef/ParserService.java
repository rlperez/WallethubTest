package com.ef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;

@Service
public class ParserService {

    private static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String DEFAULT_FILE_PATH = "access.log";
    private static final String COMMENT_TEMPLATE = "IP %s was encountered %d times between times %s and %s.";

    private static final int DATETIME_INDEX = 0;
    private static final int IP_INDEX = 1;
    private static final int REQUEST_INDEX = 2;
    private static final int RESPONSE_CODE_INDEX = 3;
    private static final int USER_AGENT_INDEX = 4;

    private final LogEntryRepository logEntryRepository;
    private final BlockedIpLogEntryRepository blockedLogEntryRepository;

    @Autowired
    public ParserService(LogEntryRepository logEntryRepository, BlockedIpLogEntryRepository blockedIpLogEntryRepository) {
        this.logEntryRepository = logEntryRepository;
        this.blockedLogEntryRepository = blockedIpLogEntryRepository;
    }

    void persistLogFileToDb(String filePath, LocalDateTime startDate, Duration duration, int threshold) throws IOException {
        persistAllLogs(getFileLineStream(filePath));
        persistBlockedLogs(getFileLineStream(filePath), startDate, duration, threshold);
    }

    private void persistAllLogs(Stream<String> lines) {
        Set<LogEntry> logEntries = lines.map(l -> {
            String[] line = l.split("\\|");
            LocalDateTime logDate = LocalDateTime.parse(line[DATETIME_INDEX], LOG_DATE_FORMATTER);
            return new LogEntry(
                    0,
                    logDate,
                    line[IP_INDEX],
                    line[REQUEST_INDEX],
                    Integer.valueOf(line[RESPONSE_CODE_INDEX]),
                    line[USER_AGENT_INDEX]);
        }).collect(Collectors.toSet());

        logEntryRepository.saveAll(logEntries);
    }

    private void persistBlockedLogs(Stream<String> lines, LocalDateTime startDate, Duration duration, int threshold) {
        LocalDateTime endDate = getEndDate(startDate, duration);
        Map<String, Long> ipCounts = lines
                .map(l -> l.split("\\|"))
                .filter(l -> {
                    LocalDateTime logDate = LocalDateTime.parse(l[DATETIME_INDEX], LOG_DATE_FORMATTER);
                    return (endDate.isAfter(logDate) && startDate.isBefore(logDate)) ||
                            (endDate.isEqual(logDate) || startDate.isEqual(logDate));
                })
                .collect(Collectors.groupingBy(l -> l[IP_INDEX], counting()));

        Set<BlockedIpLogEntry> blockedIps = ipCounts.entrySet()
                .stream()
                .filter(count -> count.getValue() >= threshold)
                .map(entry -> new BlockedIpLogEntry(
                        0,
                        entry.getKey(),
                        String.format(COMMENT_TEMPLATE, entry.getKey(), entry.getValue(), startDate, endDate)))
                .collect(Collectors.toSet());

        blockedLogEntryRepository.saveAll(blockedIps);
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
