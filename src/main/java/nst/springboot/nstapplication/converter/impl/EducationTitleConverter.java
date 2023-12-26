package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.EducationTitle;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import org.springframework.stereotype.Component;

@Component
public class EducationTitleConverter implements DtoEntityConverter<EducationTitleDto, EducationTitle> {

    @Override
    public EducationTitleDto toDto(EducationTitle entity) {
        return EducationTitleDto.builder().
                id(entity.getId()).
                name(entity.getName()).
                build();
    }

    @Override
    public EducationTitle toEntity(EducationTitleDto dto) {
        return EducationTitle.builder().
                id(dto.getId()).
                name(dto.getName()).
                build();
    }


}