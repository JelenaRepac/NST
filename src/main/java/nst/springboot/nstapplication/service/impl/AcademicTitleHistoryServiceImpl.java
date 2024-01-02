package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.AcademicTitleConverter;
import nst.springboot.nstapplication.converter.impl.AcademicTitleHistoryConverter;
import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.domain.AcademicTitleHistory;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.dto.AcademicTitleHistoryDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.AcademicTitleHistoryRepository;
import nst.springboot.nstapplication.repository.AcademicTitleRepository;
import nst.springboot.nstapplication.service.AcademicTitleHistoryService;
import nst.springboot.nstapplication.service.AcademicTitleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcademicTitleHistoryServiceImpl implements AcademicTitleHistoryService {
    private AcademicTitleHistoryConverter academicTitleConverter;
    private AcademicTitleHistoryRepository academicTitleRepository;

    public AcademicTitleHistoryServiceImpl(AcademicTitleHistoryConverter academicTitleConverter, AcademicTitleHistoryRepository academicTitleRepository) {
        this.academicTitleConverter = academicTitleConverter;
        this.academicTitleRepository = academicTitleRepository;
    }

    @Override
    public AcademicTitleHistoryDto save(AcademicTitleHistoryDto academicTitleDTO) {
        return null;
    }

    @Override
    public List<AcademicTitleHistoryDto> getAll() {
        return academicTitleRepository
                .findAll()
                .stream().map(entity -> academicTitleConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public AcademicTitleHistoryDto findById(Long id) {
        Optional<AcademicTitleHistory> title = academicTitleRepository.findById(id);
        if (title.isPresent()) {
            AcademicTitleHistory academicTitle = title.get();
            return academicTitleConverter.toDto(academicTitle);
        } else {
            throw new EntityNotFoundException("Academic title history does not exist!");
        }
    }
    @Override
    public List<AcademicTitleHistoryDto> findByMemberId(Long id) {
        List<AcademicTitleHistory>  academicTitleHistoryList= academicTitleRepository.findAllByMemberIdOrderByStartDateDesc(id);
        List<AcademicTitleHistoryDto> academicTitleHistoryDtoList= new ArrayList<>();
        if (!academicTitleHistoryList.isEmpty()){
            for (AcademicTitleHistory ac : academicTitleHistoryList) {
                academicTitleHistoryDtoList.add(academicTitleConverter.toDto(ac));
            }
        } else {
            throw new EntityNotFoundException("Academic title history does not exist!");
        }
        return academicTitleHistoryDtoList;
    }

}
