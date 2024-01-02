package nst.springboot.nstapplication.dto;

import lombok.*;
import nst.springboot.nstapplication.domain.Role;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberPatchRequest {

    String firstname;

    String lastname;

    AcademicTitleDto academicTitle;

    EducationTitleDto educationTitle;

    ScientificFieldDto scientificField;

    DepartmentDto department;

    RoleDto role;
}
