package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.AcademicTitleDto;

import java.util.List;
import java.util.Map;

public interface AcademicTitleService {

    AcademicTitleDto save(AcademicTitleDto academicTitleDTO);
    List<AcademicTitleDto> getAll();
    AcademicTitleDto findById(Long id);
    AcademicTitleDto partialUpdate(Long id, Map<String, String> updates);
}
