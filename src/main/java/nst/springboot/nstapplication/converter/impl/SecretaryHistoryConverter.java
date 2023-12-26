package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.SecretaryHistory;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;

public class SecretaryHistoryConverter implements DtoEntityConverter<SecretaryHistoryDto, SecretaryHistory> {


    private final MemberConverter memberConverter;
    private final DepartmentConverter departmentConverter;

    public SecretaryHistoryConverter(MemberConverter memberConverter, DepartmentConverter departmentConverter) {
        this.memberConverter = memberConverter;
        this.departmentConverter = departmentConverter;
    }

    @Override
    public SecretaryHistoryDto toDto(SecretaryHistory secretaryHistory) {
        return SecretaryHistoryDto.builder().
                id(secretaryHistory.getId()).
                startDate(secretaryHistory.getStartDate()).
                endDate(secretaryHistory.getEndDate()).
                member(memberConverter.toDto(secretaryHistory.getMember())).
                department(departmentConverter.toDto(secretaryHistory.getDepartment())).
                build();
    }

    @Override
    public SecretaryHistory toEntity(SecretaryHistoryDto secretaryHistoryDto) {
        return SecretaryHistory.builder().
                id(secretaryHistoryDto.getId()).
                startDate(secretaryHistoryDto.getStartDate()).
                endDate(secretaryHistoryDto.getEndDate()).
                member(memberConverter.toEntity(secretaryHistoryDto.getMember())).
                department(departmentConverter.toEntity(secretaryHistoryDto.getDepartment())).
                build();
    }
}
