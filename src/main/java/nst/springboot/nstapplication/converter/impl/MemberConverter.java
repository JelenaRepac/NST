package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.MemberHeadSecretaryDto;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter implements DtoEntityConverter<MemberDto, Member> {

    private final DepartmentConverter departmentConverter;
    private final AcademicTitleConverter academicTitleConverter;
    private final EducationTitleConverter educationTitleConverter;
    private final ScientificFieldConverter scientificFieldConverter;
    private final RoleConverter roleConverter;

    public MemberConverter(DepartmentConverter departmentConverter, AcademicTitleConverter academicTitleConverter, EducationTitleConverter educationTitleConverter, ScientificFieldConverter scientificFieldConverter, RoleConverter roleConverter) {
        this.departmentConverter = departmentConverter;
        this.academicTitleConverter = academicTitleConverter;
        this.educationTitleConverter = educationTitleConverter;
        this.scientificFieldConverter = scientificFieldConverter;
        this.roleConverter = roleConverter;
    }

    @Override
    public MemberDto toDto(Member entity) {
        return MemberDto.builder().
                id(entity.getId()).
                firstname(entity.getFirstname()).
                lastname(entity.getLastname()).
                academicTitle(academicTitleConverter.toDto(entity.getAcademicTitle())).
                educationTitle(educationTitleConverter.toDto(entity.getEducationTitle())).
                scientificField(scientificFieldConverter.toDto(entity.getScientificField())).
                role(roleConverter.toDto(entity.getRole())).
                department(departmentConverter.toDto(entity.getDepartment())).
                build();
    }

    @Override
    public Member toEntity(MemberDto dto) {
        return Member.builder().
                id(dto.getId()).
                firstname(dto.getFirstname()).
                lastname(dto.getLastname()).
                academicTitle(academicTitleConverter.toEntity(dto.getAcademicTitle())).
                educationTitle(educationTitleConverter.toEntity(dto.getEducationTitle())).
                scientificField(scientificFieldConverter.toEntity(dto.getScientificField())).
                role(roleConverter.toEntity(dto.getRole())).
                department(departmentConverter.toEntity(dto.getDepartment())).
                build();

    }
}