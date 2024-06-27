package net.lahlalia.stock.mappers;

import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.BacDto;
import net.lahlalia.stock.dtos.ESDto;
import net.lahlalia.stock.entities.Bac;
import net.lahlalia.stock.entities.EntreSortie;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MapperBac {

    private final ModelMapper mapper;

    public Bac toEntity(BacDto dto){
        Bac bac = mapper.map(dto, Bac.class);
        return bac;
    }
    public BacDto toModel(Bac bac){
        BacDto bacDto = mapper.map(bac, BacDto.class);
        return bacDto;

    }
}
