package nst.springboot.nstapplication.service;

import nst.springboot.nstapplication.dto.MemberDto;

import java.util.List;

public interface MemberService {
    MemberDto save(MemberDto memberDTO)throws Exception;
    List<MemberDto> getAll();
    List<MemberDto> getAllByDepartmentId(Long id);
    void delete(Long id) throws Exception;
    MemberDto update(MemberDto memberDTO, Long id)throws Exception;
    MemberDto findById(Long id)throws Exception;
}
