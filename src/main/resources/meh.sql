select * from log_entry where ip = ?;
select * from blocked_ip_log_entry where request_count > 100;
