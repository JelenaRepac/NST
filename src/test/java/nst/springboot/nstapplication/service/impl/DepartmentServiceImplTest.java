package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.converter.impl.HeadHistoryConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.domain.EducationTitle;
import nst.springboot.nstapplication.dto.DepartmentDto;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import nst.springboot.nstapplication.repository.DepartmentRepository;
import nst.springboot.nstapplication.repository.HeadHistoryRepository;
import nst.springboot.nstapplication.repository.MemberRepository;
import nst.springboot.nstapplication.repository.SecretaryHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private  DepartmentConverter departmentConverter;
    @Mock
    private  DepartmentRepository departmentRepository;
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    @BeforeEach
    public void setup(){
        department = Department.builder()
                .id(1L)
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();
    }
    @Test
    @DisplayName("JUnit test for saveDepartment method")
    void testSaveNewDepartmentSuccessfullySaved() {
        DepartmentDto departmentDto = DepartmentDto.builder()
                .id(1L)
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();

        when(departmentRepository.findByName("Katedra za informacione tehnologije")).thenReturn(Optional.empty());

        Department departmentEntity = new Department();
        when(departmentConverter.toEntity(departmentDto)).thenReturn(departmentEntity);
        when(departmentRepository.save(any())).thenReturn(new Department());
        when(departmentConverter.toDto(any())).thenReturn(new DepartmentDto());
        DepartmentDto savedDepartment = departmentService.save(departmentDto);
        verify(departmentRepository, times(1)).save(departmentEntity);

        assertNotNull(savedDepartment);
    }

    @Test
    void testSaveNewDepartmentThrowsEntityAlreadyExistsException(){
        DepartmentDto departmentDto = DepartmentDto.builder()
                .id(1L)
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();

        when(departmentRepository.findByName("Katedra za informacione tehnologije")).thenReturn(Optional.of(new Department()));

        assertThrows(EntityAlreadyExistsException.class,()-> departmentService.save(departmentDto));


    }


}
