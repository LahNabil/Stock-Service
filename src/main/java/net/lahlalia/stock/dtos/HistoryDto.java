package net.lahlalia.stock.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import net.lahlalia.stock.entities.Depot;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryDto {

    private Long idHistory;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dateJour;
    private String nameProduct;
    private double stock;
    private String idDepot;
}
