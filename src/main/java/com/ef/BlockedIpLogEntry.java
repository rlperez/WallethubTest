package com.ef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class BlockedIpLogEntry implements Serializable {
    @Id
    @GeneratedValue(generator = "blockedIpLogEntryIdSeqGen", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "blockedIpLogEntryIdSeqGen", strategy = "increment")
    private long id;

    private String ip;

    private String comment;

    private long requestCount;
}


