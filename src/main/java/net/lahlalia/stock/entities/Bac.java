package net.lahlalia.stock.entities;

import jakarta.persistence.*;
import lombok.*;
import net.lahlalia.stock.dtos.Product;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bac {
    @Id
    private String idBac;
    private double capacity;
    private double totalImpom;
    private boolean status;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dateOuverture;
    private double capacityUsed;
    private Long idProduct;

    @OneToMany(mappedBy = "bac")
    private List<EntreSortie> entre_sortie;

    @ManyToOne
    private Depot depot;



}
