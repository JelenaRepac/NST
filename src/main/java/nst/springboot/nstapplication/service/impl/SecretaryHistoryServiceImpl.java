package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.SecretaryHistory;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.SecretaryHistoryRepository;
import nst.springboot.nstapplication.service.SecretaryHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecretaryHistoryServiceImpl implements SecretaryHistoryService {
    private SecretaryHistoryRepository repository;

    private SecretaryHistoryConverter secretaryHistoryConverter;

    public SecretaryHistoryServiceImpl(SecretaryHistoryRepository repository, SecretaryHistoryConverter secretaryHistoryConverter) {
        this.repository = repository;
        this.secretaryHistoryConverter = secretaryHistoryConverter;
    }


    @Override
    public SecretaryHistoryDto save(SecretaryHistoryDto secretaryHistoryDTO){
        SecretaryHistory history = secretaryHistoryConverter.toEntity(secretaryHistoryDTO);
        history = repository.save(history);
        return secretaryHistoryConverter.toDto(history);
    }

    @Override
    public List<SecretaryHistoryDto> getAll() {
        return repository
                .findAll()
                .stream().map(entity -> secretaryHistoryConverter.toDto(entity))
                .collect(Collectors.toList());
    }
    @Override
    public SecretaryHistoryDto getByDepartmentId(Long id){
        Optional<SecretaryHistory> secretaryHistory = repository.findByDepartmentIdAndEndDateNull(id);
        if(secretaryHistory.isEmpty()){
            throw new EntityNotFoundException("Department doesn't have active secretary member");
        }
        return secretaryHistoryConverter.toDto(secretaryHistory.get());
    }

    @Override
    public void delete(Long id)  {
        Optional<SecretaryHistory> history = repository.findById(id);
        if (history.isPresent()) {
            SecretaryHistory history1 = history.get();
            repository.delete(history1);
        } else {
            throw new EntityNotFoundException("Secretary history does not exist!");
        }
    }

    @Override
    public SecretaryHistoryDto findById(Long id) {
        Optional<SecretaryHistory> history = repository.findById(id);
        if (history.isPresent()) {
            SecretaryHistory history1 = history.get();
            return secretaryHistoryConverter.toDto(history1);
        } else {
            throw new EntityNotFoundException("Secretary history not exist!");
        }
    }
}
