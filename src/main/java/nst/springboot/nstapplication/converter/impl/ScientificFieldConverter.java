package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.ScientificField;
import nst.springboot.nstapplication.dto.ScientificFieldDto;
import org.springframework.stereotype.Component;

@Component
public class ScientificFieldConverter implements DtoEntityConverter<ScientificFieldDto, ScientificField> {
    @Override
    public ScientificFieldDto toDto(ScientificField entity) {
        return ScientificFieldDto.builder().
                id(entity.getId()).
                name(entity.getName()).
                build();

    }

    @Override
    public ScientificField toEntity(ScientificFieldDto dto) {
        return ScientificField.builder().
                id(dto.getId()).
                name(dto.getName()).
                build();
    }


}
