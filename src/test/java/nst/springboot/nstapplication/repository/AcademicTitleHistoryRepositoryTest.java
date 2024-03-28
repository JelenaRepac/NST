package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AcademicTitleHistoryRepositoryTest {

    @Autowired
    private AcademicTitleHistoryRepository academicTitleHistoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AcademicTitleRepository academicTitleRepository;

    @Autowired
    private ScientificFieldRepository scientificFieldRepository;

    AcademicTitleHistory academicTitleHistory;
    Member member;
    AcademicTitle academicTitle;
    ScientificField scientificField;

    @BeforeEach
    public void setupTestData() {
        member = Member.builder()
                .firstname("Jelena")
                .lastname("Repac")
                .educationTitle(EducationTitle.builder().name("Associate degree").build())
                .academicTitle(AcademicTitle.builder().name("Teaching Assistant").build())
                .department(Department.builder().name("Katedra za informacione tehnologije").shortName("IS").build())
                .scientificField(ScientificField.builder().name("Artificial intelligence").build())
                .role(Role.builder().name("Default").build())
                .build();
        memberRepository.save(member);

        academicTitle =AcademicTitle.builder().name("Teaching Assistant").build();
        academicTitleRepository.save(academicTitle);

        scientificField = ScientificField.builder().name("Artificial intelligence").build();
        scientificFieldRepository.save(scientificField);

        academicTitleHistory = AcademicTitleHistory.builder()
                .startDate(LocalDate.now().minusYears(2))
                .member(member)
                .academicTitle(academicTitle)
                .scientificField(scientificField)
                .build();
    }

    @Test
    @DisplayName("JUnit test for save academic title history operation")
    public void givenAcademicTitleHistoryObject_whenSave_thenReturnSavedAcademicTitleHistory() {
        AcademicTitleHistory savedAcademicTitleHistory = academicTitleHistoryRepository.save(academicTitleHistory);

        assertThat(savedAcademicTitleHistory).isNotNull();
        assertThat(savedAcademicTitleHistory.getId()).isGreaterThan(0);
        assertEquals(savedAcademicTitleHistory.getStartDate(), academicTitleHistory.getStartDate());
        assertEquals(savedAcademicTitleHistory.getMember(), academicTitleHistory.getMember());
        assertEquals(savedAcademicTitleHistory.getAcademicTitle(), academicTitleHistory.getAcademicTitle());
        assertEquals(savedAcademicTitleHistory.getScientificField(), academicTitleHistory.getScientificField());
    }

    @Test
    @DisplayName("JUnit test for find all academic title history operation")
    public void givenAcademicTitleHistoryList_whenFindAll_thenReturnAcademicTitleHistoryList() {
        AcademicTitleHistory academicTitleHistory1 = AcademicTitleHistory.builder()
                .startDate(LocalDate.now().minusYears(1))
                .member(member)
                .academicTitle(academicTitle)
                .scientificField(scientificField)
                .build();
        AcademicTitleHistory academicTitleHistory2 = AcademicTitleHistory.builder()
                .startDate(LocalDate.now().minusYears(2))
                .member(member)
                .academicTitle(academicTitle)
                .scientificField(scientificField)
                .build();

        academicTitleHistoryRepository.save(academicTitleHistory1);
        academicTitleHistoryRepository.save(academicTitleHistory2);

        List<AcademicTitleHistory> academicTitleHistories = academicTitleHistoryRepository.findAll();

        assertThat(academicTitleHistories).isNotNull();
        assertEquals(2, academicTitleHistories.size());
    }

    @Test
    @DisplayName("JUnit test for find by id academic title history operation")
    public void givenAcademicTitleHistoryId_whenFindById_thenReturnAcademicTitleHistoryObject() {
        AcademicTitleHistory savedAcademicTitleHistory = academicTitleHistoryRepository.save(academicTitleHistory);

        AcademicTitleHistory foundAcademicTitleHistory = academicTitleHistoryRepository.findById(savedAcademicTitleHistory.getId()).get();

        assertThat(foundAcademicTitleHistory).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for delete academic title history operation")
    public void givenAcademicTitleHistoryObject_whenDelete_thenRemoveAcademicTitleHistory() {
        academicTitleHistoryRepository.save(academicTitleHistory);

        academicTitleHistoryRepository.deleteById(academicTitleHistory.getId());
        Optional<AcademicTitleHistory> deletedAcademicTitleHistory = academicTitleHistoryRepository.findById(academicTitleHistory.getId());

        assertThat(deletedAcademicTitleHistory).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for find all academic title histories for a member, ordered by start date desc")
    public void givenMemberId_whenFindAllByMemberIdOrderByStartDateDesc_thenReturnOrderedAcademicTitleHistories() {
        academicTitleHistoryRepository.save(academicTitleHistory);

        AcademicTitleHistory newerAcademicTitleHistory = AcademicTitleHistory.builder()
                .startDate(LocalDate.now().minusYears(1))
                .member(member)
                .academicTitle(academicTitle)
                .build();
        academicTitleHistoryRepository.save(newerAcademicTitleHistory);

        List<AcademicTitleHistory> academicTitleHistories = academicTitleHistoryRepository.findAllByMemberIdOrderByStartDateDesc(member.getId());

        assertThat(academicTitleHistories).isNotNull();
        assertEquals(2, academicTitleHistories.size());

        assertEquals(newerAcademicTitleHistory, academicTitleHistories.get(0));
        assertEquals(academicTitleHistory, academicTitleHistories.get(1));
    }

    @Test
    @DisplayName("JUnit test for find all academic title histories for a member with non-null end date")
    public void givenMemberId_whenFindAllByMemberIdAndEndDateNotNull_thenReturnAcademicTitleHistories() {
        AcademicTitleHistory academicTitleHistory = AcademicTitleHistory.builder()
                .startDate(LocalDate.now().minusYears(1))
                .endDate(LocalDate.now().plusYears(5))
                .member(member)
                .academicTitle(academicTitle)
                .build();
        academicTitleHistoryRepository.save(academicTitleHistory);

        List<AcademicTitleHistory> academicTitleHistories = academicTitleHistoryRepository.findAllByMemberIdAndEndDateNotNull(member.getId());

        assertThat(academicTitleHistories).isNotNull();
        assertEquals(1, academicTitleHistories.size());

        assertThat(academicTitleHistories.get(0).getEndDate()).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for find all academic title histories for a member with null end date - current academic title")
    public void givenMemberId_whenFindAllByMemberIdAndEndDateNull_thenReturnAcademicTitleHistories() {
        AcademicTitleHistory academicTitleHistory = AcademicTitleHistory.builder()
                .startDate(LocalDate.now().minusYears(1))
                .endDate(null)
                .member(member)
                .academicTitle(academicTitle)
                .build();
        academicTitleHistoryRepository.save(academicTitleHistory);

        AcademicTitleHistory currentAcademicTitleByMemberId = academicTitleHistoryRepository.findCurrentAcademicTitleByMemberId(member.getId()).get();

        assertThat(currentAcademicTitleByMemberId).isNotNull();
        assertThat(currentAcademicTitleByMemberId.getEndDate()).isNull();
    }

    @Test
    @DisplayName("JUnit test for find all academic title histories for a member, ordered by start date")
    public void givenMemberId_whenFindAllByMemberIdOrderByStartDate_thenReturnOrderedAcademicTitleHistories() {
        academicTitleHistoryRepository.save(academicTitleHistory);

        AcademicTitleHistory newerAcademicTitleHistory = AcademicTitleHistory.builder()
                .startDate(LocalDate.now().minusYears(1))
                .member(member)
                .academicTitle(academicTitle)
                .build();
        academicTitleHistoryRepository.save(newerAcademicTitleHistory);

        List<AcademicTitleHistory> academicTitleHistories = academicTitleHistoryRepository.findByMemberIdOrderByStartDate(member.getId());

        assertThat(academicTitleHistories).isNotNull();
        assertEquals(2, academicTitleHistories.size());

        assertEquals(newerAcademicTitleHistory, academicTitleHistories.get(1));
        assertEquals(academicTitleHistory, academicTitleHistories.get(0));
    }
}