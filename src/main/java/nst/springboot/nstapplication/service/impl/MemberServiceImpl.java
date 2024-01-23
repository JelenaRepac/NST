package nst.springboot.nstapplication.service.impl;

import jakarta.transaction.Transactional;
import nst.springboot.nstapplication.constants.ConstantsCustom;
import nst.springboot.nstapplication.converter.impl.*;
import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.*;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityAlreadyExistsException;
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
    private AcademicTitleConverter academicTitleConverter;
    private ScientificFieldConverter scientificFieldConverter;
    private EducationTitleConverter educationTitleConverter;
    private SecretaryHistoryConverter secretaryHistoryConverter;
    private HeadHistoryConverter headHistoryConverter;
    private MemberRepository memberRepository;
    private DepartmentRepository departmentRepository;
    private AcademicTitleRepository academicTitleRepository;
    private EducationTitleRepository educationTitleRepository;
    private ScientificFieldRepository scientificFieldRepository;
    private AcademicTitleHistoryRepository academicTitleHistoryRepository;
    private HeadHistoryRepository headHistoryRepository;
    private SecretaryHistoryRepository secretaryHistoryRepository;
    private RoleRepository roleRepository;
    private RoleConverter roleConverter;
    private DepartmentConverter departmentConverter;
    private AcademicTitleHistoryConverter academicTitleHistoryConverter;

    public MemberServiceImpl(MemberConverter memberConverter, AcademicTitleConverter academicTitleConverter, ScientificFieldConverter scientificFieldConverter, EducationTitleConverter educationTitleConverter, MemberRepository memberRepository, DepartmentRepository departmentRepository, AcademicTitleRepository academicTitleRepository, EducationTitleRepository educationTitleRepository, ScientificFieldRepository scientificFieldRepository, AcademicTitleHistoryRepository academicTitleHistoryRepository, HeadHistoryRepository headHistoryRepository, SecretaryHistoryRepository secretaryHistoryRepository, RoleRepository roleRepository, RoleConverter roleConverter, DepartmentConverter departmentConverter, SecretaryHistoryConverter secretarHistoryConventer, HeadHistoryConverter headHistoryConverter, AcademicTitleHistoryConverter academicTitleHistoryConverter) {
        this.memberConverter = memberConverter;
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
        this.roleConverter = roleConverter;
        this.departmentConverter = departmentConverter;
        this.secretaryHistoryConverter = secretarHistoryConventer;
        this.headHistoryConverter=headHistoryConverter;
        this.academicTitleHistoryConverter = academicTitleHistoryConverter;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public MemberDto save(MemberDto memberDTO) throws Exception {

        Optional<Member> existingMember = memberRepository.findByFirstnameAndLastname(memberDTO.getFirstname(), memberDTO.getLastname());

        if (existingMember.isPresent()) {
            throw new EntityAlreadyExistsException("Member already exists in the database!");
        }
        Optional<Department> departmentOptional;
        if(memberDTO.getDepartment().getId()!=null){
            departmentOptional = departmentRepository.findById(memberDTO.getDepartment().getId());
        }
        else {
            departmentOptional = departmentRepository.findByName(memberDTO.getDepartment().getName());
        }
        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            Member member = memberConverter.toEntity(memberDTO);
            member.setDepartment(department);
            AcademicTitle academicTitle;
            if(memberDTO.getAcademicTitle().getId()!=null){
                Optional<AcademicTitle> existingAcademicTitle = academicTitleRepository.findById(memberDTO.getAcademicTitle().getId());
                if(existingAcademicTitle.isPresent()){
                    member.setAcademicTitle(existingAcademicTitle.get());
                }
            }
            else{
                academicTitle= academicTitleRepository.findByName(memberDTO.getAcademicTitle().getName())
                        .orElseGet(() -> academicTitleRepository.save(academicTitleConverter.toEntity(memberDTO.getAcademicTitle())));
                member.setAcademicTitle(academicTitle);
            }
            EducationTitle educationTitle;
            if(memberDTO.getEducationTitle().getId()!=null){
                Optional<EducationTitle> existingEducationTitle = educationTitleRepository.findById(memberDTO.getEducationTitle().getId());
                if(existingEducationTitle.isPresent()){
                    member.setEducationTitle(existingEducationTitle.get());
                }
            }
            else{
                educationTitle = educationTitleRepository.findByName(memberDTO.getEducationTitle().getName())
                        .orElseGet(() -> educationTitleRepository.save(educationTitleConverter.toEntity(memberDTO.getEducationTitle())));
                member.setEducationTitle(educationTitle);
            }
            ScientificField scientificField;
            if(memberDTO.getScientificField().getId()!=null){
                Optional<ScientificField> existingScientificField = scientificFieldRepository.findById(memberDTO.getScientificField().getId());
                if(existingScientificField.isPresent()){
                    member.setScientificField(existingScientificField.get());
                }
            }
            else{
                scientificField = scientificFieldRepository.findByName(memberDTO.getScientificField().getName())
                        .orElseGet(() -> scientificFieldRepository.save(scientificFieldConverter.toEntity(memberDTO.getScientificField())));
                member.setScientificField(scientificField);
            }

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

        checkExistingMemberAndThrowException(member, ConstantsCustom.SECRETARY_ROLE_ID, "The department already has a secretary member.");
        member.setRole(roleRepository.findByName(ConstantsCustom.SECRETARY).orElseThrow(() -> new EntityNotFoundException("Secretary role not found")));
        Member savedMember = memberRepository.save(member);
        secretaryHistoryRepository.save(new SecretaryHistory(null, savedMember.getDepartment(), savedMember, LocalDate.now(), null));
    }

    private void handleHeadAttributes(Member member) {
        checkExistingMemberAndThrowException(member, ConstantsCustom.HEAD_ROLE_ID, "The department already has a head member.");
        member.setRole(roleRepository.findByName(ConstantsCustom.HEAD).orElseThrow(() -> new EntityNotFoundException("Head role not found")));
        Member savedMember = memberRepository.save(member);
        headHistoryRepository.save(new HeadHistory(null, savedMember.getDepartment(), savedMember, LocalDate.now(),null));
    }
    private void checkExistingMemberAndThrowException(Member member, Long roleId, String errorMessage) {
        if(roleId==ConstantsCustom.SECRETARY_ROLE_ID) {
            Optional<SecretaryHistory> secretaryHistory = secretaryHistoryRepository.findCurrentSecretaryByDepartmentId(member.getDepartment().getId(), LocalDate.now());
            if(secretaryHistory.get().getStartDate().isBefore(LocalDate.now()) && secretaryHistory.get().getEndDate()==null){
                secretaryHistory.get().setEndDate(LocalDate.now());
                secretaryHistoryRepository.save(secretaryHistory.get());
            }
        }
        if(roleId==ConstantsCustom.HEAD_ROLE_ID) {
            Optional<HeadHistory> headHistory = headHistoryRepository.findCurrentHeadByDepartmentId(member.getDepartment().getId(), LocalDate.now());
            if(headHistory.get().getStartDate().isBefore(LocalDate.now()) && headHistory.get().getEndDate()==null){
                headHistory.get().setEndDate(LocalDate.now());
                headHistoryRepository.save(headHistory.get());
            }
        }
    }

    @Override
    public List<MemberDto> getAll() {
        List<MemberDto> memberDtoList =memberRepository
                                        .findAll()
                                        .stream().map(entity -> memberConverter.toDto((Member) entity))
                                        .collect(Collectors.toList());
        if(memberDtoList.isEmpty()){
            throw new EmptyResponseException("There are no members in the database!");
        }
        return memberDtoList;
    }
    public List<MemberDto> getAllByDepartmentId(Long id) {
        List<MemberDto> memberDtoList=memberRepository
                                        .findAllByDepartmentId(id)
                                        .stream().map(entity -> memberConverter.toDto((Member) entity))
                                        .collect(Collectors.toList());
        if(memberDtoList.isEmpty()){
            throw new EmptyResponseException("There are no members for that department!");
        }
        return memberDtoList;
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


    @Override
    public MemberDto patchUpdateMember(Long memberId, MemberPatchRequest patchRequest) {
        Member existingMember = memberRepository.findById(memberId).orElse(null);

        if (existingMember != null) {
            if (patchRequest.getFirstname() != null) {
                existingMember.setFirstname(patchRequest.getFirstname());
            }
            if (patchRequest.getLastname() != null) {
                existingMember.setLastname(patchRequest.getLastname());
            }
            if (patchRequest.getRole() != null && !patchRequest.getRole().equals(existingMember.getRole())) {
                handleRoleUpdate(existingMember, patchRequest.getRole());
            }
            if (patchRequest.getAcademicTitle() != null && !patchRequest.getAcademicTitle().equals(existingMember.getAcademicTitle())) {
                handleAcademicTitleUpdate(existingMember, patchRequest.getAcademicTitle());
            }
            if (patchRequest.getDepartment() != null && !patchRequest.getDepartment().equals(existingMember.getDepartment())) {
                handleDepartmentUpdate(existingMember, patchRequest.getDepartment());
            }
            if (patchRequest.getEducationTitle() != null && !patchRequest.getEducationTitle().equals(existingMember.getEducationTitle())) {
                handleEducationTitle(existingMember, patchRequest.getEducationTitle());
            }
            if (patchRequest.getScientificField() != null && !patchRequest.getScientificField().equals(existingMember.getScientificField())) {
                handleScientificFieldUpdate(existingMember, patchRequest.getScientificField());
            }
            existingMember = memberRepository.save(existingMember);

            return memberConverter.toDto(existingMember);
        }

        return null;
    }

    private void handleScientificFieldUpdate(Member existingMember, ScientificFieldDto scientificField) {
        Optional<ScientificField> existingScientificField;
        if(scientificField.getId()!=null){
            existingScientificField = scientificFieldRepository.findById(scientificField.getId());
        }
        else{
            existingScientificField= scientificFieldRepository.findByName(scientificField.getName());
        }
        if(existingScientificField.isPresent()){
            existingMember.setScientificField(existingScientificField.get());
        }
        else{
            ScientificField newScientificField= scientificFieldRepository.save(scientificFieldConverter.toEntity(scientificField));
            existingMember.setScientificField(newScientificField);
        }
    }

    @Override
    public MemberDto findById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            return memberConverter.toDto(member.get());
        } else {
            throw new EntityNotFoundException("Member does not exist!");
        }
    }

    @Override
    public List<SecretaryHistoryDto> getAllHistorySecretary(Long id) {
        Optional<Member> existingMember = memberRepository.findById(id);
        if(!existingMember.isPresent()){
            throw new EntityNotFoundException("There is no member with that id!");
        }
        else{
            return secretaryHistoryRepository.findByMemberId(id).
                    stream().
                    map(entity -> secretaryHistoryConverter.toDto(entity))
                    .collect(Collectors.toList());
        }

    }

    @Override
    public List<HeadHistoryDto> getAllHistoryHead(Long id) {
        Optional<Member> existingMember = memberRepository.findById(id);
        if(!existingMember.isPresent()){
            throw new EntityNotFoundException("There is no member with that id!");
        }
        else{
            return headHistoryRepository.findByMemberId(id).
                    stream().
                    map(entity -> headHistoryConverter.toDto(entity))
                    .collect(Collectors.toList());
        }

    }

    @Override
    public List<AcademicTitleHistoryDto> getAllAcademicTitleHistory(Long id) {
        Optional<Member> existingMember = memberRepository.findById(id);
        if(!existingMember.isPresent()){
            throw new EntityNotFoundException("There is no member with that id!");
        }
        else{
            return academicTitleHistoryRepository.findByMemberIdOrderByStartDate(id).
                    stream().
                    map(entity -> academicTitleHistoryConverter.toDto(entity))
                    .collect(Collectors.toList());
        }
    }

    private void handleEducationTitle(Member existingMember, EducationTitleDto educationTitle) {
        Optional<EducationTitle> existingEducationTitle;
        if(educationTitle.getId()!=null){
            existingEducationTitle = educationTitleRepository.findById(educationTitle.getId());
        }
        else{
            existingEducationTitle= educationTitleRepository.findByName(educationTitle.getName());
        }
        if(existingEducationTitle.isPresent()){
            existingMember.setEducationTitle(existingEducationTitle.get());
        }
        else{
            EducationTitle newEducationTitle= educationTitleRepository.save(educationTitleConverter.toEntity(educationTitle));
            existingMember.setEducationTitle(newEducationTitle);
        }

    }

    private void handleDepartmentUpdate(Member existingMember, DepartmentDto department) {
        if(existingMember.getRole().getName().equals("Secretary") ||existingMember.getRole().getName().equals("Head") ){
            throw new IllegalArgumentException("Member can't change department if it is at Head or Secretary position!");
        }
        else{
            Optional<Department> existingDepartment= departmentRepository.findByName(department.getName());
            if(existingDepartment.isPresent()){
                existingMember.setDepartment(existingDepartment.get());
            }
            else{
                Department newDepartment= departmentRepository.save(departmentConverter.toEntity(department));
                existingMember.setDepartment(newDepartment);
            }
        }
    }

    private void handleAcademicTitleUpdate(Member existingMember, AcademicTitleDto academicTitle) {

        Optional<AcademicTitleHistory> academicTitleHistory= academicTitleHistoryRepository.findCurrentAcademicTitleByMemberId(existingMember.getId());
        if(academicTitleHistory.isPresent()){
            academicTitleHistory.get().setEndDate(LocalDate.now());
            academicTitleHistoryRepository.save(academicTitleHistory.get());
            Optional<AcademicTitle> existingAcademicTitle;
            if(academicTitle.getId()!=null){
                existingAcademicTitle = academicTitleRepository.findById(academicTitle.getId());
            }
            else{
                existingAcademicTitle= academicTitleRepository.findByName(academicTitle.getName());
            }
            if(existingAcademicTitle.isPresent()){
                existingMember.setAcademicTitle(existingAcademicTitle.get());
            }
            else{
                AcademicTitle newAcademicTitle= academicTitleRepository.save(academicTitleConverter.toEntity(academicTitle));
                existingMember.setAcademicTitle(newAcademicTitle);
            }
            academicTitleHistoryRepository.save(AcademicTitleHistory.builder().
                    academicTitle(existingMember.getAcademicTitle()).
                    member(existingMember).scientificField(existingMember.getScientificField()).
                    startDate(LocalDate.now()).
                    endDate(null).build());
        }


    }

    private void handleRoleUpdate(Member existingMember, RoleDto role) {
        Optional<SecretaryHistory> existingHistory=secretaryHistoryRepository.findCurrentByMemberId(existingMember.getId(), LocalDate.now());
        Optional<HeadHistory> existingHistoryHead=headHistoryRepository.findCurrentByMemberId(existingMember.getId());
        switch (existingMember.getRole().getName()){
            case(ConstantsCustom.SECRETARY):
                if(existingHistoryHead.isPresent()){
                    throw new IllegalArgumentException("Member "+existingMember.getFirstname()+" "+existingMember.getLastname()+" is active HEAD!");
                }
                if(existingHistory.isPresent()){
                    existingHistory.get().setEndDate(LocalDate.now());
                    secretaryHistoryRepository.save(existingHistory.get());
                }
                break;
            case(ConstantsCustom.HEAD):
                if(existingHistory.isPresent()){
                    throw new IllegalArgumentException("Member "+existingMember.getFirstname()+" "+existingMember.getLastname()+" is active SECRETARY!");
                }
                if(existingHistoryHead.isPresent()){
                    existingHistoryHead.get().setEndDate(LocalDate.now());
                    headHistoryRepository.save(existingHistoryHead.get());
                }
                break;
        }

       switch (role.getName()){
           case(ConstantsCustom.SECRETARY):
               handleSecretaryAttributes(existingMember);
               break;
           case(ConstantsCustom.HEAD):
               handleHeadAttributes(existingMember);
               break;
           default:
               setDefaultMemberAttributes(existingMember);
       }
    }


}
