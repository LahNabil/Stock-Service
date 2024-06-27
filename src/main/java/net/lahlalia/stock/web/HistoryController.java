package net.lahlalia.stock.web;


import lombok.RequiredArgsConstructor;
import net.lahlalia.stock.dtos.HistoryDto;
import net.lahlalia.stock.services.HistoryStockService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryStockService historyStockService;

    @GetMapping(value = "/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HistoryDto>> getAllHistories(){
        List<HistoryDto> historyDtoList = historyStockService.getAllHistoryDto();
        return ResponseEntity.ok(historyDtoList);

    }
}
