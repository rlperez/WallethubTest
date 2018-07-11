package com.ef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    private String ip;

    private String comment;
}


