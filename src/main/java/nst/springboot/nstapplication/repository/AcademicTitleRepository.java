package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.AcademicTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcademicTitleRepository extends JpaRepository<AcademicTitle, Long> {


    Optional<AcademicTitle> findByName(String name);
}
