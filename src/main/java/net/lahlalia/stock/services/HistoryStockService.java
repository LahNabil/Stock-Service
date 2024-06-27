package net.lahlalia.stock.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.stock.dtos.HistoryDto;
import net.lahlalia.stock.entities.HistoryStock;
import net.lahlalia.stock.mappers.MapperHistoryStock;
import net.lahlalia.stock.repositories.HistoryStockRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryStockService {

    private final HistoryStockRepository historyStockRepository;
    private final MapperHistoryStock historyStockMapper;

    public List<HistoryDto> getAllHistoryDto() {
        List<HistoryStock> histories = historyStockRepository.findAll();
        List<HistoryDto> historyDtos = new ArrayList<>();
        for(HistoryStock historyStock : histories){
            HistoryDto historyDto = historyStockMapper.toModel(historyStock);
            try {
                historyDto.setIdDepot(historyStock.getDepot().getIdDepot());
            } catch (EntityNotFoundException e) {
                log.error("Depot not found for HistoryStock "+ historyStock.getIdHistory());
            }
            historyDtos.add(historyDto);
        }
        return historyDtos;

    }
}
