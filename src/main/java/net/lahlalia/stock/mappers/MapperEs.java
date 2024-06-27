package net.lahlalia.stock.mappers;

import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.ESDto;
import net.lahlalia.stock.entities.EntreSortie;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MapperEs {

    private final ModelMapper mapper;

    public EntreSortie toEntity(ESDto dto){
        EntreSortie es = mapper.map(dto, EntreSortie.class);
        return es;
    }
    public ESDto toModel(EntreSortie es){
        ESDto esDto = mapper.map(es, ESDto.class);
        return esDto;

    }

    

}
