package nst.springboot.nstapplication.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.service.AcademicTitleService;
import nst.springboot.nstapplication.service.EducationTitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/educationTitle")
@Hidden
public class EducationTitleController {
    //Controller for education title
    //ENDPOINTS -> saving, getAll, getById, partialUpdate
    private EducationTitleService educationTitleService;

    public EducationTitleController(EducationTitleService educationTitleService) {
        this.educationTitleService = educationTitleService;
    }

    @PostMapping
    public ResponseEntity<EducationTitleDto> save(@Valid @RequestBody EducationTitleDto educationTitleDto)  {
        EducationTitleDto titleDTO = educationTitleService.save(educationTitleDto);
        return new ResponseEntity<>(titleDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EducationTitleDto>> getAll() {
        List<EducationTitleDto> titles = educationTitleService.getAll();
        return new ResponseEntity<>(titles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public EducationTitleDto findById(@PathVariable("id") Long id) throws Exception {
        return educationTitleService.findById(id);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<EducationTitleDto> partialUpdate(
            @PathVariable(name = "id") Long id,
            @RequestBody Map<String, String> updates) {

        EducationTitleDto updatedEducationTitle = educationTitleService.partialUpdate(id, updates);
        return ResponseEntity.ok(updatedEducationTitle);

    }
}
