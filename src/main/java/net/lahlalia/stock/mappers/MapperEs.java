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
        return mapper.map(dto, EntreSortie.class);

    }
    public ESDto toModel(EntreSortie es){
         return mapper.map(es, ESDto.class);


    }

    

}
