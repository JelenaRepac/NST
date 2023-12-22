package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.EducationTitle;
import nst.springboot.nstapplication.dto.EducationTitleDTO;
import org.springframework.stereotype.Component;

@Component
public class EducationTitleConverter implements DtoEntityConverter<EducationTitleDTO, EducationTitle> {

    @Override
    public EducationTitleDTO toDto(EducationTitle entity) {
        return new EducationTitleDTO(
                entity.getId(),
                entity.getName());
    }

    @Override
    public EducationTitle toEntity(EducationTitleDTO dto) {
        return new EducationTitle(
                dto.getId(),
                dto.getName());
    }


}