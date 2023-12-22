package nst.springboot.nstapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    Long id;

    String firstname;

    String lastname;

    AcademicTitleDTO academicTitle;

    EducationTitleDTO educationTitle;

    ScientificFieldDTO scientificField;

    DepartmentDto department;
}
