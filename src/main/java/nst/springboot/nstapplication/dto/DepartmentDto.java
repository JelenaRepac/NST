package nst.springboot.nstapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nst.springboot.nstapplication.domain.HeadHistory;
import nst.springboot.nstapplication.domain.Member;
import nst.springboot.nstapplication.domain.SecretaryHistory;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto  {

    private Long id;

    private String name;

    private String shortName;
}