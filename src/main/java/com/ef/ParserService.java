package com.ef;

import java.time.format.DateTimeFormatter;

public class LogParser {
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
    private static final int REQUEST_INDEX = 2;
    private static final int RESPONSE_CODE_INDEX = 3;
    private static final int USER_AGENT_INDEX = 4;

    
}
