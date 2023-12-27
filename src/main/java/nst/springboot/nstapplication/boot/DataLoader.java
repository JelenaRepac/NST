package nst.springboot.nstapplication.boot;

import nst.springboot.nstapplication.domain.*;
import nst.springboot.nstapplication.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AcademicTitleRepository academicTitleRepository;
    private final EducationTitleRepository educationTitleRepository;
    private final ScientificFieldRepository scientificFieldRepository;

    private final DepartmentRepository departmentRepository;

    public DataLoader(RoleRepository roleRepository, AcademicTitleRepository academicTitleRepository, EducationTitleRepository educationTitleRepository, ScientificFieldRepository scientificFieldRepository, DepartmentRepository departmentRepository) {
        this.roleRepository = roleRepository;
        this.academicTitleRepository = academicTitleRepository;
        this.educationTitleRepository = educationTitleRepository;
        this.scientificFieldRepository = scientificFieldRepository;
        this.departmentRepository = departmentRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.findAll().isEmpty()){
            roleRepository.saveAll(List.of(
                    Role.builder().name("Secretary").build(),
                    Role.builder().name("Head").build(),
                    Role.builder().name("Default").build()));
        }
        academicTitleRepository.save(AcademicTitle.builder().name("Assistant").build());
        educationTitleRepository.save(EducationTitle.builder().name("Master engineer").build());
        scientificFieldRepository.save(ScientificField.builder().name("Software engineering").build());

        departmentRepository.save(Department.builder().name("Laboratorija za softversko inženjerstvo").shortName("SILAB").build());



    }
}
