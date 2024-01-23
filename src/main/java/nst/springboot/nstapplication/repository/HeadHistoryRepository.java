package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.HeadHistory;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.domain.SecretaryHistory;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Repository
public interface HeadHistoryRepository extends JpaRepository<HeadHistory, Long> {
    @Query("SELECT sh FROM HeadHistory sh " +
            "WHERE sh.member.id = :memberId " +
            "AND sh.endDate IS NULL")
    Optional<HeadHistory> findCurrentByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT sh FROM HeadHistory sh " +
            "WHERE sh.department.id = :departmentId " +
            "AND sh.endDate IS NULL OR (sh.startDate < :localDate AND sh.endDate > :localDate)")
    Optional<HeadHistory> findCurrentHeadByDepartmentId(@Param("departmentId") Long departmentId,   @Param("localDate") LocalDate localDate);

    List<HeadHistory> findByDepartmentId(Long id);

    Optional<HeadHistory> findByDepartmentIdAndEndDateNull(Long id);

    List<HeadHistory> findByMemberId(Long id);
    @Query("SELECT h FROM HeadHistory h " +
            "WHERE h.department.id = :departmentId " +
            "ORDER BY COALESCE(h.endDate, CURRENT_DATE) " +
            "DESC, h.startDate DESC")
    List<HeadHistory> findByDepartmentIdOrderByDate(@Param("departmentId") Long id);
}