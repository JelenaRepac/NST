package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.domain.SecretaryHistory;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SecretaryHistoryRepository extends JpaRepository<SecretaryHistory, Long> {
    Optional<SecretaryHistory> findByDepartmentIdAndEndDateNull(Long id);
    @Query("SELECT sh FROM SecretaryHistory sh " +
            "WHERE sh.member.id = :memberId " +
            "AND sh.endDate IS NULL")
    Optional<SecretaryHistory> findCurrentByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT sh.member FROM SecretaryHistory sh " +
            "WHERE sh.department.id = :departmentId " +
            "AND sh.endDate IS NULL")
    Optional<Member> findCurrentSecretaryByDepartmentId(@Param("departmentId") Long departmentId);
    List<SecretaryHistory> findByDepartmentId(Long id);
    List<SecretaryHistory> findByMemberId(Long id);
    @Query("SELECT h FROM SecretaryHistory h " +
            "WHERE h.department.id = :departmentId " +
            "ORDER BY COALESCE(h.endDate, CURRENT_DATE) " +
            "DESC, h.startDate DESC")
    List<SecretaryHistory> findByDepartmentIdOrderByDate(@Param("departmentId") Long id);
}
