package nst.springboot.nstapplication.service.impl;

import nst.springboot.nstapplication.converter.impl.MemberConverter;
import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.repository.DepartmentRepository;
import nst.springboot.nstapplication.repository.MemberRepository;
import nst.springboot.nstapplication.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberConverter memberConverter;

    private MemberRepository memberRepository;

    private DepartmentRepository departmentRepository;

    public MemberServiceImpl(MemberConverter memberConverter, MemberRepository memberRepository, DepartmentRepository departmentRepository) {
        this.memberConverter = memberConverter;
        this.memberRepository = memberRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public MemberDto save(MemberDto memberDTO) throws Exception {
        Member member = memberConverter.toEntity(memberDTO);
        if(member.getDepartment().getId()==null){
            throw new Exception("Department id is required!");
        }else{
            Optional<Department> dep = departmentRepository.findById(member.getDepartment().getId());
            if(dep.isEmpty()){
                throw new Exception("Department doesn't exist!");
            }else {
                Department department = dep.get();
//                System.out.println(department.getHead()+"   before");
//                department.setAllMembers();
//                System.out.println(department.getHead()+"   after");
//                validateMember(member,department);
//                memberRepository.save(member);
//                dep.get().addMember(member);
                departmentRepository.save(dep.get());
            }
        }
        return memberDTO;
        //ako department ne postoji sacuvaj i department zajedno sa Subject/om
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
    public void delete(Long id) throws Exception {
//        Optional<Member> member = memberRepository.findById(id);
//        if (member.isPresent()) {
//            Member member1 = member.get();
//            memberRepository.delete(member1);
//        } else {
//            throw new Exception("Member does not exist!");
//        }
    }

    @Override
    public MemberDto update(MemberDto memberDTO, Long id) throws Exception {
        Member member1 = memberConverter.toEntity(memberDTO);
      //  member1 = memberRepository.save(member1);
        return memberConverter.toDto(member1);
    }

    @Override
    public MemberDto findById(Long id) throws Exception {
        Optional member = memberRepository.findById(id);
        if (member.isPresent()) {
            Member member1 = (Member) member.get();
            return memberConverter.toDto(member1);
        } else {
            throw new Exception("Member title does not exist!");
        }
    }

    private void validateMember(Member newMember,Department department) throws Exception {
//        if ((newMember.getAcademicTitle().getId().equals(7L) && department.getHead() != null)
//                || (newMember.getAcademicTitle().getId().equals(6L) && department.getSecretary() != null)) {
//            throw new Exception("The department already has a member with the specified academic title.");
//        }
    }
}
