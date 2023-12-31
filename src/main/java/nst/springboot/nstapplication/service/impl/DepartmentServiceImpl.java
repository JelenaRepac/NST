/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.dto.DepartmentDto;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.DepartmentRepository;
import nst.springboot.nstapplication.repository.HeadHistoryRepository;
import nst.springboot.nstapplication.repository.SecretaryHistoryRepository;
import nst.springboot.nstapplication.service.DepartmentService;
import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
import org.springframework.stereotype.Service;

/**
 *
 * @author student2
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentConverter departmentConverter;
    private DepartmentRepository departmentRepository;

    private SecretaryHistoryRepository secretaryHistoryRepository;
    private HeadHistoryRepository headHistoryRepository;
    private MemberConverter memberConverter;

    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository,
            DepartmentConverter departmentConverter,
            SecretaryHistoryRepository secretaryHistoryRepository,
            HeadHistoryRepository headHistoryRepository,
            MemberConverter memberConverter) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
        this.secretaryHistoryRepository=secretaryHistoryRepository;
        this.headHistoryRepository=headHistoryRepository;
        this.memberConverter= memberConverter;
    }

    @Override
    public DepartmentDto save(DepartmentDto departmentDto)  {
        Optional<Department> dept = departmentRepository.findByName(departmentDto.getName());
        if (dept.isPresent()) {
            throw new EntityAlreadyExistsException("Department with that name already exists!");
        } else {
            Department department = departmentConverter.toEntity(departmentDto);
            department = departmentRepository.save(department);
            return departmentConverter.toDto(department);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Department> dept = departmentRepository.findById(id);
        if (dept.isPresent()) {
            departmentRepository.delete(dept.get());
        } else {
            throw new EntityNotFoundException("Department does not exist!");
        }

    }

    @Override
    public DepartmentDto update(Long id, DepartmentDto department)  {
        Optional<Department> dept = departmentRepository.findById(id);

        if (dept.isPresent()){
            Department existingDepartment = departmentRepository.findById(id).get();
            existingDepartment.setName(department.getName());
            existingDepartment.setShortName(department.getShortName());
            Department updatedDepartment = departmentRepository.save(existingDepartment);

            return DepartmentDto.builder().
                    id(updatedDepartment.getId()).
                    name(updatedDepartment.getName()).
                    shortName(updatedDepartment.getShortName()).build();

        }else{
            throw new EntityNotFoundException("Department not found with name: " + department.getName()+ " and Id: "+id);
        }

    }

    @Override
    public DepartmentDto findById(Long id)  {
        Optional<Department> dept = departmentRepository.findById(id);
        if (dept.isPresent()) {
            return departmentConverter.toDto(dept.get());
        } else {
            throw new EntityNotFoundException("Department does not exist!");
        }
    }

    @Override
    public MemberDto getActiveSecretaryForDepartment(Long id) {
        Optional<Department> department= departmentRepository.findById(id);
        if(!department.isPresent()){
            throw new EntityNotFoundException("There is no department with id: "+id);
        }
          Optional<Member> secretary = secretaryHistoryRepository.findCurrentSecretaryByDepartmentId(id);
          if(secretary.isPresent()){
              return memberConverter.toDto(secretary.get());
          }else{
              throw new EntityNotFoundException("There is no active secretary for "+department.get().getName());
          }
    }

    @Override
    public MemberDto getActiveHeadForDepartment(Long id) {
        Optional<Department> department= departmentRepository.findById(id);
        if(!department.isPresent()){
            throw new EntityNotFoundException("There is no department with id: "+id);
        }
        Optional<Member> head = headHistoryRepository.findCurrentHeadByDepartmentId(id);
        if(head.isPresent()){
            return memberConverter.toDto(head.get());
        }else{
            throw new EntityNotFoundException("There is no active head for "+department.get().getName());
        }
    }

    @Override
    public List<DepartmentDto> getAll() {
        List<DepartmentDto> departmentDtoList = departmentRepository
                .findAll()
                .stream().map(entity -> departmentConverter.toDto(entity))
                .collect(Collectors.toList());
        if(departmentDtoList.isEmpty()){
            throw new EmptyResponseException("There are no departments in database!");
        }
            return departmentDtoList;
    }

}
