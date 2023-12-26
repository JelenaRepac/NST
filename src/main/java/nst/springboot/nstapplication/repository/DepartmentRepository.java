/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.repository;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import nst.springboot.nstapplication.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

/**
 *
 * @author student2
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public <S extends Department> S save(S entity);

    Optional<Department> findByName(String name);
}
