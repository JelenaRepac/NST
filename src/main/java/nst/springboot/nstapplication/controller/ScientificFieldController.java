package nst.springboot.nstapplication.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.EducationTitleDto;
import nst.springboot.nstapplication.dto.ScientificFieldDto;
import nst.springboot.nstapplication.service.EducationTitleService;
import nst.springboot.nstapplication.service.ScientificFieldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scientificField")
@Hidden
public class ScientificFieldController {
    //Controller for scientificField
    //ENDPOINTS -> saving, getAll, getById, partialUpdate

    private ScientificFieldService scientificFieldService;

    public ScientificFieldController(ScientificFieldService scientificFieldService) {
        this.scientificFieldService = scientificFieldService;
    }

    @PostMapping
    public ResponseEntity<ScientificFieldDto> save(@Valid @RequestBody ScientificFieldDto scientificFieldDto)  {
        return new ResponseEntity<>(scientificFieldService.save(scientificFieldDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScientificFieldDto>> getAll() {
        return new ResponseEntity<>(scientificFieldService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ScientificFieldDto findById(@PathVariable("id") Long id) {
        return scientificFieldService.findById(id);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ScientificFieldDto> partialUpdate(
            @PathVariable(name = "id") Long id,
            @RequestBody Map<String, String> updates) {

        ScientificFieldDto updatedScientificField = scientificFieldService.partialUpdate(id, updates);
        return ResponseEntity.ok(updatedScientificField);

    }
}
