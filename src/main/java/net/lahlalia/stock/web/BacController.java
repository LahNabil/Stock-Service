package net.lahlalia.stock.web;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.BacDto;
import net.lahlalia.stock.entities.EntreSortie;
import net.lahlalia.stock.services.BacService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bac")
@RequiredArgsConstructor
public class BacController {
    private final BacService bacService;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BacDto>> getAllBac(){
        List<BacDto> bacs = bacService.getAllBacs();
        return ResponseEntity.ok(bacs);
    }
    @GetMapping(value = "/bydepotid/{depotId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BacDto>> getBacByDepotId(@PathVariable String depotId){
        List<BacDto> bacs = bacService.getAllBacsForDepot(depotId);
        if (bacs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(bacs, HttpStatus.OK);
        }


    }
    @GetMapping(value = "/product/{nameProduct}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BacDto>> getBacsByProductName(@PathVariable String nameProduct){
        List<BacDto> bacDtos = bacService.getBacsByNameProduct(nameProduct);
        return ResponseEntity.ok(bacDtos);
    }
    @GetMapping(value = "/{idBac}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BacDto> getBacById(@PathVariable String idBac)throws EntityNotFoundException {
        BacDto dto = bacService.getBacById(idBac);
        return ResponseEntity.ok(dto);
    }
    @PostMapping(value = "/",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BacDto> saveProduct(@RequestBody BacDto dto){
        BacDto savedBac = bacService.saveBac(dto);
        return new ResponseEntity<>(savedBac, HttpStatus.CREATED);

    }
    @DeleteMapping("/{idBac}")
    public ResponseEntity<Void> deleteBacById(@PathVariable String idBac){
        Boolean deletedBac = bacService.deleteBacById(idBac);
        return deletedBac ? ResponseEntity.noContent().build() :ResponseEntity.notFound().build();

    }
    @PutMapping(value = "/{idBac}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BacDto>updateBac(@PathVariable String idBac,@RequestBody BacDto dto) throws EntityNotFoundException{
        BacDto updatedBac = bacService.updateBac(idBac,dto);
        return ResponseEntity.ok(updatedBac);
    }
    @GetMapping(value = "/creux/{idBac}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> calculerCreux(@PathVariable String idBac){
        try {
            double creux = bacService.calculerCreux(idBac);
            return ResponseEntity.ok(creux);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
    @CircuitBreaker(name="getPrductInstance", fallbackMethod = "getDefaultProduct")
    @GetMapping(value = "/name/{idProduit}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProductNameById(@PathVariable Long idProduit){
        String name = bacService.getProductNameById(idProduit);
        return ResponseEntity.ok(name);

    }
    public ResponseEntity<String> getDefaultProduct(Long idProduit, Throwable throwable) {
        return ResponseEntity.ok("Unknown Product");
    }


    @PostMapping("/stock/{idBac}")
    public ResponseEntity<BacDto> ESrProduit(@RequestBody EntreSortie es, @PathVariable String idBac){
        try{
            BacDto savedBacDto = bacService.ESrProduit(es,idBac);
            if (savedBacDto != null) {
                return ResponseEntity.ok(savedBacDto);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }catch (EntityNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

}
