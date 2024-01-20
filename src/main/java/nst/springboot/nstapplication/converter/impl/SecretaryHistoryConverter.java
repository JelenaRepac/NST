package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.SecretaryHistory;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
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
        return SecretaryHistory.builder()
                .id(Optional.ofNullable(secretaryHistoryDto.getId()).orElse(null))
                .startDate(secretaryHistoryDto.getStartDate())
                .endDate(secretaryHistoryDto.getEndDate())
                .member(memberConverter.toEntity(secretaryHistoryDto.getMember()))
                .department(departmentConverter.toEntity(secretaryHistoryDto.getDepartment()))
                .build();
    }

    public List<SecretaryHistory> toEntityList (List<SecretaryHistoryDto> secretaryHistoryDtoList){
        List<SecretaryHistory> secretaryHistoryList= new ArrayList<>();
        for(SecretaryHistoryDto secretaryHistoryDto : secretaryHistoryDtoList){
            secretaryHistoryList.add(toEntity(secretaryHistoryDto));
        }
        return secretaryHistoryList;
    }

    public List<SecretaryHistoryDto> toDtoList (List<SecretaryHistory> secretaryHistoryList){
        List<SecretaryHistoryDto> secretaryHistoryDtoList= new ArrayList<>();
        for(SecretaryHistory secretaryHistory : secretaryHistoryList){
            secretaryHistoryDtoList.add(toDto(secretaryHistory));
        }
        return secretaryHistoryDtoList;
    }
}
