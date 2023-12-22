package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter implements DtoEntityConverter<MemberDto, Member> {

    private final DepartmentConverter departmentConverter;
    private final AcademicTitleConverter academicTitleConverter;
    private final EducationTitleConverter educationTitleConverter;
    private final ScientificFieldConverter scientificFieldConverter;

    public MemberConverter(DepartmentConverter departmentConverter, AcademicTitleConverter academicTitleConverter, EducationTitleConverter educationTitleConverter, ScientificFieldConverter scientificFieldConverter) {
        this.departmentConverter = departmentConverter;
        this.academicTitleConverter = academicTitleConverter;
        this.educationTitleConverter = educationTitleConverter;
        this.scientificFieldConverter = scientificFieldConverter;
    }

    @Override
    public MemberDto toDto(Member entity) {
        return new MemberDto(
                entity.getId(),
                entity.getName(),
                entity.getLastname(),
                academicTitleConverter.toDto(entity.getAcademicTitle()),
                educationTitleConverter.toDto(entity.getEducationTitle()),
                scientificFieldConverter.toDto(entity.getScientificField()),
                departmentConverter.toDto(entity.getDepartment())
        );
    }

    @Override
    public Member toEntity(MemberDto dto) {
//        return new Member(
//                dto.getId(),
//                dto.getFirstname(),
//                dto.getLastname(),
//                academicTitleConverter.toEntity(dto.getAcademicTitle()),
//                educationTitleConverter.toEntity(dto.getEducationTitle()),
//                scientificFieldConverter.toEntity(dto.getScientificField()),
//                departmentConverter.toEntity(dto.getDepartment()),
//                dto.get);
        return new Member();
    }
}