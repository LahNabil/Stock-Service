package net.lahlalia.stock.mappers;

import net.lahlalia.stock.dtos.BacDto;
import net.lahlalia.stock.entities.Bac;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MapperBacTest {

    private MapperBac mapperBac;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        mapperBac = new MapperBac(modelMapper);
    }

    @Test
    public void testToEntity() {
        BacDto bacDto = BacDto.builder()
                .idBac("BAC123")
                .capacity(1000.0)
                .totalImpom(500.0)
                .status(true)
                .dateOuverture(new Date())
                .capacityUsed(200.0)
                .idProduct(1L)
                .idDepot("DEPOT123")
                .build();

        Bac bac = mapperBac.toEntity(bacDto);

        assertNotNull(bac);
        assertEquals(bacDto.getIdBac(), bac.getIdBac());
        assertEquals(bacDto.getCapacity(), bac.getCapacity());
        assertEquals(bacDto.getTotalImpom(), bac.getTotalImpom());
        assertEquals(bacDto.isStatus(), bac.isStatus());
        assertEquals(bacDto.getDateOuverture(), bac.getDateOuverture());
        assertEquals(bacDto.getCapacityUsed(), bac.getCapacityUsed());
        assertEquals(bacDto.getIdProduct(), bac.getIdProduct());
    }

    @Test
    public void testToModel() {
        Bac bac = Bac.builder()
                .idBac("BAC123")
                .capacity(1000.0)
                .totalImpom(500.0)
                .status(true)
                .dateOuverture(new Date())
                .capacityUsed(200.0)
                .idProduct(1L)
                .build();

        BacDto bacDto = mapperBac.toModel(bac);

        assertNotNull(bacDto);
        assertEquals(bac.getIdBac(), bacDto.getIdBac());
        assertEquals(bac.getCapacity(), bacDto.getCapacity());
        assertEquals(bac.getTotalImpom(), bacDto.getTotalImpom());
        assertEquals(bac.isStatus(), bacDto.isStatus());
        assertEquals(bac.getDateOuverture(), bacDto.getDateOuverture());
        assertEquals(bac.getCapacityUsed(), bacDto.getCapacityUsed());
        assertEquals(bac.getIdProduct(), bacDto.getIdProduct());
    }
    @Test
    public void shouldHandleNullValuesInBacDtoToBac() {
        BacDto givenBacDto = BacDto.builder()
                .idBac(null)
                .capacity(0)
                .totalImpom(0)
                .status(false)
                .dateOuverture(null)
                .capacityUsed(0)
                .idProduct(null)
                .idDepot(null)
                .build();

        Bac result = mapperBac.toEntity(givenBacDto);
        assertThat(result).isNotNull();
        assertThat(result.getIdBac()).isNull();
        assertThat(result.getCapacity()).isEqualTo(0);
        assertThat(result.getTotalImpom()).isEqualTo(0);
        assertThat(result.isStatus()).isFalse();
        assertThat(result.getDateOuverture()).isNull();
        assertThat(result.getCapacityUsed()).isEqualTo(0);
        assertThat(result.getIdProduct()).isNull();
        assertThat(result.getDepot()).isNull();
    }

    @Test
    public void shouldHandleNullValuesInBacToBacDto() {
        Bac givenBac = Bac.builder()
                .idBac(null)
                .capacity(0)
                .totalImpom(0)
                .status(false)
                .dateOuverture(null)
                .capacityUsed(0)
                .idProduct(null)
                .depot(null)
                .build();

        BacDto result = mapperBac.toModel(givenBac);
        assertThat(result).isNotNull();
        assertThat(result.getIdBac()).isNull();
        assertThat(result.getCapacity()).isEqualTo(0);
        assertThat(result.getTotalImpom()).isEqualTo(0);
        assertThat(result.isStatus()).isFalse();
        assertThat(result.getDateOuverture()).isNull();
        assertThat(result.getCapacityUsed()).isEqualTo(0);
        assertThat(result.getIdProduct()).isNull();
        assertThat(result.getIdDepot()).isNull();
    }
}
