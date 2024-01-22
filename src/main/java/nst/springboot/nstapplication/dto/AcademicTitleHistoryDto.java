package nst.springboot.nstapplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private LocalDate startDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate endDate;
    private AcademicTitleDto academicTitle;
    private ScientificFieldDto scientificField;
    private MemberDto member;
}
