package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.EducationTitle;
import nst.springboot.nstapplication.domain.Role;
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
public class EducationTitleRepositoryTest {


    @Autowired
    private EducationTitleRepository educationTitleRepository;

    EducationTitle educationTitle;


    @BeforeEach
    public void setupTestData(){
        educationTitle =EducationTitle.builder().name("Associate degree").build();
    }

    @Test
    @DisplayName("JUnit test for save education title operation")
    public void givenEducationTitleObject_whenSave_thenReturnSaveEducationTitle(){

        EducationTitle savedEducationTitle = educationTitleRepository.save(educationTitle);

        assertThat(savedEducationTitle).isNotNull();
        assertThat(savedEducationTitle.getId()).isGreaterThan(0);
        assertEquals(savedEducationTitle.getName(), educationTitle.getName());
    }
    @Test
    @DisplayName("JUnit test for find all education title operation")
    public void givenRoleList_whenFindAll_thenRoleList(){
        EducationTitle educationTitle1= EducationTitle.builder().name("Associate degree").build();
        EducationTitle educationTitle2= EducationTitle.builder().name("Bachelor's degree").build();

        educationTitleRepository.save(educationTitle1);
        educationTitleRepository.save(educationTitle2);


        List<EducationTitle> educationTitles = educationTitleRepository.findAll();

        assertThat(educationTitles).isNotNull();
        assertEquals(educationTitles.size(), 2);
    }
    @Test
    @DisplayName("JUnit test for find by id education title operation")
    public void givenEducationTitleId_whenFindById_thenReturnEducationTitleIdObject(){
        EducationTitle savedEducationTitle = educationTitleRepository.save(educationTitle);

        EducationTitle findEduTitle = educationTitleRepository.findById(savedEducationTitle.getId()).get();

        assertThat(findEduTitle).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for delete education title operation")
    public void givenRoleObject_whenDelete_thenRemoveRole() {
        educationTitleRepository.save(educationTitle);

        educationTitleRepository.deleteById(educationTitle.getId());
        Optional<EducationTitle> deletedEducationTitle = educationTitleRepository.findById(educationTitle.getId());

        assertThat(deletedEducationTitle).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for finding education title by name")
    public void givenEducationTitleName_whenFindByName_thenReturnEducationTitleObject(){
        educationTitleRepository.save(educationTitle);

        EducationTitle educationTitleDb = educationTitleRepository.findByName("Associate degree").get();

        assertEquals(educationTitleDb.getName(), "Associate degree");
    }
}
