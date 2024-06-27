package net.lahlalia.stock.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockProduitDto {
    private String nameProduit;
    private Double quantite;
    private double stockSecurite;
}
