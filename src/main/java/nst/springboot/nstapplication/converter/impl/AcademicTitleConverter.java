package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.dto.AcademicTitleDTO;
import org.springframework.stereotype.Component;

@Component
public class AcademicTitleConverter implements DtoEntityConverter<AcademicTitleDTO, AcademicTitle> {
    @Override
    public AcademicTitleDTO toDto(AcademicTitle entity) {
        return new AcademicTitleDTO(
                entity.getId(),
                entity.getName());
    }

    @Override
    public AcademicTitle toEntity(AcademicTitleDTO dto) {
        return new AcademicTitle(
                dto.getId(),
                dto.getName());
    }

}

