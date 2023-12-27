package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.EducationTitleConverter;
import nst.springboot.nstapplication.converter.impl.ScientificFieldConverter;
import nst.springboot.nstapplication.domain.EducationTitle;
import nst.springboot.nstapplication.domain.ScientificField;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import nst.springboot.nstapplication.dto.ScientificFieldDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.EducationTitleRepository;
import nst.springboot.nstapplication.repository.ScientificFieldRepository;
import nst.springboot.nstapplication.service.ScientificFieldService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ScientificFieldServiceImpl implements ScientificFieldService {

    private ScientificFieldConverter scientificFieldConverter;
    private ScientificFieldRepository scientificFieldRepository;

    public ScientificFieldServiceImpl(ScientificFieldConverter scientificFieldConverter, ScientificFieldRepository scientificFieldRepository) {
        this.scientificFieldConverter = scientificFieldConverter;
        this.scientificFieldRepository = scientificFieldRepository;
    }

    @Override
    public ScientificFieldDto save(ScientificFieldDto educationTitleDTO){
        Optional<ScientificField> scField = scientificFieldRepository.findByName(educationTitleDTO.getName());
        if (scField.isPresent()) {
            throw new EntityAlreadyExistsException("Scientific fields with that name already exists!");
        } else {
            ScientificField scientificField = scientificFieldConverter.toEntity(educationTitleDTO);
            return scientificFieldConverter.toDto(scientificFieldRepository.save(scientificField));
        }
    }

    @Override
    public List<ScientificFieldDto> getAll() {
        return scientificFieldRepository
                .findAll()
                .stream().map(entity -> scientificFieldConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public ScientificFieldDto findById(Long id) {
        Optional<ScientificField> scientificField = scientificFieldRepository.findById(id);
        if (scientificField.isPresent()) {
            return scientificFieldConverter.toDto(scientificField.get());
        } else {
            throw new EntityNotFoundException("Scientific field does not exist!");
        }
    }

    @Override
    public ScientificFieldDto partialUpdate(Long id, Map<String, String> updates) {
        Optional<ScientificField> existingScientificField = scientificFieldRepository.findById(id);

        if (existingScientificField.isPresent()) {
            ScientificField scientificField = existingScientificField.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        scientificField.setName(value);
                        break;

                }
            });
            scientificFieldRepository.save(scientificField);
            return scientificFieldConverter.toDto(scientificField);
        } else {
            throw new EntityNotFoundException("Scientific field not found with id: " + id);
        }
    }

}
