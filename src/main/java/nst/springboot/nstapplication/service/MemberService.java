package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.dto.*;

import java.util.List;
import java.util.Map;

public interface MemberService {
    MemberDto save(MemberDto memberDTO) throws Exception;
    List<MemberDto> getAll();
    void delete(Long id) ;
    MemberDto patchUpdateMember(Long memberId, Member patchRequest);
    MemberDto findById(Long id);
    List<SecretaryHistoryDto> getAllHistorySecretary(Long id);

    List<HeadHistoryDto> getAllHistoryHead(Long id);

    List<AcademicTitleHistoryDto> getAllAcademicTitleHistory(Long id);
}
