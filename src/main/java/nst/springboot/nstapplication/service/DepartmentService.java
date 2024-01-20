/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.DepartmentDto;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;

import java.util.List;

/**
 *
 * @author student2
 */
public interface DepartmentService {
    DepartmentDto save(DepartmentDto departmentDto);
    List<DepartmentDto> getAll();
    void delete(Long id) ;
    DepartmentDto update(Long id,DepartmentDto department);
    DepartmentDto findById(Long id);
    MemberDto getActiveSecretaryForDepartment(Long id);
    MemberDto getActiveHeadForDepartment(Long id);
    List<SecretaryHistoryDto> getSecretaryHistoryForDepartment(Long id);
    List<HeadHistoryDto> getHeadHistoryForDepartment(Long id);

    List<MemberDto> getAllMembersByDepartmentId(Long id);
}
