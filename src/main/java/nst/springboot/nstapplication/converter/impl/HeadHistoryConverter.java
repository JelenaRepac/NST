package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.HeadHistory;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
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
                endDate(headHistory.getEndDate()==null ? null :headHistory.getEndDate()).
                department(departmentConverter.toDto(headHistory.getDepartment())).
                head(memberConverter.toDto(headHistory.getMember())).
                build();
    }

    @Override
    public HeadHistory toEntity(HeadHistoryDto headHistoryDto) {
        return HeadHistory.builder()
                .id(Optional.ofNullable(headHistoryDto.getId()).orElse(null))
                .startDate(headHistoryDto.getStartDate())
                .endDate(headHistoryDto.getEndDate()==null ? null :headHistoryDto.getEndDate())
                .member(memberConverter.toEntity(headHistoryDto.getHead()))
                .department(departmentConverter.toEntity(headHistoryDto.getDepartment()))
                .build();
    }

    public List<HeadHistoryDto> toDtoList(List<HeadHistory> headHistoryList) {
        List<HeadHistoryDto> headHistoryDtoList= new ArrayList<>();
        for(HeadHistory headHistory: headHistoryList){
            headHistoryDtoList.add(toDto(headHistory));
        }
        return headHistoryDtoList;
    }
    public List<HeadHistory> toEntityList(List<HeadHistoryDto> headHistoryDtoList) {
        List<HeadHistory> headHistoryList= new ArrayList<>();
        for(HeadHistoryDto headHistoryDto: headHistoryDtoList){
            headHistoryList.add(toEntity(headHistoryDto));
        }
        return headHistoryList;
    }
}
