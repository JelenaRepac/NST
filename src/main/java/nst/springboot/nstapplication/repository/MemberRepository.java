package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByDepartmentId(Long id);

    Optional<Member> findByDepartmentNameAndRoleId(String name, long l);
    @Query("SELECT m FROM Member m " +
            "JOIN FETCH m.department " +
            "JOIN FETCH m.role " +
            "WHERE m.department.name = :departmentName " +
            "AND m.role.id = :roleId " +
            "AND m.id IN (" +
            "   SELECT sh.member.id " +
            "   FROM SecretaryHistory sh " +
            "   WHERE sh.member.id = m.id " +
            "   AND sh.endDate IS NULL " +
            "   ORDER BY sh.startDate DESC NULLS LAST" +
            ")")
    Member findCurrentSecretary(
            @Param("departmentName") String departmentName,
            @Param("roleId") Long roleId
    );
    Optional<Member> findByFirstnameAndLastname(String firstname, String lastname);
}
