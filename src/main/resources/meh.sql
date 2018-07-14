select ip from (select ip, count(1) as count
  from log_entry
  where TIMESTAMP('2017-01-01.17:00:00') < timestamp and
        TIMESTAMP('2017-01-01.18:00:00') > timestamp
  group by ip) as time_spanned_log where count > 100;

select * from log_entry where ip = '192.168.230.70';
