package net.lahlalia.stock.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder // for building objects using .build
@AllArgsConstructor
@NoArgsConstructor
public class BacDto {

    private String idBac;
    private double capacity;
    private double totalImpom;
    private boolean status;
    private Date dateOuverture;
    private double capacityUsed;
    private Long idProduct;
    private String idDepot;


}
