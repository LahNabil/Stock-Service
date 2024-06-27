package net.lahlalia.stock.services;

import net.lahlalia.stock.dtos.BacDto;
import net.lahlalia.stock.dtos.DepotDTO;
import net.lahlalia.stock.entities.Depot;
import net.lahlalia.stock.entities.HistoryStock;
import net.lahlalia.stock.exceptions.DepotNotFoundException;
import net.lahlalia.stock.mappers.MapperDepot;
import net.lahlalia.stock.mappers.MapperHistoryStock;
import net.lahlalia.stock.repositories.DepotRepository;
import net.lahlalia.stock.repositories.HistoryStockRepository;
import net.lahlalia.stock.restClients.ProductRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DepotServiceTest {

    @Mock
    private DepotRepository depotRepository;

    @Mock
    private MapperDepot depotMapper;

    @Mock
    private BacService bacService;

    @Mock
    private HistoryStockRepository historyStockRepository;

    @Mock
    private MapperHistoryStock historyStockMapper;

    @InjectMocks
    private DepotService depotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveDepot() {
        // Given
        DepotDTO depotDTO = new DepotDTO("DEPOT123", "Depot A", "Zone A", null, null);

        Depot depotEntity = new Depot();
        depotEntity.setIdDepot("DEPOT123");
        depotEntity.setNameDepot("Depot A");
        depotEntity.setZone("Zone A");

        when(depotMapper.toEntity(depotDTO)).thenReturn(depotEntity);
        when(depotRepository.save(depotEntity)).thenReturn(depotEntity);
        when(depotMapper.toModel(depotEntity)).thenReturn(depotDTO);

        // When
        DepotDTO savedDepotDTO = depotService.saveDepot(depotDTO);

        // Then
        assertNotNull(savedDepotDTO);
        assertEquals("DEPOT123", savedDepotDTO.getIdDepot());
        assertEquals("Depot A", savedDepotDTO.getNameDepot());
        assertEquals("Zone A", savedDepotDTO.getZone());
    }

    @Test
    public void testFindBacsByProductAndZone() {
        // Given
        String productName = "Product A";
        String zoneDepot = "Zone A";

        DepotDTO depotDTO = new DepotDTO();
        depotDTO.setIdDepot("DEPOT123");
        depotDTO.setZone("Zone A");

        List<BacDto> bacs = new ArrayList<>();
        bacs.add(new BacDto("BAC1", 100.0, 50.0, true, new Date(), 20.0, 1L, "DEPOT123"));
        depotDTO.setBacDtos(bacs);

        List<DepotDTO> allDepots = new ArrayList<>();
        allDepots.add(depotDTO);

        when(depotService.geAllDepots()).thenReturn(allDepots);
        when(bacService.getProductNameById(1L)).thenReturn("Product A");

        // When
        List<BacDto> result = depotService.findBacsByProductAndZone(productName, zoneDepot);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BAC1", result.get(0).getIdBac());
        assertEquals("DEPOT123", result.get(0).getIdDepot());
    }

    @Test
    public void testEditDepot() {
        // Given
        String idDepot = "DEPOT123";
        DepotDTO depotDTO = new DepotDTO("DEPOT123", "Updated Depot Name", "Updated Zone", null, null);

        Depot existingDepot = new Depot();
        existingDepot.setIdDepot("DEPOT123");
        existingDepot.setNameDepot("Old Depot Name");
        existingDepot.setZone("Old Zone");

        when(depotRepository.findById(idDepot)).thenReturn(java.util.Optional.of(existingDepot));
        when(depotRepository.save(existingDepot)).thenReturn(existingDepot);
        when(depotMapper.toModel(existingDepot)).thenReturn(depotDTO);

        // When
        DepotDTO updatedDepotDTO = depotService.editDepot(idDepot, depotDTO);

        // Then
        assertNotNull(updatedDepotDTO);
        assertEquals("DEPOT123", updatedDepotDTO.getIdDepot());
        assertEquals("Updated Depot Name", updatedDepotDTO.getNameDepot());
        assertEquals("Updated Zone", updatedDepotDTO.getZone());
    }

    // Add tests for other methods such as CalculerStockProduitDepot, CalculerStockDepotAllProducts, etc.

    // Example of a test method for CalculerStockSecurite
    @Test
    public void testCalculerStockSecurite() {
        // Given
        double quantity = 500.0;

        // When
        double stockSecurite = depotService.calculerStockSecurite(quantity);

        // Then
        assertEquals(1130.0, stockSecurite);
    }

    // Example of a test method for saveDailyStock
    @Test
    public void testSaveDailyStock() {
        // Given
        List<DepotDTO> depotDTOs = new ArrayList<>();
        depotDTOs.add(new DepotDTO("DEPOT123", "Depot A", "Zone A", null, null));

        when(depotService.geAllDepots()).thenReturn(depotDTOs);
        when(historyStockMapper.toEntity(any())).thenReturn(new HistoryStock());

        // When
        depotService.saveDailyStock();

        // Then
        // Verify that save method is called on historyStockRepository with correct parameters
        verify(historyStockRepository, times(1)).save(any());
    }
}
