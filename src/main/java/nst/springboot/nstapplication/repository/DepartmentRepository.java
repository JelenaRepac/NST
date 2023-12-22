/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.repository;

import java.util.Optional;
import nst.springboot.nstapplication.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author student2
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{

    Optional<Department> findByName(String name);
}
