package com.ef;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedIpLogEntryRepository extends JpaRepository<BlockedIpLogEntry, Long> {
}
