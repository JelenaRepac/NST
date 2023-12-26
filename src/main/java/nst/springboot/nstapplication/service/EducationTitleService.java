package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.dto.EducationTitleDto;

import java.util.List;
import java.util.Map;

public interface EducationTitleService {

    EducationTitleDto save(EducationTitleDto educationTitleDTO);
    List<EducationTitleDto> getAll();
    void delete(Long id);
    EducationTitleDto findById(Long id);

    EducationTitleDto partialUpdate(Long id, Map<String, String> updates);

}
