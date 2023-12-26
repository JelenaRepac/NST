package nst.springboot.nstapplication.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
    Long id;

    String firstname;

    String lastname;

    AcademicTitleDto academicTitle;

    EducationTitleDto educationTitle;

    ScientificFieldDto scientificField;

    DepartmentDto department;

    RoleDto role;

}
