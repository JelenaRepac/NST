package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.SecretaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SecretaryHistoryRepository extends JpaRepository<SecretaryHistory, Long> {
    Optional<SecretaryHistory> findByDepartmentIdAndEndDateNull(Long id);
    List<SecretaryHistory> findAllByMemberIdAndDepartmentIdOrderByStartDateDesc(Long idM, Long idD);

}
