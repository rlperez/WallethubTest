package com.ef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class LogEntry {
    @Id
    @GeneratedValue(generator = "logEntryIdSeqGen")
    @SequenceGenerator(name = "logEntryIdSeqGen", allocationSize = 1)
    private long id;

    private LocalDateTime timestamp;

    private String ip;

    private String request;

    private int responseCode;

    private String userAgent;
}


