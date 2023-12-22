package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.domain.ScientificField;
import nst.springboot.nstapplication.dto.ScientificFieldDTO;
import org.springframework.stereotype.Component;

@Component
public class ScientificFieldConverter implements DtoEntityConverter<ScientificFieldDTO, ScientificField> {
    @Override
    public ScientificFieldDTO toDto(ScientificField entity) {
        return new ScientificFieldDTO(
                entity.getId(),
                entity.getName());
    }

    @Override
    public ScientificField toEntity(ScientificFieldDTO dto) {
        return new ScientificField(
                dto.getId(),
                dto.getName());
    }


}
