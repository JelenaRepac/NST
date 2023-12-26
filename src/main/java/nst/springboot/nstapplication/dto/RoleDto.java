package nst.springboot.nstapplication.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto implements Serializable {

    Long id;

    String name;
}
