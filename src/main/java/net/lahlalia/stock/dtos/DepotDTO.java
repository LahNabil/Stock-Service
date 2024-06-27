package net.lahlalia.stock.dtos;


import lombok.*;
import net.lahlalia.stock.enums.Area;

import java.util.List;

@Getter
@Setter
@Builder 
@AllArgsConstructor
@NoArgsConstructor
public class DepotDTO {

    private String idDepot;
    private String nameDepot;
    private String zone;
    private Area area;
    private List<BacDto> bacDtos;


}
