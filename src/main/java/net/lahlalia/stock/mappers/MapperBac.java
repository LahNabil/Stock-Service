package net.lahlalia.stock.mappers;

import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.BacDto;
import net.lahlalia.stock.entities.Bac;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MapperBac {

    private final ModelMapper mapper;

    public Bac toEntity(BacDto dto){
        return mapper.map(dto, Bac.class);

    }
    public BacDto toModel(Bac bac){
        return mapper.map(bac, BacDto.class);


    }
}
