/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import nst.springboot.nstapplication.converter.impl.HeadHistoryConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.domain.HeadHistory;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.domain.SecretaryHistory;
import nst.springboot.nstapplication.dto.DepartmentDto;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.repository.DepartmentRepository;
import nst.springboot.nstapplication.repository.HeadHistoryRepository;
import nst.springboot.nstapplication.repository.MemberRepository;
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
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentConverter departmentConverter;
    private final DepartmentRepository departmentRepository;
    private final SecretaryHistoryRepository secretaryHistoryRepository;
    private final SecretaryHistoryConverter secretaryHistoryConverter;
    private final HeadHistoryRepository headHistoryRepository;
    private final HeadHistoryConverter headHistoryConverter;
    private final MemberConverter memberConverter;
    private final MemberRepository memberRepository;

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
        MemberDto memberDtoEndNull=null;
        MemberDto memberDto = null;
        if(!department.isPresent()){
            throw new EntityNotFoundException("There is no department with id: "+id);
        }

      else{
          List<SecretaryHistory> secretaryHistoryList= secretaryHistoryRepository.findByDepartmentId(id);
          LocalDate currentDate = LocalDate.now();
          for (SecretaryHistory secretaryHistory : secretaryHistoryList) {
              if(secretaryHistory.getStartDate() != null && secretaryHistory.getEndDate() != null &&
              ((secretaryHistory.getStartDate().isBefore(currentDate) || secretaryHistory.getStartDate().isEqual(currentDate) ) &&
              (secretaryHistory.getEndDate().isAfter(currentDate) || secretaryHistory.getEndDate().isEqual(currentDate) ))
              ) {
                  memberDto= memberConverter.toDto(secretaryHistory.getMember());
              }
              if(secretaryHistory.getEndDate()==null &&
                      (secretaryHistory.getStartDate().isBefore(currentDate) || secretaryHistory.getStartDate().isEqual(currentDate)) ){
                  System.out.println(secretaryHistory.getStartDate());
                  memberDtoEndNull= memberConverter.toDto(secretaryHistory.getMember());
              }
          }
      }
      if(memberDto==null && memberDtoEndNull==null){
          throw new EntityNotFoundException("There is no active secretary for "+department.get().getName()+" department.");
      }
      if(memberDtoEndNull!=null){
          return memberDtoEndNull;
      }
      return memberDto;

}

    @Override
    public MemberDto getActiveHeadForDepartment(Long id) {
        Optional<Department> department= departmentRepository.findById(id);
        MemberDto memberDto=null;
        MemberDto memberDtoEndNull=null;
        if(!department.isPresent()){
            throw new EntityNotFoundException("There is no department with id: "+id);
        }

        else{
            List<HeadHistory> headHistoryList= headHistoryRepository.findByDepartmentId(id);
            LocalDate currentDate = LocalDate.now();
            for (HeadHistory headHistory : headHistoryList) {
                if(headHistory.getStartDate() != null && headHistory.getEndDate() != null &&
                        ((headHistory.getStartDate().isBefore(currentDate) || headHistory.getStartDate().isEqual(currentDate) ) &&
                                (headHistory.getEndDate().isAfter(currentDate) || headHistory.getEndDate().isEqual(currentDate) ))
                ) {
                    memberDto= memberConverter.toDto(headHistory.getMember());
                }
                if(headHistory.getEndDate()==null &&
                        (headHistory.getStartDate().isBefore(currentDate) || headHistory.getStartDate().isEqual(currentDate)) ){
                    System.out.println(headHistory.getStartDate());
                    memberDtoEndNull= memberConverter.toDto(headHistory.getMember());
                }
            }
        }
        if(memberDto==null && memberDtoEndNull==null){
            throw new EntityNotFoundException("There is no active head for "+department.get().getName()+" department.");
        }
        if(memberDtoEndNull!=null){
            return  memberDtoEndNull;
        }
        return memberDto;
    }

    @Override
    public List<SecretaryHistoryDto> getSecretaryHistoryForDepartment(Long id) {
        Optional<Department> department= departmentRepository.findById(id);
        if(!department.isPresent()){
            throw new EntityNotFoundException("There is no department with id: "+id);
        }
        List<SecretaryHistory> secretaryHistoryList = secretaryHistoryRepository.findByDepartmentIdOrderByDate(id);
        if(secretaryHistoryList.isEmpty()){
            throw new EmptyResponseException("There wasn't any secretary for department "+department.get().getName());
        }
        return secretaryHistoryConverter.toDtoList(secretaryHistoryList);
    }

    @Override
    public List<HeadHistoryDto> getHeadHistoryForDepartment(Long id) {
        Optional<Department> department= departmentRepository.findById(id);
        if(!department.isPresent()){
            throw new EntityNotFoundException("There is no department with id: "+id);
        }
        List<HeadHistory> headHistoryList = headHistoryRepository.findByDepartmentIdOrderByDate(id);
        if(headHistoryList.isEmpty()){
            throw new EmptyResponseException("There wasn't any head for department "+department.get().getName());
        }
        return headHistoryConverter.toDtoList(headHistoryList);
    }

    @Override
    public List<MemberDto> getAllMembersByDepartmentId(Long id) {
       Optional<Department> department= departmentRepository.findById(id);
       if(!department.isPresent()){
           throw new EntityNotFoundException("There is no department with that id!");
       }
       List<Member> memberList= memberRepository.findAllByDepartmentId(id);
       if(memberList.isEmpty()){
           throw new EntityNotFoundException("There are no members for department "+ department.get().getName());
       }
       return memberConverter.toDtoList(memberList);
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
