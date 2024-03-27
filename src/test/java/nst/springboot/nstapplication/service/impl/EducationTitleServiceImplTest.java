package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.EducationTitleConverter;
import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.domain.EducationTitle;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.EducationTitleRepository;
import nst.springboot.nstapplication.service.EducationTitleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EducationTitleServiceImplTest {

    @Mock
    private EducationTitleConverter educationTitleConverter;
    @Mock
    private EducationTitleRepository educationTitleRepository;

    @InjectMocks
    private EducationTitleServiceImpl educationTitleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveNewEducationTitleSuccessfullySaved() {
        EducationTitleDto educationTitleDto = new EducationTitleDto();
        educationTitleDto.setName("Bachelor's degreee");

        when(educationTitleRepository.findByName("Bachelor's degree'")).thenReturn(Optional.empty());
        when(educationTitleConverter.toEntity(educationTitleDto)).thenReturn(new EducationTitle());
        when(educationTitleRepository.save(any())).thenReturn(new EducationTitle());
        when(educationTitleConverter.toDto(any())).thenReturn(new EducationTitleDto());

        EducationTitleDto savedEducationTitleDto = educationTitleService.save(educationTitleDto);

        verify(educationTitleRepository, times(1)).save(any());
        assertNotNull(savedEducationTitleDto);
    }

    @Test
    void testSaveExistingEducationTitleThrowsEntityAlreadyExistsException() {
        EducationTitleDto educationTitleDto = new EducationTitleDto();
        educationTitleDto.setName("Bachelor's degree'");

        when(educationTitleRepository.findByName("Bachelor's degree'")).thenReturn(Optional.of(new EducationTitle()));

        assertThrows(EntityAlreadyExistsException.class, () -> educationTitleService.save(educationTitleDto));
    }

    @Test
    void testGetAll() {
        EducationTitle educationTitle = EducationTitle.builder().id(1L).name("Bachelor's degree").build();
        EducationTitle educationTitle1 = EducationTitle.builder().id(2L).name("Master's degree").build();

        when(educationTitleRepository.findAll()).thenReturn(Arrays.asList(educationTitle,educationTitle1));

        when(educationTitleConverter.toDto(educationTitle)).thenReturn(new EducationTitleDto(1L, "Bachelor's degree"));
        when(educationTitleConverter.toDto(educationTitle1)).thenReturn(new EducationTitleDto(2L, "Master's degree"));

        List<EducationTitleDto> educationTitleDtos = educationTitleService.getAll();

        verify(educationTitleRepository, times(1)).findAll();

        verify(educationTitleConverter, times(1)).toDto(educationTitle);
        verify(educationTitleConverter, times(1)).toDto(educationTitle1);

        assertEquals(2, educationTitleDtos.size());
        assertEquals("Bachelor's degree", educationTitleDtos.get(0).getName());
        assertEquals("Master's degree", educationTitleDtos.get(1).getName());

    }

    @Test
    void testFindByIdEducationTitleExists() {
        EducationTitle educationTitle = new EducationTitle(1L, "Bachelor's degree'");
        when(educationTitleRepository.findById(1L)).thenReturn(Optional.of(educationTitle));

        EducationTitleDto educationTitleDto = new EducationTitleDto(1L, "Bachelor's degree'");
        when(educationTitleConverter.toDto(educationTitle)).thenReturn(educationTitleDto);

        EducationTitleDto foundTitleDto = educationTitleService.findById(1L);

        verify(educationTitleConverter, times(1)).toDto(educationTitle);
        verify(educationTitleRepository, times(1)).findById(1L);

        assertNotNull(foundTitleDto);
        assertEquals(educationTitleDto.getId(), foundTitleDto.getId());
        assertEquals(educationTitleDto.getName(), foundTitleDto.getName());
    }

    @Test
    void testFindByIdEducationTitleDoesNotExist() {
        when(educationTitleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> educationTitleService.findById(1L));

        verify(educationTitleRepository, times(1)).findById(1L);
        verify(educationTitleConverter, never()).toDto(any());
    }

    @Test
    void testPartialUpdateEducationTitleNotFound() {
        when(educationTitleRepository.findById(1L)).thenReturn(Optional.empty());

        Map<String, String> updates = new HashMap<>();
        updates.put("name", "Bachelor's degree");

        assertThrows(EntityNotFoundException.class, () -> educationTitleService.partialUpdate(1L, updates));

        verify(educationTitleRepository, times(1)).findById(1L);
        verify(educationTitleRepository, never()).save(any());
        verify(educationTitleConverter, never()).toDto(any());
    }
}
