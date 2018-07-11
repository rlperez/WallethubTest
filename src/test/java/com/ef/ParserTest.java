package com.ef;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
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
        Parser parser = new Parser();
        LocalDateTime startDate = LocalDateTime.parse("2017-01-01 00:00:00", FORMATTER);
        Set<String> ips = parser.getIpAddresses("out/test/resources/access.log", startDate, Duration.DAILY, 500);

        assertThat(ips)
                .isNotEmpty()
                .hasSize(15)
                .containsExactlyInAnyOrder(
                        "192.168.203.111",
                        "192.168.51.205",
                        "192.168.185.164",
                        "192.168.129.191",
                        "192.168.31.26",
                        "192.168.52.153",
                        "192.168.33.16",
                        "192.168.162.248",
                        "192.168.38.77",
                        "192.168.62.176",
                        "192.168.219.10",
                        "192.168.199.209",
                        "192.168.143.177",
                        "192.168.102.136",
                        "192.168.206.141"
                );
    }
}
