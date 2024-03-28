package nst.springboot.nstapplication.repository;

import nst.springboot.nstapplication.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    Role role;


    @BeforeEach
    public void setupTestData(){
        role =Role.builder().name("Default").build();
    }

    @Test
    @DisplayName("JUnit test for save role operation")
    public void givenRoleObject_whenSave_thenReturnSaveRole(){

        Role savedRole = roleRepository.save(role);

        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getId()).isGreaterThan(0);
        assertEquals(role.getName(), savedRole.getName());
    }
    @Test
    @DisplayName("JUnit test for find all roles operation")
    public void givenRoleList_whenFindAll_thenRoleList(){
        Role role1= Role.builder().name("Secretary").build();
        Role role2= Role.builder().name("Default").build();

        roleRepository.save(role1);
        roleRepository.save(role2);


        List<Role> roles = roleRepository.findAll();

        assertThat(roles).isNotNull();
        assertEquals(roles.size(), 2);
    }
    @Test
    @DisplayName("JUnit test for find by id role operation")
    public void givenRoleId_whenFindById_thenReturnRoleObject(){
        Role savedRole = roleRepository.save(role);

        Role findRole = roleRepository.findById(savedRole.getId()).get();

        assertThat(findRole).isNotNull();
    }
    @Test
    @DisplayName("JUnit test for delete role operation")
    public void givenRoleObject_whenDelete_thenRemoveRole() {
        roleRepository.save(role);

        roleRepository.deleteById(role.getId());
        Optional<Role> deletedRole = roleRepository.findById(role.getId());

        assertThat(deletedRole).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for finding role by name")
    public void givenRoleName_whenFindByName_thenReturnRoleObject(){
        roleRepository.save(role);

        Role roleDb = roleRepository.findByName("Default").get();

        assertEquals(roleDb.getName(), "Default");
    }
}
