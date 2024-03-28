package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.domain.EducationTitle;
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
public class AcademicTitleRepositoryTest {



    @Autowired
    private AcademicTitleRepository academicTitleRepository;

    AcademicTitle academicTitle;


    @BeforeEach
    public void setupTestData(){
        academicTitle =AcademicTitle.builder().name("Teaching Assistant").build();
    }

    @Test
    @DisplayName("JUnit test for save academic title operation")
    public void givenAcademicTitleObject_whenSave_thenReturnSaveAcademicTitle(){

        AcademicTitle savedAcademicTitle = academicTitleRepository.save(academicTitle);

        assertThat(savedAcademicTitle).isNotNull();
        assertThat(savedAcademicTitle.getId()).isGreaterThan(0);
        assertEquals(savedAcademicTitle.getName(), academicTitle.getName());
    }
    @Test
    @DisplayName("JUnit test for find all academic title operation")
    public void givenAcademicList_whenFindAll_thenAcademicList(){
        AcademicTitle academicTitle1= AcademicTitle.builder().name("Teaching Assistant").build();
        AcademicTitle academicTitle2= AcademicTitle.builder().name("Senior Teaching Assistant").build();

        academicTitleRepository.save(academicTitle1);
        academicTitleRepository.save(academicTitle2);


        List<AcademicTitle> academicTitles = academicTitleRepository.findAll();

        assertThat(academicTitles).isNotNull();
        assertEquals(academicTitles.size(), 2);
    }
    @Test
    @DisplayName("JUnit test for find by id academic title operation")
    public void givenAcademicTitleId_whenFindById_thenReturnAcademicTitleIdObject(){
        AcademicTitle savedAcademicTitle = academicTitleRepository.save(academicTitle);

        AcademicTitle findAcTitle = academicTitleRepository.findById(savedAcademicTitle.getId()).get();

        assertThat(findAcTitle).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for delete academic title operation")
    public void givenAcademicObject_whenDelete_thenRemoveAcademic() {
        academicTitleRepository.save(academicTitle);

        academicTitleRepository.deleteById(academicTitle.getId());
        Optional<AcademicTitle> deletedAcademicTitle = academicTitleRepository.findById(academicTitle.getId());

        assertThat(deletedAcademicTitle).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for finding academic title by name")
    public void givenAcademicTitleName_whenFindByName_thenReturnAcademicTitleObject(){
        academicTitleRepository.save(academicTitle);

        AcademicTitle academicTitleDb = academicTitleRepository.findByName("Teaching Assistant").get();

        assertEquals(academicTitleDb.getName(), "Teaching Assistant");
    }
}
