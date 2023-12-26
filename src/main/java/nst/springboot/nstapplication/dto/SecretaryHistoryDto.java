package nst.springboot.nstapplication.dto;

import jakarta.persistence.*;
import lombok.*;
import nst.springboot.nstapplication.domain.Department;
import nst.springboot.nstapplication.domain.Member;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecretaryHistoryDto implements Serializable {

    private Long id;

    private DepartmentDto department;

    private MemberDto member;

    private LocalDate startDate;

    private LocalDate endDate;
}
