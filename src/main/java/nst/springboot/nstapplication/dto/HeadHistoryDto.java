package nst.springboot.nstapplication.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadHistoryDto implements Serializable {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private MemberDto head;
    private DepartmentDto department;
}
