package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.ScientificField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScientificFieldRepository extends JpaRepository<ScientificField, Long> {

}
