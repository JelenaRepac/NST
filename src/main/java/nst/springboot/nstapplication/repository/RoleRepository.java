package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
