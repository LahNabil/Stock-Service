package net.lahlalia.stock.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.BacDto;
import net.lahlalia.stock.dtos.DepotDTO;
import net.lahlalia.stock.dtos.StockProduitDto;
import net.lahlalia.stock.services.DepotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;

@RestController
@RequestMapping("/api/depot")
@RequiredArgsConstructor
public class DepotController {

    private final DepotService depotService;

    @PostMapping(value = "/",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepotDTO>saveDepot(@RequestBody DepotDTO dto){
        DepotDTO savedDepot = depotService.saveDepot(dto);
        return new ResponseEntity<>(savedDepot, HttpStatus.CREATED);

    }
    @GetMapping(value = "/bacs/{nameProduct}/{zoneDepot}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BacDto>> getBacsByProductAndZone(@PathVariable String nameProduct, @PathVariable String zoneDepot) {
        List<BacDto> bacs = depotService.findBacsByProductAndZone(nameProduct, zoneDepot);
        return ResponseEntity.ok(bacs);
    }


    @GetMapping(value = "/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DepotDTO>> getAllDepot(){
        List<DepotDTO> depots = depotService.geAllDepots();
        return ResponseEntity.ok(depots);
    }
    @GetMapping(value = "/{idDepot}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepotDTO> getDepotById(@PathVariable String idDepot){
        DepotDTO depotDTO = depotService.getDepotById(idDepot);
        return ResponseEntity.ok(depotDTO);
    }
    @PutMapping(value = "/{idDepot}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepotDTO>updateDepot(@PathVariable String idDepot, @RequestBody DepotDTO dto) throws EntityNotFoundException{
        DepotDTO updatedDepot = depotService.editDepot(idDepot,dto);
        return ResponseEntity.ok(updatedDepot);
    }
    @GetMapping("/calculerStocksProduits/{idDepot}")
    public ResponseEntity<List<StockProduitDto>> calculerStocksProduits(@PathVariable String idDepot) {
        try {
            DepotDTO depotDTO = depotService.getDepotById(idDepot);
            List<StockProduitDto> stocksProduits = depotService.calculerStocksProduitsDansDepot(depotDTO);
            return ResponseEntity.ok(stocksProduits);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/{idDepot}")
    public ResponseEntity<Void>deleteDepotById(@PathVariable String idDepot){
        Boolean deletedDepot = depotService.deleteDepotById(idDepot);
        return deletedDepot ? ResponseEntity.noContent().build() :ResponseEntity.notFound().build();

    }

    @GetMapping("/calculerStock/{idDepot}")
    public ResponseEntity<Double> calculerStock(@PathVariable String idDepot){
        try{
            double stock = depotService.CalculerStockDepotAllProducts(idDepot);
            return ResponseEntity.ok(stock);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/capacitydepot/{idDepot}")
    public ResponseEntity<Double> calculerCapacityDepot(@PathVariable String idDepot){
        double capacity = depotService.CalculerCapacityDepot(idDepot);
        return ResponseEntity.ok(capacity);
    }
    @GetMapping("/capacities")
    public ResponseEntity<List<Double>> calculerCapacities(){
        List<Double> capacities = depotService.CalculerCapacites();
        return ResponseEntity.ok(capacities);
    }
    @GetMapping("/stocks")
    public ResponseEntity<List<Double>> calculerStock(){
        List<Double> stockList = depotService.calculerStockDepot();
        return ResponseEntity.ok(stockList);
    }
    @GetMapping("/calculerStockProduitDepotDate/{idDepot}/{nameProduct}/{year}/{month}")
    public ResponseEntity<Double>CalculerStockProduitDepotDate(@PathVariable String idDepot,@PathVariable String nameProduct,@PathVariable int year,@PathVariable int month){
        double stock = depotService.CalculerStockProduitDepotYearMonth(idDepot,nameProduct,year,month);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/calculerStockProduit/{idDepot}/{nameProduct}")
    public ResponseEntity<Double>CalculerStockProduitDepot(@PathVariable String idDepot,@PathVariable String nameProduct){
        double stock = depotService.CalculerStockProduitDepot(idDepot,nameProduct);
        return ResponseEntity.ok(stock);
    }
    @GetMapping("/getcitydepot/{idDepot}")
    public ResponseEntity<String> getCityDepot(@PathVariable String idDepot){
        String city = depotService.getCity(idDepot);
        return ResponseEntity.ok(city);
    }
}
