package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;

import java.util.List;

public interface HeadHistoryService {
    HeadHistoryDto save(HeadHistoryDto headHistoryDto);
    List<HeadHistoryDto> getAll();
    HeadHistoryDto getByDepartmentId(Long id);
    void delete(Long id);
    HeadHistoryDto findById(Long id);
    List<HeadHistoryDto> getHistoryForDepartmentId(Long id);
}
