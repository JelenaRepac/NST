package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.domain.Role;
import nst.springboot.nstapplication.dto.MemberHeadSecretaryDto;
import org.springframework.stereotype.Component;

@Component
public class MemberHeadSecretaryConverter implements DtoEntityConverter<MemberHeadSecretaryDto, Member> {

    private DepartmentConverter departmentConverter;
    private AcademicTitleConverter academicTitleConverter;
    private EducationTitleConverter educationTitleConverter;
    private ScientificFieldConverter scientificFieldConverter;

    private RoleConverter roleConverter;

    public MemberHeadSecretaryConverter(DepartmentConverter departmentConverter, AcademicTitleConverter academicTitleConverter, EducationTitleConverter educationTitleConverter, ScientificFieldConverter scientificFieldConverter, RoleConverter roleConverter) {
        this.departmentConverter = departmentConverter;
        this.academicTitleConverter = academicTitleConverter;
        this.educationTitleConverter = educationTitleConverter;
        this.scientificFieldConverter = scientificFieldConverter;
        this.roleConverter = roleConverter;
    }

    @Override
    public MemberHeadSecretaryDto toDto(Member entity) {
        return MemberHeadSecretaryDto.builder().
                id(entity.getId()).
                firstname(entity.getFirstname()).
                lastname(entity.getLastname()).
                academicTitle(academicTitleConverter.toDto(entity.getAcademicTitle())).
                educationTitle(educationTitleConverter.toDto(entity.getEducationTitle())).
                scientificField(scientificFieldConverter.toDto(entity.getScientificField())).
                department(departmentConverter.toDto(entity.getDepartment())).
                role(roleConverter.toDto(entity.getRole()))
                .build();
    }

    @Override
    public Member toEntity(MemberHeadSecretaryDto memberHeadSecretaryDto) {
        return Member.builder().
                id(memberHeadSecretaryDto.getId()).
                firstname(memberHeadSecretaryDto.getFirstname()).
                lastname(memberHeadSecretaryDto.getLastname()).
                academicTitle(academicTitleConverter.toEntity(memberHeadSecretaryDto.getAcademicTitle())).
                educationTitle(educationTitleConverter.toEntity(memberHeadSecretaryDto.getEducationTitle())).
                scientificField(scientificFieldConverter.toEntity(memberHeadSecretaryDto.getScientificField())).
                department(departmentConverter.toEntity(memberHeadSecretaryDto.getDepartment())).
                role(memberHeadSecretaryDto.getRole()==null ? null : roleConverter.toEntity(memberHeadSecretaryDto.getRole()))
                .build();
    }

}
