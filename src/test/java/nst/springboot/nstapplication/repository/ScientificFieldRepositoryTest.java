package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.ScientificField;
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
public class ScientificFieldRepositoryTest {

    @Autowired
    private ScientificFieldRepository scientificFieldRepository;

    ScientificField scientificField;

    @BeforeEach
    public void setupTestData() {
        scientificField = ScientificField.builder().name("Artificial intelligence").build();
    }

    @Test
    @DisplayName("JUnit test for save scientific field operation")
    public void givenScientificFieldObject_whenSave_thenReturnSavedScientificField() {
        ScientificField savedScientificField = scientificFieldRepository.save(scientificField);

        assertThat(savedScientificField).isNotNull();
        assertThat(savedScientificField.getId()).isGreaterThan(0);
        assertEquals(savedScientificField.getName(), scientificField.getName());
    }

    @Test
    @DisplayName("JUnit test for find all scientific fields operation")
    public void givenScientificFieldList_whenFindAll_thenReturnScientificFieldList() {
        ScientificField scientificField1 = ScientificField.builder().name("Artificial intelligence").build();
        ScientificField scientificField2 = ScientificField.builder().name("Scientific computing applications").build();

        scientificFieldRepository.save(scientificField1);
        scientificFieldRepository.save(scientificField2);

        List<ScientificField> scientificFields = scientificFieldRepository.findAll();

        assertThat(scientificFields).isNotNull();
        assertEquals(2, scientificFields.size());
    }

    @Test
    @DisplayName("JUnit test for find by id scientific field operation")
    public void givenScientificFieldId_whenFindById_thenReturnScientificFieldObject() {
        ScientificField savedScientificField = scientificFieldRepository.save(scientificField);

        ScientificField foundScientificField = scientificFieldRepository.findById(savedScientificField.getId()).get();

        assertThat(foundScientificField).isNotNull();
    }

    @Test
    @DisplayName("JUnit test for delete scientific field operation")
    public void givenScientificFieldObject_whenDelete_thenRemoveScientificField() {
        scientificFieldRepository.save(scientificField);

        scientificFieldRepository.deleteById(scientificField.getId());
        Optional<ScientificField> deletedScientificField = scientificFieldRepository.findById(scientificField.getId());

        assertThat(deletedScientificField).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for finding scientific field by name")
    public void givenScientificFieldName_whenFindByName_thenReturnScientificFieldObject() {
        scientificFieldRepository.save(scientificField);

        ScientificField scientificFieldDb = scientificFieldRepository.findByName("Artificial intelligence").get();

        assertEquals("Artificial intelligence", scientificFieldDb.getName());
    }
}