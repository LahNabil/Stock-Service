package net.lahlalia.stock.dtos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import net.lahlalia.stock.entities.Bac;
import net.lahlalia.stock.enums.Business;

import java.util.Date;

@Getter
@Setter
@Builder // for building objects using .build
@AllArgsConstructor
@NoArgsConstructor
public class ESDto {

    private Long id;
    private double quantite;
    private Date date;
    private Boolean typeES;
    private Business business;
    private String  idBac;
    private String nameProduct;
}
