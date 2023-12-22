/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.controller;

import jakarta.validation.Valid;
import java.util.List;
import nst.springboot.nstapplication.service.SubjectService;
import nst.springboot.nstapplication.dto.SubjectDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/subject")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<SubjectDto> save(@Valid @RequestBody SubjectDto subject) throws Exception {
        //ResponseEntity
        SubjectDto subjectDto = subjectService.save(subject);
        return new ResponseEntity<>(subjectDto, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAll() {
        List<SubjectDto> subjects = subjectService.getAll();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public SubjectDto findById(@PathVariable("id") Long id) throws Exception {
        return subjectService.findById(id);
    }

    @GetMapping("/query")
    public SubjectDto queryById(@RequestParam("id") Long id) throws Exception {
        return subjectService.findById(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {

        
        subjectService.delete(id);
        return new ResponseEntity<>("Department removed!", HttpStatus.OK);

    }
    
    
}
