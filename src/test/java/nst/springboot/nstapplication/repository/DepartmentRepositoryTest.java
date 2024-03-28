package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    Department department;

    @BeforeEach
    public void setupTestData() {
        department = Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build();
    }

    @Test
    @DisplayName("JUnit test for save department operation")
    public void givenDepartmentObject_whenSave_thenReturnSavedDepartment() {
        Department savedDepartment = departmentRepository.save(department);

        assertThat(savedDepartment).isNotNull();
        assertThat(savedDepartment.getId()).isGreaterThan(0);
        assertEquals(savedDepartment.getName(), department.getName());
        assertEquals(savedDepartment.getShortName(), department.getShortName());
    }

    @Test
    @DisplayName("JUnit test for find all departments operation")
    public void givenDepartmentList_whenFindAll_thenReturnDepartmentList() {
        Department department1 = Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build();
        Department department2 = Department.builder().name("Katedra za elektronsko poslovanje").shortName("ELAB").build();

        departmentRepository.save(department1);
        departmentRepository.save(department2);

        List<Department> departments = departmentRepository.findAll();

        assertThat(departments).isNotNull();
        assertEquals(2, departments.size());
    }

    @Test
    @DisplayName("JUnit test for find by id department operation")
    public void givenDepartmentId_whenFindById_thenReturnDepartmentObject() {
        Department savedDepartment = departmentRepository.save(department);

        Department foundDepartment = departmentRepository.findById(savedDepartment.getId()).get();

        assertThat(foundDepartment).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for delete department operation")
    public void givenDepartmentObject_whenDelete_thenRemoveDepartment() {
        departmentRepository.save(department);

        departmentRepository.deleteById(department.getId());
        Optional<Department> deletedDepartment = departmentRepository.findById(department.getId());

        assertThat(deletedDepartment).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for finding department by name")
    public void givenDepartmentName_whenFindByName_thenReturnDepartmentObject() {
        departmentRepository.save(department);

        Department departmentDb = departmentRepository.findByName("Katedra za informacione tehnologije").get();

        assertEquals("Katedra za informacione tehnologije", departmentDb.getName());
        assertEquals("IS", departmentDb.getShortName());
    }
}