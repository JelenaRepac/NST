package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.AcademicTitle;
import nst.springboot.nstapplication.domain.AcademicTitleHistory;
import nst.springboot.nstapplication.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicTitleHistoryRepository extends JpaRepository<AcademicTitleHistory, Long> {
    //get academic title history for member id
    List<AcademicTitleHistory> findAllByMemberIdOrderByStartDateDesc(Long memberId);

    @Query("SELECT at FROM AcademicTitleHistory at " +
            "WHERE at.member.id = :memberId " +
            "AND at.endDate IS NULL")
    Optional<AcademicTitleHistory> findCurrentAcademicTitleByMemberId(@Param("memberId") Long memberId);

}
