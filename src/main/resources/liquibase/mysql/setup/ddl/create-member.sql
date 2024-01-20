create table member(
	id bigint unsigned not null AUTO_INCREMENT,
	firstname varchar(100) not null,
	lastname varchar(100) not null,
	academic_title_id bigint unsigned,
	education_title_id bigint unsigned,
	scientific_field_id bigint unsigned,
	department_id bigint unsigned,
    role_id bigint unsigned,
    primary key (id),
    constraint academic_title_fk FOREIGN KEY (academic_title_id) REFERENCES academic_title(id),
    constraint education_title_fk FOREIGN KEY (education_title_id) REFERENCES education_title(id),
    constraint scientific_field_fk FOREIGN KEY (scientific_field_id) REFERENCES scientific_field(id),
    constraint department_member_fk FOREIGN KEY (department_id) REFERENCES department(id),
    constraint role_fk FOREIGN KEY (role_id) REFERENCES tbl_role(id)
    )