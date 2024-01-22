package nst.springboot.nstapplication.controller;

import jakarta.validation.Valid;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.dto.*;
import nst.springboot.nstapplication.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    //saving default member
    @PostMapping
    public ResponseEntity<MemberDto> save(@Valid @RequestBody MemberDto memberDTO) throws Exception {
        MemberDto member=  memberService.save(memberDTO);
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @RequestBody MemberPatchRequest patchRequest) {
        MemberDto updatedMember = memberService.patchUpdateMember(id, patchRequest);
        if (updatedMember != null) {
            return ResponseEntity.ok(updatedMember);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<MemberDto>> getAll() {
        List<MemberDto> members = memberService.getAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable("id") Long id) {
        MemberDto member = memberService.findById(id);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }
//    @GetMapping("/department/{id}")
//    public ResponseEntity<List<MemberDto>> findByDepartmentId(@PathVariable("id") Long id) {
//        return new ResponseEntity<>(memberService.getAllByDepartmentId(id), HttpStatus.OK) ;
//    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        memberService.delete(id);
        return new ResponseEntity<>("Member removed!", HttpStatus.OK);
    }
    @GetMapping("/{id}/secretary-history")
    public ResponseEntity<List<SecretaryHistoryDto>> getAllHistorySecretary(@PathVariable  Long id){
       return new ResponseEntity<>( memberService.getAllHistorySecretary(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/head-history")
    public ResponseEntity<List<HeadHistoryDto>> getAllHistoryHead(@PathVariable  Long id){
        return new ResponseEntity<>( memberService.getAllHistoryHead(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/academic-title-history")
    public ResponseEntity<List<AcademicTitleHistoryDto>> getAllAcademicTitleHistory(@PathVariable  Long id){
        return new ResponseEntity<>( memberService.getAllAcademicTitleHistory(id), HttpStatus.OK);
    }

}
