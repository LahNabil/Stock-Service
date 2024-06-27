package net.lahlalia.stock.dtos;


import lombok.*;
import net.lahlalia.stock.enums.Business;

import java.util.Date;

@Getter
@Setter
@Builder
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
