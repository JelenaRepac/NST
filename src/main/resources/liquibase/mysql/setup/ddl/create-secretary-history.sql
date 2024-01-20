create table secretary_history(
	id bigint unsigned not null AUTO_INCREMENT,
	start_date DATE,
	end_date DATE,
    member_id bigint unsigned,
    department_id bigint unsigned,
    primary key (id),
    constraint member_sec_fk FOREIGN KEY (member_id) REFERENCES member(id),
    constraint department_sec_fk FOREIGN KEY (department_id) REFERENCES department(id)
   )