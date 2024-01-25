package nst.springboot.nstapplication.service.impl;

import lombok.RequiredArgsConstructor;
import nst.springboot.nstapplication.converter.impl.EducationTitleConverter;
import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.domain.EducationTitle;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.EducationTitleRepository;
import nst.springboot.nstapplication.service.EducationTitleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EducationTitleServiceImpl implements EducationTitleService {

    private final EducationTitleConverter educationTitleConverter;
    private final EducationTitleRepository educationTitleRepository;


    @Override
    public EducationTitleDto save(EducationTitleDto educationTitleDTO){
        Optional<EducationTitle> eTitle = educationTitleRepository.findByName(educationTitleDTO.getName());
        if (eTitle.isPresent()) {
            throw new EntityAlreadyExistsException("Education title with that name already exists!");
        } else {
            EducationTitle educationTitle = educationTitleConverter.toEntity(educationTitleDTO);
            return educationTitleConverter.toDto(educationTitleRepository.save(educationTitle));
        }
    }

    @Override
    public List<EducationTitleDto> getAll() {
        return educationTitleRepository
                .findAll()
                .stream().map(entity -> educationTitleConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public EducationTitleDto findById(Long id) {
        Optional<EducationTitle> title = educationTitleRepository.findById(id);
        if (title.isPresent()) {
            EducationTitle educationTitle = title.get();
            return educationTitleConverter.toDto(educationTitle);
        } else {
            throw new EntityNotFoundException("Education title does not exist!");
        }
    }

    @Override
    public EducationTitleDto partialUpdate(Long id, Map<String, String> updates) {
        Optional<EducationTitle> existingEducationTitle = educationTitleRepository.findById(id);

        if (existingEducationTitle.isPresent()) {
            EducationTitle educationTitle = existingEducationTitle.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        educationTitle.setName(value);
                        break;

                }
            });
            educationTitleRepository.save(educationTitle);
            return educationTitleConverter.toDto(educationTitle);
        } else {
            throw new EntityNotFoundException("Education title not found with id: " + id);
        }
    }


}
