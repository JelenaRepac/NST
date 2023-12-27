package nst.springboot.nstapplication.controller;

import jakarta.validation.Valid;
import nst.springboot.nstapplication.constants.ConstantsCustom;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.MemberHeadSecretaryDto;
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
    public ResponseEntity<MemberDto> save(@Valid @RequestBody MemberHeadSecretaryDto memberDTO) throws Exception {
        MemberDto member=  memberService.save(memberDTO);
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }
//    @PutMapping("/{id}")
//    public ResponseEntity<MemberDto> update(@Valid @RequestBody MemberHeadSecretaryDto memberDTO, @PathVariable Long id) throws Exception {
//        MemberDto member = memberService.update(memberDTO, id);
//        return new ResponseEntity<>(member, HttpStatus.CREATED);
//    }
    @GetMapping
    public ResponseEntity<List<MemberDto>> getAll() {
        List<MemberDto> members = memberService.getAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }
    @GetMapping("/department/{id}")
    public ResponseEntity<List<MemberDto>> findByDepartmentId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(memberService.getAllByDepartmentId(id), HttpStatus.OK) ;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        memberService.delete(id);
        return new ResponseEntity<>("Member removed!", HttpStatus.OK);

    }
}
