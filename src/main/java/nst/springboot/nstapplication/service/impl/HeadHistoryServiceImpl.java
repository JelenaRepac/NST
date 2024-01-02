package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.HeadHistoryConverter;
import nst.springboot.nstapplication.domain.HeadHistory;
import nst.springboot.nstapplication.domain.SecretaryHistory;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.HeadHistoryRepository;
import nst.springboot.nstapplication.service.HeadHistoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HeadHistoryServiceImpl implements HeadHistoryService{
    private HeadHistoryRepository repository;

    private HeadHistoryConverter headHistoryConverter;

    public HeadHistoryServiceImpl(HeadHistoryRepository repository, HeadHistoryConverter secretaryHistoryConverter) {
        this.repository = repository;
        this.headHistoryConverter = secretaryHistoryConverter;
    }

    @Override
    public HeadHistoryDto save(HeadHistoryDto headHistoryDto){
        HeadHistory history = headHistoryConverter.toEntity(headHistoryDto);
        history = repository.save(history);
        return headHistoryConverter.toDto(history);
    }

    @Override
    public List<HeadHistoryDto> getAll() {
        return repository
                .findAll()
                .stream().map(entity -> headHistoryConverter.toDto(entity))
                .collect(Collectors.toList());
    }
    @Override
    public HeadHistoryDto getByDepartmentId(Long id){
        Optional<HeadHistory> headHistory = repository.findByDepartmentIdAndEndDateNull(id);
        if(headHistory.isEmpty()){
            throw new EntityNotFoundException("Department doesn't have active head member");
        }
        return headHistoryConverter.toDto(headHistory.get());
}
    @Override
    public void delete(Long id)  {
        Optional<HeadHistory> history = repository.findById(id);
        if (history.isPresent()) {
            HeadHistory history1 = history.get();
            repository.delete(history1);
        } else {
            throw new EntityNotFoundException("Head history does not exists!");
        }
    }

    @Override
    public HeadHistoryDto findById(Long id) {
        Optional<HeadHistory> history = repository.findById(id);
        if (history.isPresent()) {
            HeadHistory history1 = history.get();
            return headHistoryConverter.toDto(history1);
        } else {
            throw new EntityNotFoundException("Head history does not exists!");
        }
    }

    @Override
    public List<HeadHistoryDto> getHistoryForDepartmentId(Long id) {
        List<HeadHistory> headHistoryList = repository.findByDepartmentId(id);
        if (headHistoryList.isEmpty()) {
            throw new EmptyResponseException("There are no head history for department!");
        }
        List<HeadHistoryDto> headHistoryDtoList = new ArrayList<>();
        for (HeadHistory hh : headHistoryList) {
            headHistoryDtoList.add(headHistoryConverter.toDto(hh));
        }
        return headHistoryDtoList;

    }
}
