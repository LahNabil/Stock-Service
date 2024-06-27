package net.lahlalia.stock.mappers;

import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.DepotDTO;
import net.lahlalia.stock.entities.Depot;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MapperDepot {

    private final ModelMapper mapper;

    public Depot toEntity(DepotDTO dto){
        return mapper.map(dto, Depot.class);
    }
    public DepotDTO toModel(Depot depot){
        return mapper.map(depot, DepotDTO.class);


    }
}
