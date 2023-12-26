package nst.springboot.nstapplication.controller;
import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.exception.EntityNotFoundException;
import nst.springboot.nstapplication.service.AcademicTitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/academicTitle")
public class AcademicTitleController {

    private AcademicTitleService academicTitleService;

    public AcademicTitleController(AcademicTitleService academicTitleService) {
        this.academicTitleService = academicTitleService;
    }

    @PostMapping
    public ResponseEntity<AcademicTitleDto> save(@Valid @RequestBody AcademicTitleDto academicTitleDTO)  {
        AcademicTitleDto titleDTO = academicTitleService.save(academicTitleDTO);
        return new ResponseEntity<>(titleDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AcademicTitleDto>> getAll() {
        List<AcademicTitleDto> titles = academicTitleService.getAll();
        return new ResponseEntity<>(titles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public AcademicTitleDto findById(@PathVariable("id") Long id) throws Exception {
        return academicTitleService.findById(id);
    }

    @GetMapping("/query")
    public AcademicTitleDto queryById(@RequestParam("id") Long id) throws Exception {
        return academicTitleService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        academicTitleService.delete(id);
        return new ResponseEntity<>("Academic title removed!", HttpStatus.OK);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<AcademicTitleDto> partialUpdate(
            @PathVariable(name = "id") Long id,
            @RequestBody Map<String, String> updates) {

        AcademicTitleDto updatedAcademicTitle = academicTitleService.partialUpdate(id, updates);
        return ResponseEntity.ok(updatedAcademicTitle);

    }
}
