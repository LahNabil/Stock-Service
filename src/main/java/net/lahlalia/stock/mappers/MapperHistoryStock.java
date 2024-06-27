package net.lahlalia.stock.mappers;

import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.ESDto;
import net.lahlalia.stock.dtos.HistoryDto;
import net.lahlalia.stock.entities.EntreSortie;
import net.lahlalia.stock.entities.HistoryStock;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperHistoryStock {

    private final ModelMapper mapper;

    public HistoryStock toEntity(HistoryDto dto){
        HistoryStock historyStock = mapper.map(dto, HistoryStock.class);
        return historyStock;
    }
    public HistoryDto toModel(HistoryStock historyStock){
        HistoryDto historyDto = mapper.map(historyStock, HistoryDto.class);
        return historyDto;

    }
}
