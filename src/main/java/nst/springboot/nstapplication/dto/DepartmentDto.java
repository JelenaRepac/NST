package nst.springboot.nstapplication.dto;

import lombok.*;
import nst.springboot.nstapplication.domain.HeadHistory;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.domain.SecretaryHistory;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDto  {

    private Long id;

    private String name;

    private String shortName;
}