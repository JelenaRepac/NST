package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.SecretaryHistoryDto;

import java.util.List;

public interface SecretaryHistoryService {
    SecretaryHistoryDto save(SecretaryHistoryDto secretaryHistoryDTO);
    List<SecretaryHistoryDto> getAll();
    SecretaryHistoryDto getByDepartmentId(Long id);
    void delete(Long id);
    SecretaryHistoryDto findById(Long id);
    List<SecretaryHistoryDto> getHistoryForDepartmentId(Long id);
    SecretaryHistoryDto patchSecretaryHistory(Long id, SecretaryHistoryDto secretaryHistoryDto);
}
