package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.domain.AcademicTitleHistory;
import nst.springboot.nstapplication.dto.AcademicTitleHistoryDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AcademicTitleHistoryConverter implements DtoEntityConverter<AcademicTitleHistoryDto, AcademicTitleHistory> {

    private final MemberConverter memberConverter;
    private final AcademicTitleConverter academicTitleConverter;
    private final ScientificFieldConverter scientificFieldConverter;

    public AcademicTitleHistoryConverter(MemberConverter memberConverter, AcademicTitleConverter academicTitleConverter, ScientificFieldConverter scientificFieldConverter) {
        this.memberConverter = memberConverter;
        this.academicTitleConverter = academicTitleConverter;
        this.scientificFieldConverter = scientificFieldConverter;
    }

    @Override
    public AcademicTitleHistoryDto toDto(AcademicTitleHistory academicTitleHistory) {
        return AcademicTitleHistoryDto.builder().
                id(academicTitleHistory.getId()).
                startDate(academicTitleHistory.getStartDate()).
                endDate(academicTitleHistory.getEndDate()).
                member(memberConverter.toDto(academicTitleHistory.getMember())).
                academicTitle(academicTitleConverter.toDto(academicTitleHistory.getAcademicTitle())).
                scientificField(scientificFieldConverter.toDto(academicTitleHistory.getScientificField())).
                build();
    }

    @Override
    public AcademicTitleHistory toEntity(AcademicTitleHistoryDto academicTitleHistoryDto) {
        return AcademicTitleHistory.builder().
                id(Optional.ofNullable(academicTitleHistoryDto.getId()).orElse(null)).
                startDate(academicTitleHistoryDto.getStartDate()).
                endDate(academicTitleHistoryDto.getEndDate()).
                member(memberConverter.toEntity(academicTitleHistoryDto.getMember())).
                academicTitle(academicTitleConverter.toEntity(academicTitleHistoryDto.getAcademicTitle())).
                scientificField(scientificFieldConverter.toEntity(academicTitleHistoryDto.getScientificField())).
                build();
    }
}
