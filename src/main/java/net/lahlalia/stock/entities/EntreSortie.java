package net.lahlalia.stock.entities;

import jakarta.persistence.*;
import lombok.*;
import net.lahlalia.stock.enums.Business;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntreSortie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double quantite;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private Date date;
    private Boolean typeES;
    @Enumerated(EnumType.STRING)
    private Business business;
    @ManyToOne
    private Bac bac;


}
