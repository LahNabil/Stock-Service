package net.lahlalia.stock.mappers;

import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.HistoryDto;
import net.lahlalia.stock.entities.HistoryStock;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperHistoryStock {

    private final ModelMapper mapper;

    public HistoryStock toEntity(HistoryDto dto){
        return mapper.map(dto, HistoryStock.class);

    }
    public HistoryDto toModel(HistoryStock historyStock){
        return mapper.map(historyStock, HistoryDto.class);
    }
}
