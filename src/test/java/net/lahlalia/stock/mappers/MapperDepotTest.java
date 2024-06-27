package net.lahlalia.stock.mappers;

import net.lahlalia.stock.dtos.DepotDTO;
import net.lahlalia.stock.entities.Depot;
import net.lahlalia.stock.enums.Area;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MapperDepotTest {

    private MapperDepot mapperDepot;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        mapperDepot = new MapperDepot(modelMapper);
    }

    @Test
    public void testToEntity() {
        DepotDTO depotDTO = DepotDTO.builder()
                .idDepot("DEPOT123")
                .nameDepot("Warehouse A")
                .zone("Zone A")
                .area(Area.NORTH)
                .bacDtos(Collections.emptyList())
                .build();

        Depot depot = mapperDepot.toEntity(depotDTO);

        assertNotNull(depot);
        assertEquals(depotDTO.getIdDepot(), depot.getIdDepot());
        assertEquals(depotDTO.getNameDepot(), depot.getNameDepot());
        assertEquals(depotDTO.getZone(), depot.getZone());
        assertEquals(depotDTO.getArea(), depot.getArea());
        assertThat(depot.getBacs()).isEmpty(); // Assuming you're mapping List<BacDto> to List<Bac> correctly
    }

    @Test
    public void testToModel() {
        Depot depot = new Depot();
        depot.setIdDepot("DEPOT123");
        depot.setNameDepot("Warehouse A");
        depot.setZone("Zone A");
        depot.setArea(Area.NORTH);

        DepotDTO depotDTO = mapperDepot.toModel(depot);

        assertNotNull(depotDTO);
        assertEquals(depot.getIdDepot(), depotDTO.getIdDepot());
        assertEquals(depot.getNameDepot(), depotDTO.getNameDepot());
        assertEquals(depot.getZone(), depotDTO.getZone());
        assertEquals(depot.getArea(), depotDTO.getArea());
    }
}
