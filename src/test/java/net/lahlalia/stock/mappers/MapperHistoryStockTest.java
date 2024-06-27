package net.lahlalia.stock.mappers;

import net.lahlalia.stock.dtos.HistoryDto;
import net.lahlalia.stock.entities.Depot;
import net.lahlalia.stock.entities.HistoryStock;
import net.lahlalia.stock.enums.Area;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MapperHistoryStockTest {

    private MapperHistoryStock mapperHistoryStock;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        mapperHistoryStock = new MapperHistoryStock(modelMapper);
    }

    @Test
    public void testToEntity() {
        HistoryDto historyDto = HistoryDto.builder()
                .idHistory(1L)
                .dateJour(new Date())
                .nameProduct("Product A")
                .stock(500.0)
                .idDepot("DEPOT123")
                .build();

        HistoryStock historyStock = mapperHistoryStock.toEntity(historyDto);

        assertNotNull(historyStock);
        assertEquals(historyDto.getIdHistory(), historyStock.getIdHistory());
        assertEquals(historyDto.getDateJour(), historyStock.getDateJour());
        assertEquals(historyDto.getNameProduct(), historyStock.getNameProduct());
        assertEquals(historyDto.getStock(), historyStock.getStock());
        assertEquals(historyDto.getIdDepot(), historyStock.getDepot().getIdDepot());
    }

    @Test
    public void testToModel() {
        HistoryStock historyStock = HistoryStock.builder()
                .idHistory(1L)
                .dateJour(new Date())
                .nameProduct("Product A")
                .stock(500.0)
                .depot(Depot.builder().idDepot("DEPOT123").nameDepot("Depot A").zone("Zone A").area(Area.NORTH).build())
                .build();

        HistoryDto historyDto = mapperHistoryStock.toModel(historyStock);

        assertNotNull(historyDto);
        assertEquals(historyStock.getIdHistory(), historyDto.getIdHistory());
        assertEquals(historyStock.getDateJour(), historyDto.getDateJour());
        assertEquals(historyStock.getNameProduct(), historyDto.getNameProduct());
        assertEquals(historyStock.getStock(), historyDto.getStock());
        assertEquals(historyStock.getDepot().getIdDepot(), historyDto.getIdDepot());
    }
}
