package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.domain.ScientificField;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import nst.springboot.nstapplication.dto.ScientificFieldDto;

import java.util.List;
import java.util.Map;

public interface ScientificFieldService {

    ScientificFieldDto save(ScientificFieldDto scientificFieldDto);
    List<ScientificFieldDto> getAll();
    ScientificFieldDto findById(Long id);
    ScientificFieldDto partialUpdate(Long id, Map<String, String> updates);

}
