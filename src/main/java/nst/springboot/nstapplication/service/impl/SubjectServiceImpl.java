/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nst.springboot.nstapplication.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nst.springboot.nstapplication.domain.Subject;
import nst.springboot.nstapplication.repository.DepartmentRepository;
import nst.springboot.nstapplication.repository.SubjectRepository;
import nst.springboot.nstapplication.service.SubjectService;
import nst.springboot.nstapplication.converter.impl.DepartmentConverter;
import nst.springboot.nstapplication.converter.impl.SubjectConverter;
import nst.springboot.nstapplication.dto.SubjectDto;
import org.springframework.stereotype.Service;

/**
 *
 * @author student2
 */
@Service
public class SubjectServiceImpl implements SubjectService {

    private DepartmentConverter departmentConverter;
    private SubjectConverter subjectConverter;

    private SubjectRepository subjectRepository;
    private DepartmentRepository departmentRepository;

    public SubjectServiceImpl(
            SubjectRepository subjectRepository,
            DepartmentRepository departmentRepository,
            DepartmentConverter departmentConverter, SubjectConverter subjectConverter) {
        this.departmentRepository = departmentRepository;
        this.subjectRepository = subjectRepository;
        this.departmentConverter = departmentConverter;
        this.subjectConverter = subjectConverter;
    }

    @Override
    public SubjectDto save(SubjectDto subjectDto) throws Exception {
          throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<SubjectDto> getAll() {
        return subjectRepository
                .findAll()
                .stream().map(entity -> subjectConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) throws Exception {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            Subject subj = subject.get();
            subjectRepository.delete(subj);
        } else {
            throw new Exception("Subject does not exist!");
        }

    }

    @Override
    public void update(SubjectDto subjectDto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SubjectDto findById(Long id) throws Exception {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            //postoji
            Subject subj = subject.get();
            return subjectConverter.toDto(subj);
        } else {
            throw new Exception("Subject does not exist!");
        }
    }

}
