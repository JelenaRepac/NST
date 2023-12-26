package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.AcademicTitleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AcademicTitleHistoryRepository extends JpaRepository<AcademicTitleHistory, Long> {
    List<AcademicTitleHistory> findAllByMemberIdAndAcademicTitleIdOrderByStartDateDesc(Long memberId, Long academicTitleId);
}
