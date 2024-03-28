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
public class HeadHistoryRepositoryTest {

    @Autowired
    private HeadHistoryRepository headHistoryRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MemberRepository memberRepository;
    HeadHistory headHistory;

    @BeforeEach
    public void setupTestData(){

        headHistory =HeadHistory.builder()
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
    @DisplayName("JUnit test for save head history operation")
    public void givenHeadHistoryObject_whenSave_thenReturnSaveHeadHistory(){

        HeadHistory savedHeadHistory = headHistoryRepository.save(headHistory);

        assertThat(savedHeadHistory).isNotNull();
        assertThat(savedHeadHistory.getId()).isGreaterThan(0);
    }
    @Test
    @DisplayName("JUnit test for find all head histories operation")
    public void givenHeadHistoryList_whenFindAll_thenHeadHistoryList(){

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

        HeadHistory headHistory = HeadHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(5))
                .endDate(LocalDate.now().minusYears(2))
                .build();

        headHistoryRepository.save(headHistory);


        List<HeadHistory> headHistoryList = headHistoryRepository.findAll();

        assertThat(headHistoryList).isNotNull();
        assertEquals(headHistoryList.size(), 1);
    }
    @Test
    @DisplayName("JUnit test for find by id head history operation")
    public void givenHeadHistoryId_whenFindById_thenReturnHeadHistoryIdObject(){
        HeadHistory savedHeadHistory = headHistoryRepository.save(headHistory);

        HeadHistory findHeadHistory = headHistoryRepository.findById(savedHeadHistory.getId()).get();

        assertThat(findHeadHistory).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for delete head history operation")
    public void givenHeadHistoryObject_whenDelete_thenRemoveHeadHistory() {
        headHistoryRepository.save(headHistory);

        headHistoryRepository.deleteById(headHistory.getId());
        Optional<HeadHistory> deletedHeadHistory = headHistoryRepository.findById(headHistory.getId());

        assertThat(deletedHeadHistory).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for finding current head history by member id")
    public void givenMemberId_whenFindCurrentByMemberId_thenReturnHeadHistoryObject(){

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

        HeadHistory headHistory = HeadHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(5))
                .endDate(null)
                .build();

        headHistoryRepository.save(headHistory);


        HeadHistory head= headHistoryRepository.findCurrentByMemberId(member.getId()).get();

        assertThat(head).isNotNull();
        assertEquals(head.getMember().getId(), member.getId());
        assertThat(head.getEndDate()).isNull();
    }

    @Test
    @DisplayName("JUnit test for finding current head history by department id")
    public void givenMemberId_whenFindCurrentHeadByDepartmentId_thenReturnHeadHistoryObject(){

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

        HeadHistory headHistory = HeadHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(5))
                .endDate(null)
                .build();

        headHistoryRepository.save(headHistory);


        HeadHistory head= headHistoryRepository.findCurrentHeadByDepartmentId(department.getId(), LocalDate.now()).get();

        assertThat(head).isNotNull();
        assertEquals(head.getMember().getId(), member.getId());
        assertThat(head.getEndDate()).isNull();
    }

    @Test
    @DisplayName("Test for finding head histories by department id and ordering by date")
    public void givenDepartmentId_whenFindByDepartmentIdOrderByDate_thenReturnHeadHistoryList() {

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

        HeadHistory head = HeadHistory.builder()
                .member(member)
                .department(department)
                .startDate(LocalDate.now().minusYears(5))
                .endDate(null)
                .build();

        headHistoryRepository.save(head);

        List<HeadHistory> headHistoryList = headHistoryRepository.findByDepartmentIdOrderByDate(department.getId());

        assertThat(headHistoryList).isNotNull();
        assertThat(headHistoryList.size()).isGreaterThanOrEqualTo(1);

        LocalDate previousStartDate = LocalDate.now();
        for (HeadHistory headHistory : headHistoryList) {
            assertThat(headHistory.getStartDate()).isBeforeOrEqualTo(previousStartDate);
            previousStartDate = headHistory.getStartDate();
        }
    }
}
