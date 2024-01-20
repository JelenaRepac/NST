/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.controller;

import jakarta.validation.Valid;
import java.util.List;
import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.dto.DepartmentDto;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.dto.MemberDto;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author student2
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;


    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> save(@Valid @RequestBody DepartmentDto departmentDto) throws Exception {
        DepartmentDto deptDto = departmentService.save(departmentDto);
        return new ResponseEntity<>(deptDto, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAll() {
        List<DepartmentDto> departments = departmentService.getAll();
        return new ResponseEntity(departments, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public DepartmentDto findById(@PathVariable("id") Long id)  {
        return departmentService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id)  {
        departmentService.delete(id);
        return new ResponseEntity<>("Department removed!", HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> update(@PathVariable(name = "id") Long id, @Valid @RequestBody DepartmentDto departmentDto) throws Exception {
        return new ResponseEntity<>(departmentService.update(id, departmentDto), HttpStatus.OK);
    }
    @GetMapping("{id}/secretary")
    public ResponseEntity<MemberDto> getActiveSecretaryForDepartment(@PathVariable("id") Long id) {
        return new ResponseEntity<>(departmentService.getActiveSecretaryForDepartment(id), HttpStatus.OK) ;
    }
    @GetMapping("{id}/secretary-history")
    public ResponseEntity<List<SecretaryHistoryDto>> getSecretaryHistoryForDepartment(@PathVariable("id") Long id) {
        return new ResponseEntity<>(departmentService.getSecretaryHistoryForDepartment(id), HttpStatus.OK) ;
    }
    @GetMapping("{id}/head")
    public ResponseEntity<MemberDto> getActiveHeadForDepartment(@PathVariable("id") Long id) {
        return new ResponseEntity<>(departmentService.getActiveHeadForDepartment(id), HttpStatus.OK) ;
    }
    @GetMapping("{id}/head-history")
    public ResponseEntity<List<HeadHistoryDto>> getHeadHistoryForDepartment(@PathVariable("id") Long id) {
        return new ResponseEntity<>(departmentService.getHeadHistoryForDepartment(id), HttpStatus.OK) ;
    }


    @GetMapping("/{id}/member")
    public ResponseEntity<List<MemberDto>> getAllMembersByDepartmentId(@PathVariable("id") Long id){
        return new ResponseEntity<>(departmentService.getAllMembersByDepartmentId(id), HttpStatus.OK);
    }



}
