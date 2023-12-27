package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.MemberHeadSecretaryDto;

import java.util.List;

public interface MemberService {
    MemberDto save(MemberHeadSecretaryDto memberDTO);
    List<MemberDto> getAll();
    List<MemberDto> getAllByDepartmentId(Long id);
    void delete(Long id) ;
}
