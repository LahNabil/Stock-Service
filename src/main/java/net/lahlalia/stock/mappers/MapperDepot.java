package net.lahlalia.stock.mappers;

import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.BacDto;
import net.lahlalia.stock.dtos.DepotDTO;
import net.lahlalia.stock.entities.Bac;
import net.lahlalia.stock.entities.Depot;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MapperDepot {

    private final ModelMapper mapper;

    public Depot toEntity(DepotDTO dto){
        Depot depot = mapper.map(dto, Depot.class);
        return depot;
    }
    public DepotDTO toModel(Depot depot){
        DepotDTO depotDTO = mapper.map(depot, DepotDTO.class);
        return depotDTO;

    }
}
