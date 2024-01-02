/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.converter.impl;

import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.converter.DtoEntityConverter;
import nst.springboot.nstapplication.dto.DepartmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author student2
 */

@Component
public class DepartmentConverter implements DtoEntityConverter<DepartmentDto, Department>{


    @Override
    public DepartmentDto toDto(Department entity) {
        return DepartmentDto.builder().
                id(entity.getId()).
                name(entity.getName()).
                shortName(entity.getShortName()).
                href("http://localhost:8080/department/"+entity.getId()).
                build();
    }

    @Override
    public Department toEntity(DepartmentDto dto) {
        return Department.builder().
                id(dto.getId()).
                name(dto.getName()).
                shortName(dto.getShortName()).
                build();
    }
}
