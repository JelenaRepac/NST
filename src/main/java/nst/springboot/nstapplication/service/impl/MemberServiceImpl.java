package nst.springboot.nstapplication.service.impl;

import jakarta.transaction.Transactional;
import nst.springboot.nstapplication.constants.ConstantsCustom;
import nst.springboot.nstapplication.converter.impl.*;
import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.MemberHeadSecretaryDto;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.exception.IllegalArgumentException;
import nst.springboot.nstapplication.repository.*;
import nst.springboot.nstapplication.service.MemberService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private MemberConverter memberConverter;
    private MemberHeadSecretaryConverter memberHeadSecConverter;
    private AcademicTitleConverter academicTitleConverter;
    private ScientificFieldConverter scientificFieldConverter;
    private EducationTitleConverter educationTitleConverter;
    private MemberRepository memberRepository;
    private DepartmentRepository departmentRepository;
    private AcademicTitleRepository academicTitleRepository;
    private EducationTitleRepository educationTitleRepository;
    private ScientificFieldRepository scientificFieldRepository;
    private AcademicTitleHistoryRepository academicTitleHistoryRepository;
    private HeadHistoryRepository headHistoryRepository;
    private SecretaryHistoryRepository secretaryHistoryRepository;
    private RoleRepository roleRepository;


    public MemberServiceImpl(MemberConverter memberConverter, MemberHeadSecretaryConverter memberHeadSecConverter, AcademicTitleConverter academicTitleConverter, ScientificFieldConverter scientificFieldConverter, EducationTitleConverter educationTitleConverter, MemberRepository memberRepository, DepartmentRepository departmentRepository, AcademicTitleRepository academicTitleRepository, EducationTitleRepository educationTitleRepository, ScientificFieldRepository scientificFieldRepository, AcademicTitleHistoryRepository academicTitleHistoryRepository, HeadHistoryRepository headHistoryRepository, SecretaryHistoryRepository secretaryHistoryRepository, RoleRepository roleRepository) {
        this.memberConverter = memberConverter;
        this.memberHeadSecConverter = memberHeadSecConverter;
        this.academicTitleConverter = academicTitleConverter;
        this.scientificFieldConverter = scientificFieldConverter;
        this.educationTitleConverter = educationTitleConverter;
        this.memberRepository = memberRepository;
        this.departmentRepository = departmentRepository;
        this.academicTitleRepository = academicTitleRepository;
        this.educationTitleRepository = educationTitleRepository;
        this.scientificFieldRepository = scientificFieldRepository;
        this.academicTitleHistoryRepository = academicTitleHistoryRepository;
        this.headHistoryRepository = headHistoryRepository;
        this.secretaryHistoryRepository = secretaryHistoryRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public MemberDto save(MemberHeadSecretaryDto memberDTO) {
        Optional<Department> departmentOptional = departmentRepository.findByName(memberDTO.getDepartment().getName());

        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            Member member = memberHeadSecConverter.toEntity(memberDTO);
            member.setDepartment(department);

            AcademicTitle academicTitle = academicTitleRepository.findByName(memberDTO.getAcademicTitle().getName())
                    .orElseGet(() -> academicTitleRepository.save(academicTitleConverter.toEntity(memberDTO.getAcademicTitle())));
            member.setAcademicTitle(academicTitle);

            EducationTitle educationTitle = educationTitleRepository.findByName(memberDTO.getEducationTitle().getName())
                    .orElseGet(() -> educationTitleRepository.save(educationTitleConverter.toEntity(memberDTO.getEducationTitle())));
            member.setEducationTitle(educationTitle);

            ScientificField scientificField = scientificFieldRepository.findByName(memberDTO.getScientificField().getName())
                    .orElseGet(() -> scientificFieldRepository.save(scientificFieldConverter.toEntity(memberDTO.getScientificField())));
            member.setScientificField(scientificField);

            if (member.getRole() == null) {
                setDefaultMemberAttributes(member);
            } else {
                handleRoleSpecificAttributes(member);
            }

            Member savedMember = memberRepository.save(member);
            academicTitleHistoryRepository.save(AcademicTitleHistory.builder().
                    academicTitle(member.getAcademicTitle()).
                    member(member).scientificField(member.getScientificField()).
                    startDate(LocalDate.now()).
                    endDate(null).build());
            return memberConverter.toDto(savedMember);
        }

        throw new EntityNotFoundException("Department doesn't exist!");
    }

    private void setDefaultMemberAttributes(Member member) {
        Role defaultRole = roleRepository.findByName(ConstantsCustom.DEFAULT_ROLE).orElseThrow(() -> new EntityNotFoundException("Default role not found"));
        member.setRole(defaultRole);
    }

    private void handleRoleSpecificAttributes(Member member) {
        String roleName = member.getRole().getName();

        switch (roleName) {
            case ConstantsCustom.SECRETARY:
                handleSecretaryAttributes(member);
                break;
            case ConstantsCustom.HEAD:
                handleHeadAttributes(member);
                break;
            default:
                throw new IllegalArgumentException("You provided unknown type!");
        }
    }

    private void handleSecretaryAttributes(Member member) {
        checkExistingMemberAndThrowException(member, 1L, "The department already has a secretary member.");
        member.setRole(roleRepository.findByName(ConstantsCustom.SECRETARY).orElseThrow(() -> new EntityNotFoundException("Secretary role not found")));
        Member savedMember = memberRepository.save(member);
        secretaryHistoryRepository.save(new SecretaryHistory(null, savedMember.getDepartment(), savedMember, LocalDate.now(), null));
    }

    private void handleHeadAttributes(Member member) {
        checkExistingMemberAndThrowException(member, 2L, "The department already has a head member.");
        member.setRole(roleRepository.findByName(ConstantsCustom.HEAD).orElseThrow(() -> new EntityNotFoundException("Head role not found")));
        Member savedMember = memberRepository.save(member);
        headHistoryRepository.save(new HeadHistory(null, savedMember.getDepartment(), savedMember, null, LocalDate.now()));
    }
    private void checkExistingMemberAndThrowException(Member member, Long roleId, String errorMessage) {
        Optional<Member> existingMember = memberRepository.findByDepartmentNameAndRoleId(member.getDepartment().getName(), roleId);
        if (existingMember.isPresent()) {
            throw new EntityNotFoundException(errorMessage);
        }
    }

    @Override
    public List<MemberDto> getAll() {
        return memberRepository
                .findAll()
                .stream().map(entity -> memberConverter.toDto((Member) entity))
                .collect(Collectors.toList());
    }
    public List<MemberDto> getAllByDepartmentId(Long id) {
        return memberRepository
                .findAllByDepartmentId(id)
                .stream().map(entity -> memberConverter.toDto((Member) entity))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Optional<Member> member= memberRepository.findById(id);
        if(member.isPresent()){
            memberRepository.deleteById(id);
        }
        else {
            throw new EntityNotFoundException("There is no member with that id in database!");
        }
    }


}
