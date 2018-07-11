package com.ef;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void contextLoad() {

    }

    @Test
    public void verifyGivenTestCase() throws IOException {
        // java -cp "parser.jar" com.ef.Parser --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500
        LocalDateTime startDate = LocalDateTime.parse("2017-01-01 00:00:00", FORMATTER);
        Set<String> ips = Parser.getIpAddresses("out/test/resources/access.log", startDate, Duration.DAILY, 500);

        assertThat(ips)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly("192.168.102.136");
    }
}
