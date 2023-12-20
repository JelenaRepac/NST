/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package nst.springboot.nstapplication.service;

import java.util.List;
import nst.springboot.nstapplication.dto.SubjectDto;

/**
 *
 * @author student2
 */
public interface SubjectService {
    SubjectDto save(SubjectDto subjectDto)throws Exception;
    List<SubjectDto> getAll();
    void delete(Long id) throws Exception;
    void update(SubjectDto subjectDto)throws Exception;
    SubjectDto findById(Long id)throws Exception;
}
