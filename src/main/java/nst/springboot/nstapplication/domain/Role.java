package nst.springboot.nstapplication.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty(message = "Ime je obavezno polje")
    @Size(min = 2, max = 20, message = "Broj znakova je od 2 do 20")
    @Column(name = "name")
    String name;
}
