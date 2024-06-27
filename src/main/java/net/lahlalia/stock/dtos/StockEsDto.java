package net.lahlalia.stock.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Builder // for building objects using .build
@AllArgsConstructor
@NoArgsConstructor
public class StockEsDto {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dateJour;
    private double stockInitial;
    private double entre;
    private double sortie;
    private double stockFinale;

}
