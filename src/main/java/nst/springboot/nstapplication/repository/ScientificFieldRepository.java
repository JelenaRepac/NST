package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.ScientificField;
import nst.springboot.nstapplication.dto.ScientificFieldDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScientificFieldRepository extends JpaRepository<ScientificField, Long> {

    Optional<ScientificField> findByName(String name);
}
