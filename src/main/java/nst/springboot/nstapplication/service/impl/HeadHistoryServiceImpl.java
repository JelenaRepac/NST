package nst.springboot.nstapplication.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nst.springboot.nstapplication.constants.ConstantsCustom;
import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.converter.impl.HeadHistoryConverter;
import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.converter.impl.SecretaryHistoryConverter;
import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.exception.EmptyResponseException;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.exception.IllegalArgumentException;
import nst.springboot.nstapplication.repository.DepartmentRepository;
import nst.springboot.nstapplication.repository.HeadHistoryRepository;
import nst.springboot.nstapplication.repository.MemberRepository;
import nst.springboot.nstapplication.repository.SecretaryHistoryRepository;
import nst.springboot.nstapplication.service.HeadHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeadHistoryServiceImpl implements HeadHistoryService{
    private final HeadHistoryRepository repository;
    private final HeadHistoryConverter headHistoryConverter;
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final DepartmentRepository departmentRepository;
    private final DepartmentConverter departmentConverter;
    private final SecretaryHistoryRepository secretaryHistoryRepository;
    private final MemberServiceImpl  memberService;


    @Override
    @Transactional()
    public HeadHistoryDto save(HeadHistoryDto headHistoryDto){
        //Ako se posalje null za start date postavlja se danasnji
        if(headHistoryDto.getStartDate() == null){
            headHistoryDto.setStartDate(LocalDate.now());
        }
        //Ako je end date pre start date a
        if(headHistoryDto.getEndDate()!=null && headHistoryDto.getStartDate()!= null){
            if(headHistoryDto.getEndDate().isBefore(headHistoryDto.getStartDate())){
                throw new IllegalArgumentException("End date can't be before start date!");
            }
        }
        //Provera za clana, da li postoji
        Optional<Member> existingMember;
        if(headHistoryDto.getHead().getId()!=null){
            existingMember = memberRepository.findById(headHistoryDto.getHead().getId());
            if(existingMember.isPresent()){
                headHistoryDto.setHead(memberConverter.toDto(existingMember.get()));
            }
            else{
                throw new EntityNotFoundException("There is no member with that id!");
            }
        }
        else{
            existingMember = memberRepository.findByFirstnameAndLastname(headHistoryDto.getHead().getFirstname(), headHistoryDto.getHead().getLastname());
            if(existingMember.isPresent()){
                headHistoryDto.setHead(memberConverter.toDto(existingMember.get()));
            }
            else{
                memberRepository.save(memberConverter.toEntity(headHistoryDto.getHead()));
                Optional<Member> member = memberRepository.findByFirstnameAndLastname(headHistoryDto.getHead().getFirstname(), headHistoryDto.getHead().getLastname());
                if(member.isPresent()){
                    headHistoryDto.setHead(memberConverter.toDto(member.get()));
                }
            }
        }
        //Provera za katedru i da li clan pripada toj katedri
        Optional<Department> existingDepartment;
        if(headHistoryDto.getDepartment().getId()!=null) {
            existingDepartment = departmentRepository.findById(headHistoryDto.getDepartment().getId());
            if(existingDepartment.isPresent()){
                if(headHistoryDto.getHead().getDepartment().getId() != headHistoryDto.getDepartment().getId()){
                    throw new IllegalArgumentException("Member is not in that department! He is in  " + headHistoryDto.getHead().getDepartment().getName() +".");
                }
                headHistoryDto.setDepartment(departmentConverter.toDto(existingDepartment.get()));
            }
            else{
                throw new EntityNotFoundException("There is no department with that id!");
            }
        }
        else{
            existingDepartment = departmentRepository.findByName(headHistoryDto.getDepartment().getName());
            if(existingDepartment.isPresent()){
                if(headHistoryDto.getHead().getDepartment().getId() != existingDepartment.get().getId()){
                    throw new IllegalArgumentException("Member is not in that department! He is in  " + headHistoryDto.getHead().getDepartment().getName() +".");
                }
                headHistoryDto.setDepartment(departmentConverter.toDto(existingDepartment.get()));
            }
            else{
                departmentRepository.save(departmentConverter.toEntity(headHistoryDto.getDepartment()));
                Optional<Department> department= departmentRepository.findByName(headHistoryDto.getDepartment().getName());
                if(department.isPresent()){
                    headHistoryDto.setDepartment(departmentConverter.toDto(department.get()));
                }

            }
        }
        //Lista istorija za konkretnu katedru
        List<HeadHistory> existingHistoryList = repository.findByDepartmentId(headHistoryDto.getDepartment().getId());
        //Ukoliko se posalje end date null, a postoji trenutno aktivni sef, a
        // prosledjeni start date je nakon tog postojeceg
        // postojeci se setuje end date na sadasnji
        // u drugom slucaju exc
        for (HeadHistory existingHistory : existingHistoryList) {
            Optional<Member> member = memberRepository.findById(existingHistory.getMember().getId());
            if(headHistoryDto.getEndDate() ==null && existingHistory.getEndDate() ==null) {
                if(headHistoryDto.getStartDate().isAfter(existingHistory.getStartDate())){
                    existingHistory.setEndDate(headHistoryDto.getStartDate());
                    Optional<Member> activeMember = memberRepository.findById(existingHistory.getMember().getId());
                    if(activeMember.isPresent()){
                        activeMember.get().setRole(Role.builder().id(ConstantsCustom.DEFAULT_ROLE_ID).name(ConstantsCustom.DEFAULT_ROLE).build());
                        memberService.patchUpdateMember(activeMember.get().getId(), activeMember.get());
                        repository.save(existingHistory);
                    }
                }
               else{
                    throw new IllegalArgumentException("Member "+existingHistory.getMember().getFirstname()+
                            " "+existingHistory.getMember().getLastname()+" is at the SECRETARY postion from "+
                            existingHistory.getStartDate());
                }
            }
            //Ukoliko su oba datuma poslata, provera da li postoji preklapanja medju sadasnjim istorijama
            if(existingHistory.getStartDate()!= null && existingHistory.getEndDate() !=null &&  headHistoryDto.getStartDate()!=null && headHistoryDto.getEndDate()!=null) {
                if (isDateOverlap(existingHistory.getStartDate(), existingHistory.getEndDate(),
                        headHistoryDto.getStartDate(), headHistoryDto.getEndDate())) {
                    throw new IllegalArgumentException("The member " + member.get().getFirstname() + " " +
                            member.get().getLastname() +
                            " already was at the HEAD position from " + existingHistory.getStartDate() + " to " + existingHistory.getEndDate() +
                            " in department " + headHistoryDto.getDepartment().getName());
                }
            }
        }
        //Provera da li je clan trenutno na poziciji sekretara
        Optional<SecretaryHistory> activeSecretary = secretaryHistoryRepository.findCurrentByMemberId(headHistoryDto.getHead().getId(), LocalDate.now());
        if(activeSecretary.isPresent()){
            if (headHistoryDto.getEndDate()==null &&
                    ( headHistoryDto.getStartDate().isAfter(activeSecretary.get().getStartDate()) ||  headHistoryDto.getStartDate().isEqual(activeSecretary.get().getStartDate()))){
                throw new IllegalArgumentException("Member "+headHistoryDto.getHead().getFirstname()+" "+headHistoryDto.getHead().getLastname()+" " +
                        "can't be HEAD because member is at the SECRETARY position from "+activeSecretary.get().getStartDate()+ " for department "+activeSecretary.get().getDepartment().getName());
            }
        }

        if(headHistoryDto.getStartDate()!=null &&
                (headHistoryDto.getStartDate().isBefore(LocalDate.now()) || headHistoryDto.getStartDate().isEqual(LocalDate.now())) &&
                (headHistoryDto.getEndDate()==null || headHistoryDto.getEndDate().isAfter(LocalDate.now()))){
            existingMember.get().setRole(Role.builder().name(ConstantsCustom.HEAD).id(ConstantsCustom.HEAD_ROLE_ID).build());
            headHistoryDto.setHead(memberService.patchUpdateMember(existingMember.get().getId(),existingMember.get()));

        }

        return headHistoryConverter.toDto(repository.save(headHistoryConverter.toEntity(headHistoryDto)));
    }
    private boolean isDateOverlap(LocalDate startDate1, LocalDate endDate1, LocalDate startDate2, LocalDate endDate2) {
        if(startDate1!= null && endDate1 !=null && startDate2!=null && endDate2!=null) {
            return startDate1.isBefore(endDate2) && endDate1.isAfter(startDate2);
        }
        return false;
    }

    @Override
    public List<HeadHistoryDto> getAll() {
        return repository
                .findAll()
                .stream().map(entity -> headHistoryConverter.toDto(entity))
                .collect(Collectors.toList());
    }
    @Override
    public HeadHistoryDto getByDepartmentId(Long id){
        Optional<HeadHistory> headHistory = repository.findByDepartmentIdAndEndDateNull(id);
        if(headHistory.isEmpty()){
            throw new EntityNotFoundException("Department doesn't have active head member");
        }
        return headHistoryConverter.toDto(headHistory.get());
}
    @Override
    public void delete(Long id)  {
        Optional<HeadHistory> history = repository.findById(id);
        if (history.isPresent()) {
            HeadHistory history1 = history.get();
            repository.delete(history1);
        } else {
            throw new EntityNotFoundException("Head history does not exists!");
        }
    }

    @Override
    public HeadHistoryDto findById(Long id) {
        Optional<HeadHistory> history = repository.findById(id);
        if (history.isPresent()) {
            HeadHistory history1 = history.get();
            return headHistoryConverter.toDto(history1);
        } else {
            throw new EntityNotFoundException("Head history does not exists!");
        }
    }

    @Override
    public List<HeadHistoryDto> getHistoryForDepartmentId(Long id) {
        List<HeadHistory> headHistoryList = repository.findByDepartmentId(id);
        if (headHistoryList.isEmpty()) {
            throw new EmptyResponseException("There are no head history for department!");
        }
        List<HeadHistoryDto> headHistoryDtoList = new ArrayList<>();
        for (HeadHistory hh : headHistoryList) {
            headHistoryDtoList.add(headHistoryConverter.toDto(hh));
        }
        return headHistoryDtoList;

    }

    @Override
    @Transactional
    public HeadHistoryDto patchHeadHistory(Long id, HeadHistoryDto headHistoryDto) {
        if(headHistoryDto.getEndDate()!=null && headHistoryDto.getStartDate()!= null){
            if(headHistoryDto.getEndDate().isBefore(headHistoryDto.getStartDate())){
                throw new IllegalArgumentException("End date can't be before start date!");
            }
        }
        Optional<HeadHistory> existingHeadHistory = repository.findById(id);
        if(!existingHeadHistory.isPresent()){
            throw new EntityNotFoundException("There is no history with that id!");
        }

        if(headHistoryDto.getHead() == null){
            headHistoryDto.setHead(memberConverter.toDto(existingHeadHistory.get().getMember()));
        }
        Optional<Member> existingMember = memberRepository.findById(headHistoryDto.getHead().getId());
        if(!existingMember.isPresent()){
            throw new IllegalArgumentException("There is no member with that id!");
        }
        headHistoryDto.setHead(memberConverter.toDto(existingMember.get()));
        if(headHistoryDto.getHead().getId()!=existingHeadHistory.get().getMember().getId()){
            throw new IllegalArgumentException("You want to change history for member "+headHistoryDto.getHead().getFirstname()+" "+headHistoryDto.getHead().getLastname()+
                    ", but the specific history is for "+existingHeadHistory.get().getMember().getFirstname()+" "+existingHeadHistory.get().getMember().getLastname());

        }


        if(headHistoryDto.getDepartment() == null){
            headHistoryDto.setDepartment(departmentConverter.toDto(existingHeadHistory.get().getDepartment()));
        }
        Optional<Department> existingDepartment = departmentRepository.findById(headHistoryDto.getDepartment().getId());
        if(!existingDepartment.isPresent()){
            throw new IllegalArgumentException("There is no department with that id!");
        }
        headHistoryDto.setDepartment(departmentConverter.toDto(existingDepartment.get()));
        if(headHistoryDto.getDepartment().getId()!=existingHeadHistory.get().getDepartment().getId()){
            throw new IllegalArgumentException("You want to change history for department "+headHistoryDto.getDepartment().getName()+
                  ", but the specific history is for "+existingHeadHistory.get().getDepartment().getName());

        }

        List<HeadHistory> existingHistories = repository.findByDepartmentId(headHistoryDto.getDepartment().getId());
        for(HeadHistory headHistory : existingHistories) {
            if (headHistoryDto.getStartDate() != null && headHistoryDto.getEndDate() != null && headHistory.getStartDate() != null && headHistory.getEndDate() != null) {
                if (isDateOverlap(headHistoryDto.getStartDate(), headHistoryDto.getEndDate(),
                        headHistory.getStartDate(), headHistory.getEndDate())) {
                    throw new IllegalArgumentException("The member " + headHistory.getMember().getFirstname() + " " +
                            headHistory.getMember().getLastname() +
                            " already was at the SECRETARY position from " + headHistory.getStartDate() + " to " + headHistory.getEndDate() +
                            " in department " + headHistory.getDepartment().getName());
                }
            }
        }
        Optional<HeadHistory> activeHead = repository.findByDepartmentIdAndEndDateNull(headHistoryDto.getDepartment().getId());
        if(activeHead.isPresent()){
            if(headHistoryDto.getEndDate() == null){
                if(headHistoryDto.getStartDate().isAfter(activeHead.get().getStartDate())){
                    activeHead.get().setEndDate(headHistoryDto.getStartDate());
                    Optional<Member> member = memberRepository.findById(activeHead.get().getMember().getId());
                    if (member.isPresent()) {
                        member.get().setRole(Role.builder().id(ConstantsCustom.DEFAULT_ROLE_ID).name(ConstantsCustom.DEFAULT_ROLE).build());
                        activeHead.get().setMember(memberConverter.toEntity(memberService.patchUpdateMember(member.get().getId(),existingMember.get())));
                        repository.save(activeHead.get());
                    }
                }
            }
        }

        existingHeadHistory.get().setStartDate(headHistoryDto.getStartDate());
        existingHeadHistory.get().setEndDate(headHistoryDto.getEndDate());


        if(existingHeadHistory.get().getStartDate()!=null && existingHeadHistory.get().getStartDate().isBefore(LocalDate.now())
                && (existingHeadHistory.get().getEndDate()==null || existingHeadHistory.get().getEndDate().isAfter(LocalDate.now()))){
            existingMember.get().setRole(Role.builder().id(ConstantsCustom.SECRETARY_ROLE_ID).name(ConstantsCustom.SECRETARY).build());
            existingHeadHistory.get().setMember(memberConverter.toEntity(memberService.patchUpdateMember(existingMember.get().getId(),existingMember.get())));
        }
        return headHistoryConverter.toDto(repository.save(existingHeadHistory.get()));
    }

}
