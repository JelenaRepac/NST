package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByDepartmentId(Long id);

    Optional<Member> findByDepartmentIdAndRoleId(Long idDept, Long idR);
}
