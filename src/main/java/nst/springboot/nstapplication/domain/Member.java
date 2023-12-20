package nst.springboot.nstapplication.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class Member {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastname;

    @ManyToOne
    private AcademicTitle academicTitle;

    @ManyToOne
    private EducationTitle educationTitle;

    @ManyToOne
    private ScientificField scientificField;

    @ManyToOne
    private Department department;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<AcademicTitleHistory> academicTitleHistoryList;
}
