package net.lahlalia.stock.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import net.lahlalia.stock.entities.Bac;
import net.lahlalia.stock.enums.Area;

import java.util.List;

@Getter
@Setter
@Builder // for building objects using .build
@AllArgsConstructor
@NoArgsConstructor
public class DepotDTO {

    private String idDepot;
    private String nameDepot;
    private String zone;
    private Area area;
    private List<BacDto> bacDtos;


}
