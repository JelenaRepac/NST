package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.MemberHeadSecretaryDto;
import nst.springboot.nstapplication.dto.MemberPatchRequest;

import java.util.List;
import java.util.Map;

public interface MemberService {
    MemberDto save(MemberDto memberDTO) throws Exception;
    List<MemberDto> getAll();
    List<MemberDto> getAllByDepartmentId(Long id);
    void delete(Long id) ;

    MemberDto patchUpdateMember(Long memberId, MemberPatchRequest patchRequest);
}
