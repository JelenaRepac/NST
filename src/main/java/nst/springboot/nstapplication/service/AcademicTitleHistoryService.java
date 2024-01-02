package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.dto.AcademicTitleHistoryDto;

import java.util.List;
import java.util.Map;

public interface AcademicTitleHistoryService {

    AcademicTitleHistoryDto save(AcademicTitleHistoryDto academicTitleHistoryDto);
    List<AcademicTitleHistoryDto> getAll();
    AcademicTitleHistoryDto findById(Long id);
    List<AcademicTitleHistoryDto> findByMemberId(Long id);
}
