package net.lahlalia.stock.dtos;

import lombok.*;

@Getter
@Setter
@Builder // for building objects using .build
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long idProduit;
    private String name;
    private String status;
    private String type;
    private RegimeDto regime;


}
