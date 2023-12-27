package nst.springboot.nstapplication.dto;

import lombok.*;

import java.io.Serializable;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberHeadSecretaryDto implements Serializable {

    private Long id;

    private String firstname;

    private String lastname;

    private AcademicTitleDto academicTitle;

    private EducationTitleDto educationTitle;

    private ScientificFieldDto scientificField;

    private DepartmentDto department;

    private RoleDto role;
}
