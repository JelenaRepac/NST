create table tbl_academic_title_history(
	id bigint unsigned not null AUTO_INCREMENT,
	start_date DATE,
	end_date DATE,
    member_id bigint unsigned,
    academic_title_id bigint unsigned,
    scientific_field_id bigint unsigned,
    primary key (id),
    constraint member_ath_fk FOREIGN KEY (member_id) REFERENCES tbl_member(id),
    constraint academic_title_ath_fk FOREIGN KEY (academic_title_id) REFERENCES tbl_academic_title(id),
    constraint scientific_field_ath_fk FOREIGN KEY (scientific_field_id) REFERENCES tbl_scientific_field(id)
   )