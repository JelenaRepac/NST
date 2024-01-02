package nst.springboot.nstapplication.controller;
import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.service.AcademicTitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/academicTitle")
public class AcademicTitleController {
    //Controller for academic title
    //ENDPOINTS -> saving, getAll, getById, partialUpdate
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

    @PatchMapping("/{id}")
    public ResponseEntity<AcademicTitleDto> partialUpdate(
            @PathVariable(name = "id") Long id,
            @RequestBody Map<String, String> updates) {

        AcademicTitleDto updatedAcademicTitle = academicTitleService.partialUpdate(id, updates);
        return ResponseEntity.ok(updatedAcademicTitle);

    }
}
