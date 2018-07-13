create table blocked_ip_log_entry
(
	id bigint not null
		primary key,
	comment varchar(255) null,
	ip varchar(255) null
)
engine=MyISAM
;

create table log_entry
(
	id bigint not null
		primary key,
	ip varchar(255) null,
	request varchar(255) null,
	response_code int not null,
	timestamp datetime null,
	user_agent varchar(255) null
)
engine=MyISAM
;

