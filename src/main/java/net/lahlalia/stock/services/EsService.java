package net.lahlalia.stock.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.stock.dtos.BacDto;
import net.lahlalia.stock.dtos.ESDto;
import net.lahlalia.stock.dtos.HistoryDto;
import net.lahlalia.stock.dtos.StockEsDto;
import net.lahlalia.stock.entities.Bac;
import net.lahlalia.stock.entities.Depot;
import net.lahlalia.stock.entities.EntreSortie;
import net.lahlalia.stock.mappers.MapperEs;
import net.lahlalia.stock.repositories.DepotRepository;
import net.lahlalia.stock.repositories.EntreSortieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EsService {
    private final EntreSortieRepository entreSortieRepository;
    private final MapperEs esMapper;
    private final BacService bacService;
    private final DepotRepository depotRepository;
    private final HistoryStockService historyStockService;

    public List<StockEsDto> generateMonthlyStockReport(String idDepot,String nameProduct,int year, int month) {

        List<HistoryDto> historyDtoList = historyStockService.getAllHistoryDto();
        List<HistoryDto> filteredHistoryList = historyDtoList.stream()
                .filter(history -> history.getIdDepot().equals(idDepot) && history.getNameProduct().equals(nameProduct))
                .collect(Collectors.toList());
        List<ESDto> esDtoList = getSortiesParProduitDepot(idDepot,nameProduct);
        List<StockEsDto> stockEsDtoList = new ArrayList<>();
        double stockInitial = 1000.0;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        double entre = 0.0;
        double sortie = 0.0;

        for (int day = 1; day <= maxDay; day++) {


            cal.set(year, month - 1, day);
            Date dateJour = cal.getTime();
            for(ESDto esDto : esDtoList){
                Calendar esCal = Calendar.getInstance();
                esCal.setTime(esDto.getDate());
                if(esCal.get(Calendar.YEAR) == year && esCal.get(Calendar.MONTH) == month - 1
                && esCal.get(Calendar.DAY_OF_MONTH) == day
                ){
                   if (esDto.getTypeES()){
                       entre += esDto.getQuantite();
                   } else{
                       sortie += esDto.getQuantite();
                   }
                }
            }
            boolean found = false;

            for (HistoryDto history : filteredHistoryList) {
                Calendar historyCal = Calendar.getInstance();
                historyCal.setTime(history.getDateJour());
                if (historyCal.get(Calendar.YEAR) == year &&
                        historyCal.get(Calendar.MONTH) == month - 1 &&
                        historyCal.get(Calendar.DAY_OF_MONTH) == day) {
                    stockInitial = history.getStock();
                    found = true;
                    break;
                }
            }


            double stockFinale = stockInitial + entre - sortie;

            StockEsDto stockDto = StockEsDto.builder()
                    .dateJour(dateJour)
                    .stockInitial(stockInitial)
                    .entre(entre)
                    .sortie(sortie)
                    .stockFinale(stockFinale)
                    .build();

            stockEsDtoList.add(stockDto);

            // Mettre à jour le stock initial pour le jour suivant
            stockInitial = stockFinale;
            entre = 0;
            sortie = 0;
        }

        return stockEsDtoList;
    }

    public List<ESDto> getSortiesParProduitDepot(String idDepot,String nameProduct)throws EntityNotFoundException{
        if(nameProduct == null || idDepot == null){
            return null;
        }
        Depot depot = depotRepository.findById(idDepot).get();
        List<Bac> bacList = depot.getBacs();
        List<Bac> bacListFilteredByProductName = bacList.stream()
                .filter(bac-> {
                    String productName = bacService.getProductNameById(bac.getIdProduct());
                    return productName.equals(nameProduct);
                })
                .collect(Collectors.toList());
        List<EntreSortie> ESList = entreSortieRepository.findAll();

        ESList = ESList.stream()
                .filter(es->bacListFilteredByProductName.stream()
                        .anyMatch(bac->bac.getIdBac().equals(es.getBac().getIdBac())))
                .collect(Collectors.toList());
        return ESList.stream()
                .map(es -> {
                    ESDto esDto = esMapper.toModel(es);
                    if (es.getQuantite() != 0) {
                        if (Boolean.FALSE.equals(es.getTypeES())) {
                            esDto.setQuantite(-es.getQuantite());
                        } else {
                            esDto.setQuantite(es.getQuantite());
                        }
                    }
                    try {
                        esDto.setIdBac(es.getBac().getIdBac());
                        BacDto bacDto = bacService.getBacById(esDto.getIdBac());
                        esDto.setNameProduct(bacService.getProductNameById(bacDto.getIdProduct()));
                    } catch (EntityNotFoundException e) {
                        log.error("Bac not found for Bac ID: " + es.getId());
                    }
                    return esDto;
                })
                .collect(Collectors.toList());

    }
    public List<ESDto> getAllES(){
        List<EntreSortie> ESList = entreSortieRepository.findAll();
        List<ESDto> esDtoList = new ArrayList<>();
        for(EntreSortie es : ESList){
            ESDto esDto = esMapper.toModel(es);
            if (es.getQuantite() != 0) {
                if (Boolean.FALSE.equals(es.getTypeES())) {
                    esDto.setQuantite(-es.getQuantite());
                } else {
                    esDto.setQuantite(es.getQuantite());
                }
            }
            try{
                esDto.setIdBac(es.getBac().getIdBac());
                BacDto bacDto = bacService.getBacById(esDto.getIdBac());
                esDto.setNameProduct(bacService.getProductNameById(bacDto.getIdProduct()));

            }catch (EntityNotFoundException e) {

                log.error("Bac not found for Bac ID: " + es.getId());
            }
            esDtoList.add(esDto);
        }
        return esDtoList;
    }

}
