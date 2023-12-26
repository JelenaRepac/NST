package nst.springboot.nstapplication.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcademicTitleHistoryDto implements Serializable {
    private Long id;

    private MemberDto member;

    private LocalDate startDate;

    private LocalDate endDate;

    private AcademicTitleDto academicTitle;

    private ScientificFieldDto scientificField;
}
