package com.ef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class LogEntry implements Serializable {
    @Id
    @GeneratedValue(generator = "logEntryIdSeqGen", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "logEntryIdSeqGen", strategy = "increment")
    private long id;

    private LocalDateTime timestamp;

    private String ip;

    private String request;

    private int responseCode;

    private String userAgent;
}


