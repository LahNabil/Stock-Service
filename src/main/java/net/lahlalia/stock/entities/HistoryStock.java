package net.lahlalia.stock.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistory;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dateJour;
    private double stock;
    private String nameProduct;
    @ManyToOne
    private Depot depot;




}
