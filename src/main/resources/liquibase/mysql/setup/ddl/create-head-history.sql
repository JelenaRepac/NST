create table tbl_head_history(
	id bigint unsigned not null AUTO_INCREMENT,
	start_date DATE,
	end_date DATE,
    member_id bigint unsigned,
    department_id bigint unsigned,
    primary key (id),
    constraint member_head_fk FOREIGN KEY (member_id) REFERENCES tbl_member(id),
    constraint department_head_fk FOREIGN KEY (department_id) REFERENCES tbl_department(id)
   )