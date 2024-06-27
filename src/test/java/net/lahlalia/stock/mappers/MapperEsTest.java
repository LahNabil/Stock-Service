package net.lahlalia.stock.mappers;

import net.lahlalia.stock.dtos.ESDto;
import net.lahlalia.stock.entities.Bac;
import net.lahlalia.stock.entities.EntreSortie;
import net.lahlalia.stock.enums.Business;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MapperEsTest {

    private MapperEs mapperEs;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        mapperEs = new MapperEs(modelMapper);
    }

    @Test
    public void testToEntity() {
        ESDto esDto = ESDto.builder()
                .id(1L)
                .quantite(100.0)
                .date(new Date())
                .typeES(true)
                .business(Business.RETAIL)
                .idBac("BAC123")
                .nameProduct("Product A")
                .build();

        EntreSortie es = mapperEs.toEntity(esDto);

        assertNotNull(es);
        assertEquals(esDto.getId(), es.getId());
        assertEquals(esDto.getQuantite(), es.getQuantite());
        assertEquals(esDto.getDate(), es.getDate());
        assertEquals(esDto.getTypeES(), es.getTypeES());
        assertEquals(esDto.getBusiness(), es.getBusiness());
        assertEquals(esDto.getIdBac(), es.getBac().getIdBac());
    }

    @Test
    public void testToModel() {
        EntreSortie es = EntreSortie.builder()
                .id(1L)
                .quantite(100.0)
                .date(new Date())
                .typeES(true)
                .business(Business.RETAIL)
                .bac(Bac.builder().idBac("BAC123").build())
                .build();

        ESDto esDto = mapperEs.toModel(es);

        assertNotNull(esDto);
        assertEquals(es.getId(), esDto.getId());
        assertEquals(es.getQuantite(), esDto.getQuantite());
        assertEquals(es.getDate(), esDto.getDate());
        assertEquals(es.getTypeES(), esDto.getTypeES());
        assertEquals(es.getBusiness(), esDto.getBusiness());
        assertEquals(es.getBac().getIdBac(), esDto.getIdBac());
    }
}
