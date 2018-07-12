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
class BlockedIpLogEntry {
    @Id
    @GeneratedValue(generator = "blockedIpLogEntryIdSeqGen")
    @SequenceGenerator(name = "blockedIpLogEntryIdSeqGen", allocationSize = 1)
    private long id;

    private String ip;

    private String comment;
}


