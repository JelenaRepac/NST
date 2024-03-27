package nst.springboot.nstapplication.service.impl;


import nst.springboot.nstapplication.converter.impl.AcademicTitleConverter;
import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.AcademicTitleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

class AcademicTitleServiceImplTest {

    @Mock
    private AcademicTitleRepository academicTitleRepository;

    @Mock
    private AcademicTitleConverter academicTitleConverter;

    @InjectMocks
    private AcademicTitleServiceImpl academicTitleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void testSaveNewAcademicTitleSuccessfullySaved() {

        AcademicTitleDto academicTitleDto = new AcademicTitleDto();
        academicTitleDto.setName("Bachelor");

        when(academicTitleRepository.findByName("Bachelor")).thenReturn(Optional.empty());
        when(academicTitleConverter.toEntity(academicTitleDto)).thenReturn(new AcademicTitle());
        when(academicTitleRepository.save(any())).thenReturn(new AcademicTitle());
        when(academicTitleConverter.toDto(any())).thenReturn(new AcademicTitleDto());

        AcademicTitleDto savedAcademicTitleDto = academicTitleService.save(academicTitleDto);

        verify(academicTitleRepository, times(1)).save(any());
        assertNotNull(savedAcademicTitleDto);
    }

    @Test
    void testSaveExistingAcademicTitleThrowsEntityAlreadyExistsException() {
        AcademicTitleDto academicTitleDto = new AcademicTitleDto();
        academicTitleDto.setName("Bachelor");

        when(academicTitleRepository.findByName("Bachelor")).thenReturn(Optional.of(new AcademicTitle()));

        assertThrows(EntityAlreadyExistsException.class, () -> academicTitleService.save(academicTitleDto));
    }


    @Test
    void testGetAll() {
        AcademicTitle academicTitle1 = new AcademicTitle(1L, "PhD");
        AcademicTitle academicTitle2 = new AcademicTitle(2L, "MSc");

        when(academicTitleRepository.findAll()).thenReturn(Arrays.asList(academicTitle1, academicTitle2));

        when(academicTitleConverter.toDto(academicTitle1)).thenReturn(new AcademicTitleDto(1L, "PhD"));
        when(academicTitleConverter.toDto(academicTitle2)).thenReturn(new AcademicTitleDto(2L, "MSc"));

        List<AcademicTitleDto> academicTitleDtoList = academicTitleService.getAll();

        verify(academicTitleRepository, times(1)).findAll();

        verify(academicTitleConverter, times(1)).toDto(academicTitle1);
        verify(academicTitleConverter, times(1)).toDto(academicTitle2);

        assertEquals(2, academicTitleDtoList.size());
        assertEquals("PhD", academicTitleDtoList.get(0).getName());
        assertEquals("MSc", academicTitleDtoList.get(1).getName());
    }
    @Test
    void testFindByIdAcademicTitleExists() {
        AcademicTitle academicTitle = new AcademicTitle(1L, "PhD");
        when(academicTitleRepository.findById(1l)).thenReturn(Optional.of(academicTitle));

        AcademicTitleDto academicTitleDto = new AcademicTitleDto(1L, "PhD");
        when(academicTitleConverter.toDto(academicTitle)).thenReturn(academicTitleDto);


        AcademicTitleDto foundTitleDto = academicTitleService.findById(1L);
        System.out.println(foundTitleDto.getName());
        verify(academicTitleConverter, times(1)).toDto(academicTitle);
        verify(academicTitleRepository,times(1)).findById(1L);

        assertNotNull(foundTitleDto);
        assertEquals(academicTitleDto.getId(), foundTitleDto.getId());
        assertEquals(academicTitleDto.getName(), foundTitleDto.getName());
    }

    @Test
    void testFindByIdAcademicTitleDoesNotExist() {
        when(academicTitleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> academicTitleService.findById(1L));

        verify(academicTitleRepository, times(1)).findById(1L);
        verify(academicTitleConverter, never()).toDto(any());
    }


    @Test
    void testPartialUpdate_AcademicTitleNotFound() {
        when(academicTitleRepository.findById(1L)).thenReturn(Optional.empty());

        Map<String, String> updates = new HashMap<>();
        updates.put("name", "PhD");

        assertThrows(EntityNotFoundException.class, () -> academicTitleService.partialUpdate(1L, updates));

        verify(academicTitleRepository, times(1)).findById(1L);

        verify(academicTitleRepository, never()).save(any());

        verify(academicTitleConverter, never()).toDto(any());
    }

}