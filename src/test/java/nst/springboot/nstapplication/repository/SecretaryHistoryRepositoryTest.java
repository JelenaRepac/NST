package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
@DataJpaTest
public class SecretaryHistoryRepositoryTest {

    @Autowired
    private SecretaryHistoryRepository secretaryHistoryRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MemberRepository memberRepository;
    SecretaryHistory secretaryHistory;

    @BeforeEach
    public void setupTestData(){

        secretaryHistory =SecretaryHistory.builder()
                .member(Member.builder()
                        .firstname("Jelena")
                        .lastname("Repac")
                        .educationTitle(EducationTitle.builder().name("Associate degree").build())
                        .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                        .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                        .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                        .role(Role.builder().name("Default").build())
                        .build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(2)).build();
    }

    @Test
    @DisplayName("JUnit test for save secretary history operation")
    public void givenSecretaryHistoryObject_whenSave_thenReturnSaveSecretaryHistory(){

        SecretaryHistory savedSecretaryHistory = secretaryHistoryRepository.save(secretaryHistory);

        assertThat(savedSecretaryHistory).isNotNull();
        assertThat(savedSecretaryHistory.getId()).isGreaterThan(0);
    }
    @Test
    @DisplayName("JUnit test for find all secretary histories operation")
    public void givenSecretaryHistoryList_whenFindAll_thenSecretaryHistoryList(){

        Department department = Department.builder()
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();
        departmentRepository.save(department);

        Member member = Member.builder()
                .firstname("Jovan")
                .lastname("Ciric")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(department)
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();
        memberRepository.save(member);

        SecretaryHistory secretaryHistory = SecretaryHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(5))
                .endDate(LocalDate.now().minusYears(2))
                .build();

        secretaryHistoryRepository.save(secretaryHistory);


        List<SecretaryHistory> secretaryHistories = secretaryHistoryRepository.findAll();

        assertThat(secretaryHistories).isNotNull();
        assertEquals(secretaryHistories.size(), 1);
    }
    @Test
    @DisplayName("JUnit test for find by id secretary history operation")
    public void givenSecretaryHistoryId_whenFindById_thenReturnSecretaryHistoryIdObject(){
        SecretaryHistory savedSecretaryHistory = secretaryHistoryRepository.save(secretaryHistory);

        SecretaryHistory findSecretaryHistory = secretaryHistoryRepository.findById(savedSecretaryHistory.getId()).get();

        assertThat(findSecretaryHistory).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for delete secretary history operation")
    public void givenSecretaryHistoryObject_whenDelete_thenRemoveSecretaryHistory() {
        secretaryHistoryRepository.save(secretaryHistory);

        secretaryHistoryRepository.deleteById(secretaryHistory.getId());
        Optional<SecretaryHistory> deletedSecretaryHistory = secretaryHistoryRepository.findById(secretaryHistory.getId());

        assertThat(deletedSecretaryHistory).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for finding current secretary history by member id")
    public void givenMemberId_whenFindCurrentByMemberId_thenReturnSecretaryHistoryObject(){

        //end date is null, sending member id

        Department department = Department.builder()
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();
        departmentRepository.save(department);

        Member member = Member.builder()
                .firstname("Jovan")
                .lastname("Ciric")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(department)
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();
        memberRepository.save(member);

        SecretaryHistory secretaryHistory = SecretaryHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(5))
                .endDate(null)
                .build();

        secretaryHistoryRepository.save(secretaryHistory);


        SecretaryHistory secretary= secretaryHistoryRepository.findCurrentByMemberId(member.getId(), LocalDate.now()).get();

        assertThat(secretary).isNotNull();
        assertEquals(secretary.getMember().getId(), member.getId());
        assertThat(secretary.getEndDate()).isNull();
    }

    @Test
    @DisplayName("JUnit test for finding current secretary history by department id")
    public void givenMemberId_whenFindCurrentSecretaryByDepartmentId_thenReturnSecretaryHistoryObject(){

        //end date is null, sending department id

        Department department = Department.builder()
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();
        departmentRepository.save(department);

        Member member = Member.builder()
                .firstname("Jovan")
                .lastname("Ciric")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(department)
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();
        memberRepository.save(member);

        SecretaryHistory secretaryHistory = SecretaryHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(5))
                .endDate(null)
                .build();

        secretaryHistoryRepository.save(secretaryHistory);


        SecretaryHistory secretary= secretaryHistoryRepository.findCurrentSecretaryByDepartmentId(department.getId(), LocalDate.now()).get();

        assertThat(secretary).isNotNull();
        assertEquals(secretary.getMember().getId(), member.getId());
        assertThat(secretary.getEndDate()).isNull();
    }

    @Test
    @DisplayName("Test for finding secretary histories by department id and ordering by date")
    public void givenDepartmentId_whenFindByDepartmentIdOrderByDate_thenReturnSecretaryHistoryList() {

        Department department = Department.builder()
                .name("Katedra za informacione tehnologije")
                .shortName("IS")
                .build();
        departmentRepository.save(department);

        Member member = Member.builder()
                .firstname("Jovan")
                .lastname("Ciric")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(department)
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();
        memberRepository.save(member);

        SecretaryHistory secretary = SecretaryHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(5))
                .endDate(null)
                .build();

        secretaryHistoryRepository.save(secretary);

        List<SecretaryHistory> secretaryHistories = secretaryHistoryRepository.findByDepartmentIdOrderByDate(department.getId());

        assertThat(secretaryHistories).isNotNull();
        assertThat(secretaryHistories.size()).isGreaterThanOrEqualTo(1);

        LocalDate previousStartDate = LocalDate.now();
        for (SecretaryHistory secretaryHistory : secretaryHistories) {
            assertThat(secretaryHistory.getStartDate()).isBeforeOrEqualTo(previousStartDate);
            previousStartDate = secretaryHistory.getStartDate();
        }
    }
}
