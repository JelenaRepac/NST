package nst.springboot.nstapplication.service.impl;

import lombok.RequiredArgsConstructor;
import nst.springboot.nstapplication.converter.impl.AcademicTitleConverter;
import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.AcademicTitleRepository;
import nst.springboot.nstapplication.service.AcademicTitleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AcademicTitleServiceImpl implements AcademicTitleService {

    private final AcademicTitleConverter academicTitleConverter;
    private final AcademicTitleRepository academicTitleRepository;

    @Override
    public AcademicTitleDto save(AcademicTitleDto academicTitleDTO) {
        Optional<AcademicTitle> aTitle = academicTitleRepository.findByName(academicTitleDTO.getName());
        if (aTitle.isPresent()) {
            throw new EntityAlreadyExistsException("Academic title with that name already exists!");
        } else {
            AcademicTitle academicTitle = academicTitleConverter.toEntity(academicTitleDTO);
            return academicTitleConverter.toDto(academicTitleRepository.save(academicTitle));
        }
    }

    @Override
    public List<AcademicTitleDto> getAll() {
        return academicTitleRepository
                .findAll()
                .stream().map(entity -> academicTitleConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public AcademicTitleDto findById(Long id) {
        Optional<AcademicTitle> title = academicTitleRepository.findById(id);
        if (title.isPresent()) {
            AcademicTitle academicTitle = title.get();
            return academicTitleConverter.toDto(academicTitle);
        } else {
            throw new EntityNotFoundException("Academic title does not exist!");
        }
    }

    @Override
    public AcademicTitleDto partialUpdate(Long id, Map<String, String> updates) {
        Optional<AcademicTitle> existingAcademicTitle = academicTitleRepository.findById(id);

        if (existingAcademicTitle.isPresent()) {
            AcademicTitle academicTitle = existingAcademicTitle.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        academicTitle.setName(value);
                        break;

                }
            });
            AcademicTitle savedAcademicTitle= academicTitleRepository.save(academicTitle);
            return academicTitleConverter.toDto(savedAcademicTitle);
        } else {
            throw new EntityNotFoundException("Academic title not found with id: " + id);
        }
    }
}
