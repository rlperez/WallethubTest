create table wallethub.blocked_ip_log_entry
(
	id bigint not null
		primary key,
	comment varchar(255) not null,
	ip varchar(255) not null,
	request_count bigint not null
);

create table wallethub.log_entry
(
	id bigint not null
		primary key,
	ip varchar(255) not null,
	request varchar(255) not null,
	response_code int not null,
	timestamp datetime not null,
	user_agent varchar(255) not null
);




