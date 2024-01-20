package nst.springboot.nstapplication.controller;


import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import nst.springboot.nstapplication.dto.RoleDto;
import nst.springboot.nstapplication.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
@Hidden
public class RoleController {

    //Controller for roles
    //ENDPOINTS -> saving, getAll, getById, partialUpdate
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDto> save(@Valid @RequestBody RoleDto roleDto)  {
        return new ResponseEntity<>(roleService.save(roleDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAll() {
        return new ResponseEntity<>(roleService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public RoleDto findById(@PathVariable("id") Long id) throws Exception {
        return roleService.findById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoleDto> partialUpdate(
            @PathVariable(name = "id") Long id,
            @RequestBody Map<String, String> updates) {

        RoleDto updatedRole = roleService.partialUpdate(id, updates);
        return ResponseEntity.ok(updatedRole);

    }
}