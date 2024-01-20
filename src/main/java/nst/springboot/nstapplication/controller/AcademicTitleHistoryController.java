package nst.springboot.nstapplication.controller;
import jakarta.validation.Valid;
import nst.springboot.nstapplication.domain.AcademicTitleHistory;
import nst.springboot.nstapplication.dto.AcademicTitleDto;
import nst.springboot.nstapplication.dto.AcademicTitleHistoryDto;
import nst.springboot.nstapplication.dto.DepartmentDto;
import nst.springboot.nstapplication.service.AcademicTitleHistoryService;
import nst.springboot.nstapplication.service.AcademicTitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/academicTitleHistory")
public class AcademicTitleHistoryController {
    //Controller for academic title
    //ENDPOINTS -> saving, getAll, getById, partialUpdate
    private AcademicTitleHistoryService academicTitleService;

    public AcademicTitleHistoryController(AcademicTitleHistoryService academicTitleService) {
        this.academicTitleService = academicTitleService;
    }

    @PostMapping
    public ResponseEntity<AcademicTitleHistoryDto> save(@Valid @RequestBody AcademicTitleHistoryDto academicTitleDTO)  {
        AcademicTitleHistoryDto titleDTO = academicTitleService.save(academicTitleDTO);
        return new ResponseEntity<>(titleDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AcademicTitleHistoryDto>> getAll() {
        List<AcademicTitleHistoryDto> titles = academicTitleService.getAll();
        return new ResponseEntity<>(titles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public AcademicTitleHistoryDto findById(@PathVariable("id") Long id) throws Exception {
        return academicTitleService.findById(id);
    }

    @GetMapping("/member/{id}")
    public List<AcademicTitleHistoryDto> findByMemberId(@PathVariable("id") Long id) {
        return academicTitleService.findByMemberId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicTitleHistoryDto> update(@PathVariable(name = "id") Long id, @Valid @RequestBody AcademicTitleHistoryDto academicTitleHistoryDto) throws Exception {
        return new ResponseEntity<>(academicTitleService.update(id, academicTitleHistoryDto), HttpStatus.OK);
    }
}
