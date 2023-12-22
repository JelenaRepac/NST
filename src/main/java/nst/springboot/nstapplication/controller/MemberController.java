package nst.springboot.nstapplication.controller;

import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.MemberDto;
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


    @PostMapping
    public ResponseEntity<MemberDto> save(@Valid @RequestBody MemberDto memberDTO) throws Exception {
        //ResponseEntity
        MemberDto member = memberService.save(memberDTO);
        return new ResponseEntity<>(member, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> update(@Valid @RequestBody MemberDto memberDTO, @RequestParam Long id) throws Exception {
        //ResponseEntity
       // Long idA = memberService.findById(id).getAcademicTitle().getId();
        MemberDto member = memberService.update(memberDTO, id);
//        if(!(memberDTO.getAcademicTitle().getId().equals(idA))) {
//            calendar.setTime(new Date());
//            Date now = calendar.getTime();
//            calendar.add(Calendar.YEAR, 1);
//            athService.save(new ATHDto(null,now,calendar.getTime(),member,memberDTO.getAcademicTitle(),member.getScientificField()));
//        }
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MemberDto>> getAll() {
        List<MemberDto> members = memberService.getAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public MemberDto findById(@PathVariable("id") Long id) throws Exception {
        System.out.println("Controller: " + id);
        return memberService.findById(id);
    }

    @GetMapping("/query")
    public MemberDto queryById(@RequestParam("id") Long id) throws Exception {
        System.out.println("Controller: " + id);
        return memberService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        memberService.delete(id);
        return new ResponseEntity<>("Member removed!", HttpStatus.OK);

    }
}
