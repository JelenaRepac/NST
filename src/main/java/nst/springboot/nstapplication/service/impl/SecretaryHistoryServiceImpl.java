package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.domain.HeadHistory;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.domain.SecretaryHistory;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.exception.IllegalArgumentException;
import nst.springboot.nstapplication.repository.DepartmentRepository;
import nst.springboot.nstapplication.repository.MemberRepository;
import nst.springboot.nstapplication.repository.SecretaryHistoryRepository;
import nst.springboot.nstapplication.service.SecretaryHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecretaryHistoryServiceImpl implements SecretaryHistoryService {
    private SecretaryHistoryRepository repository;
    private MemberRepository memberRepository;
    private MemberConverter memberConverter;
    private DepartmentRepository departmentRepository;
    private DepartmentConverter departmentConverter;
    private SecretaryHistoryConverter secretaryHistoryConverter;

    public SecretaryHistoryServiceImpl(DepartmentConverter departmentConverter, DepartmentRepository departmentRepository, SecretaryHistoryRepository repository, SecretaryHistoryConverter secretaryHistoryConverter, MemberRepository memberRepository, MemberConverter memberConverter) {
        this.repository = repository;
        this.secretaryHistoryConverter = secretaryHistoryConverter;
        this.memberRepository = memberRepository;
        this.memberConverter = memberConverter;
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
    }


    @Override
    public SecretaryHistoryDto save(SecretaryHistoryDto secretaryHistoryDTO) {
        if (secretaryHistoryDTO.getEndDate() != null && secretaryHistoryDTO.getStartDate() != null) {
            if (secretaryHistoryDTO.getEndDate().isBefore(secretaryHistoryDTO.getStartDate())) {
                throw new IllegalArgumentException("End date can't be before start date!");
            }
        }
        Optional<Member> existingMember;
        if (secretaryHistoryDTO.getMember().getId() != null) {
            existingMember = memberRepository.findById(secretaryHistoryDTO.getMember().getId());
            if (existingMember.isPresent()) {
                secretaryHistoryDTO.setMember(memberConverter.toDto(existingMember.get()));
            } else {
                throw new EntityNotFoundException("There is no member with that id!");
            }
        } else {
            existingMember = memberRepository.findByFirstnameAndLastname(secretaryHistoryDTO.getMember().getFirstname(), secretaryHistoryDTO.getMember().getLastname());
            if (existingMember.isPresent()) {
                secretaryHistoryDTO.setMember(memberConverter.toDto(existingMember.get()));
            } else {
                memberRepository.save(memberConverter.toEntity(secretaryHistoryDTO.getMember()));
                Optional<Member> member = memberRepository.findByFirstnameAndLastname(secretaryHistoryDTO.getMember().getFirstname(), secretaryHistoryDTO.getMember().getLastname());
                if (member.isPresent()) {
                    secretaryHistoryDTO.setMember(memberConverter.toDto(member.get()));
                }
            }
        }
        Optional<Department> existingDepartment;

        if (secretaryHistoryDTO.getDepartment().getId() != null) {
            existingDepartment = departmentRepository.findById(secretaryHistoryDTO.getDepartment().getId());
            if (existingDepartment.isPresent()) {
                if (secretaryHistoryDTO.getMember().getDepartment().getId() != secretaryHistoryDTO.getDepartment().getId()) {
                    throw new IllegalArgumentException("Member is not in that department! He is in  " + secretaryHistoryDTO.getMember().getDepartment().getName() + ".");
                }
                secretaryHistoryDTO.setDepartment(departmentConverter.toDto(existingDepartment.get()));
            } else {
                throw new EntityNotFoundException("There is no department with that id!");
            }
        } else {
            existingDepartment = departmentRepository.findByName(secretaryHistoryDTO.getDepartment().getName());
            if (existingDepartment.isPresent()) {
                if (secretaryHistoryDTO.getMember().getDepartment().getId() != existingDepartment.get().getId()) {
                    throw new IllegalArgumentException("Member is not in that department! He is in  " + secretaryHistoryDTO.getMember().getDepartment().getName() + ".");
                }
                secretaryHistoryDTO.setDepartment(departmentConverter.toDto(existingDepartment.get()));
            } else {
                departmentRepository.save(departmentConverter.toEntity(secretaryHistoryDTO.getDepartment()));
                Optional<Department> department = departmentRepository.findByName(secretaryHistoryDTO.getDepartment().getName());
                if (department.isPresent()) {
                    secretaryHistoryDTO.setDepartment(departmentConverter.toDto(department.get()));
                }

            }
        }

        List<SecretaryHistory> existingHistoryList = repository.findByDepartmentId(secretaryHistoryDTO.getDepartment().getId());

        for (SecretaryHistory existingHistory : existingHistoryList) {
            Optional<Member> member = memberRepository.findById(existingHistory.getMember().getId());
            if (secretaryHistoryDTO.getEndDate() == null && existingHistory.getEndDate() == null) {
                throw new IllegalArgumentException("There is already secretary member " + existingHistory.getMember().getFirstname()
                        + " " + existingHistory.getMember().getLastname() + " for department " + existingHistory.getDepartment().getName());

            }

            if (existingHistory.getStartDate() != null && existingHistory.getEndDate() != null && secretaryHistoryDTO.getStartDate() != null && secretaryHistoryDTO.getEndDate() != null) {
                if (isDateOverlap(existingHistory.getStartDate(), existingHistory.getEndDate(),
                        secretaryHistoryDTO.getStartDate(), secretaryHistoryDTO.getEndDate())) {
                    throw new IllegalArgumentException("The member " + member.get().getFirstname() + " " +
                            member.get().getLastname() +
                            " already was at the SECRETARY position from " + existingHistory.getStartDate() + " to " + existingHistory.getEndDate() +
                            " in department " + secretaryHistoryDTO.getDepartment().getName());
                }
            }
        }

        return secretaryHistoryConverter.toDto(repository.save(secretaryHistoryConverter.toEntity(secretaryHistoryDTO)));
    }

    private boolean isDateOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        return startDate1.isBefore(endDate2) && endDate1.isAfter(startDate2);
    }

    @Override
    public List<SecretaryHistoryDto> getAll() {
        List<SecretaryHistoryDto> secretaryHistoryDtoList = repository
                .findAll()
                .stream()
                .map(entity -> secretaryHistoryConverter.toDto(entity))
                .collect(Collectors.toList());

        if (secretaryHistoryDtoList.isEmpty()) {
            throw new EntityNotFoundException("");
        }

        return secretaryHistoryDtoList;
    }
    @Override
    public SecretaryHistoryDto getByDepartmentId(Long id){
        Optional<SecretaryHistory> secretaryHistory = repository.findByDepartmentIdAndEndDateNull(id);
        if(secretaryHistory.isEmpty()){
            throw new EntityNotFoundException("Department doesn't have active secretary member");
        }
        return secretaryHistoryConverter.toDto(secretaryHistory.get());
    }

    @Override
    public void delete(Long id)  {
        Optional<SecretaryHistory> history = repository.findById(id);
        if (history.isPresent()) {
            SecretaryHistory history1 = history.get();
            repository.delete(history1);
        } else {
            throw new EntityNotFoundException("Secretary history does not exist!");
        }
    }

    @Override
    public SecretaryHistoryDto findById(Long id) {
        Optional<SecretaryHistory> history = repository.findById(id);
        if (history.isPresent()) {
            SecretaryHistory history1 = history.get();
            return secretaryHistoryConverter.toDto(history1);
        } else {
            throw new EntityNotFoundException("Secretary history not exist!");
        }
    }

    @Override
    public List<SecretaryHistoryDto> getHistoryForDepartmentId(Long id) {
        List<SecretaryHistory> secretaryHistoryList = repository.findByDepartmentId(id);
        if(secretaryHistoryList.isEmpty()){
            throw new EmptyResponseException("There are no secretary history for department!");
        }
        List<SecretaryHistoryDto> secretaryHistoryDtoList= new ArrayList<>();
        for(SecretaryHistory sc : secretaryHistoryList){
            secretaryHistoryDtoList.add(secretaryHistoryConverter.toDto(sc));
        }
        return secretaryHistoryDtoList;
    }
}
