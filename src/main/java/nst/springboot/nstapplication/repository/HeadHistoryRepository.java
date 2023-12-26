package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.HeadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeadHistoryRepository extends JpaRepository<HeadHistory, Long> {
    Optional<HeadHistory> findByDepartmentIdAndEndDateNull(Long id);

    List<HeadHistory> findAllByMemberIdAndDepartmentIdOrderByStartDateDesc(Long idM, Long idD);
}