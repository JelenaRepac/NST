package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.domain.AcademicTitleHistory;
import nst.springboot.nstapplication.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AcademicTitleRepository extends JpaRepository<AcademicTitle, Long> {
    Optional<AcademicTitle> findByName(String name);
}
