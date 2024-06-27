package net.lahlalia.stock.web;

import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.ESDto;
import net.lahlalia.stock.dtos.StockEsDto;
import net.lahlalia.stock.services.EsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/es")
@RequiredArgsConstructor
public class ESController {
    private final EsService esService;

    @GetMapping(value = "/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ESDto>> getAllES(){
        List<ESDto> esDtoList = esService.getAllES();
        return ResponseEntity.ok(esDtoList);

    }

    @GetMapping(value = "/{idDepot}/{nameProduct}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ESDto>> getSortiesParProduitDepot(@PathVariable String idDepot,@PathVariable String nameProduct){
        List<ESDto> esDtoList = esService.getSortiesParProduitDepot(idDepot,nameProduct);
        return ResponseEntity.ok(esDtoList);
    }
//    @GetMapping("/{idDepot}/{nameProduct}/{year}/{month}")
//    public ResponseEntity<List<StockEsDto>>createStockEsDto(@PathVariable String idDepot,
//                                                            @PathVariable String nameProduct,
//                                                            @PathVariable int year,
//                                                            @PathVariable int month){
//        List<StockEsDto> stockEsDtoList = esService.createStockEsDto(idDepot,nameProduct,year,month);
//        return ResponseEntity.ok(stockEsDtoList);
//    }
    @GetMapping("/{idDepot}/{nameProduct}/{year}/{month}")
    public ResponseEntity<List<StockEsDto>>generateMonthlyStockReport(@PathVariable String idDepot,
                                                            @PathVariable String nameProduct,
                                                            @PathVariable int year,
                                                            @PathVariable int month){
        List<StockEsDto> stockEsDtoList = esService.generateMonthlyStockReport(idDepot,nameProduct,year,month);
        return ResponseEntity.ok(stockEsDtoList);
    }


}
