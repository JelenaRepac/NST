package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.HeadHistory;
import nst.springboot.nstapplication.dto.HeadHistoryDto;

public class HeadHistoryConverter implements DtoEntityConverter<HeadHistoryDto, HeadHistory> {

    private final MemberConverter memberConverter;
    private final DepartmentConverter departmentConverter;

    public HeadHistoryConverter(MemberConverter memberConverter, DepartmentConverter departmentConverter) {
        this.memberConverter = memberConverter;
        this.departmentConverter = departmentConverter;
    }


    @Override
    public HeadHistoryDto toDto(HeadHistory headHistory) {
        return HeadHistoryDto.builder().
                id(headHistory.getId()).
                startDate(headHistory.getStartDate()).
                endDate(headHistory.getEndDate()).
                department(departmentConverter.toDto(headHistory.getDepartment())).
                head(memberConverter.toDto(headHistory.getMember())).
                build();
    }

    @Override
    public HeadHistory toEntity(HeadHistoryDto headHistoryDto) {
        return HeadHistory.builder().
                id(headHistoryDto.getId()).
                startDate(headHistoryDto.getStartDate()).
                endDate(headHistoryDto.getEndDate()).
                department(departmentConverter.toEntity(headHistoryDto.getDepartment())).
                member(memberConverter.toEntity(headHistoryDto.getHead())).
                build();
    }
}
