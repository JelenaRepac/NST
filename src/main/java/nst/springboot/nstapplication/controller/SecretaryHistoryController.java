package nst.springboot.nstapplication.controller;

import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.HeadHistoryDto;
import nst.springboot.nstapplication.dto.SecretaryHistoryDto;
import nst.springboot.nstapplication.service.SecretaryHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secretary")
@Transactional
public class SecretaryHistoryController {
    private SecretaryHistoryService service;

    public SecretaryHistoryController(SecretaryHistoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SecretaryHistoryDto> save(@Valid @RequestBody SecretaryHistoryDto secretaryHistory) throws Exception {
        return new ResponseEntity<>(service.save(secretaryHistory), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SecretaryHistoryDto>> getAll() {
        List<SecretaryHistoryDto> histories = service.getAll();
        return new ResponseEntity<>(histories, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public SecretaryHistoryDto findById(@PathVariable("id") Long id){
        return service.findById(id);
    }


//    @GetMapping("/department/{id}")
//    public SecretaryHistoryDto findByDepartmentId(@PathVariable("id") Long id)  {
//        return service.getByDepartmentId(id);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>("Secretary history removed!", HttpStatus.OK);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<SecretaryHistoryDto> updateSecretaryHistory(@PathVariable Long id, @RequestBody SecretaryHistoryDto secretaryHistoryDto) {
        SecretaryHistoryDto updatedSecretaryHistory = service.patchSecretaryHistory(id, secretaryHistoryDto);
        if (updatedSecretaryHistory != null) {
            return ResponseEntity.ok(updatedSecretaryHistory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
//    @GetMapping("/department/{id}/history")
//    public List<SecretaryHistoryDto> getHistoryForDepartmentId(@PathVariable("id") Long id)  {
//        return service.getHistoryForDepartmentId(id);
//    }
}