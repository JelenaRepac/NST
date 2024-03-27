package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AcademicTitleConverter implements DtoEntityConverter<AcademicTitleDto, AcademicTitle> {
    @Override
    public AcademicTitleDto toDto(AcademicTitle entity) {
        return new AcademicTitleDto(entity.getId(), entity.getName());
    }
    @Override
    public AcademicTitle toEntity(AcademicTitleDto dto) {
        return new AcademicTitle(dto.getId(),dto.getName());
    }


}

