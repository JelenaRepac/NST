/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.controller;

import jakarta.validation.Valid;
import java.util.List;
import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.dto.DepartmentDto;
import nst.springboot.nstapplication.service.DepartmentService;
import nst.springboot.nstapplication.exception.DepartmentAlreadyExistException;
import nst.springboot.nstapplication.exception.MyErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println(deptDto.toString());
        return new ResponseEntity<>(deptDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAll() {
        List<DepartmentDto> departments = departmentService.getAll();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public DepartmentDto findById(@PathVariable("id") Long id) throws Exception {
        return departmentService.findById(id);
    }
    @GetMapping("/query")
    public Department queryById(@RequestParam("id") Long id) throws Exception {
        throw new Exception("Nije implementirana.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {

        departmentService.delete(id);
        return new ResponseEntity<>("Department removed!", HttpStatus.OK);

    }

}
