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
    private final DepotService depotService;
    private final DepotRepository depotRepository;
    private final HistoryStockService historyStockService;

    public List<StockEsDto> generateMonthlyStockReport(String idDepot,String nameProduct,int year, int month) {

        List<HistoryDto> historyDtoList = historyStockService.getAllHistoryDto();
        List<HistoryDto> filteredHistoryList = historyDtoList.stream()
                .filter(history -> history.getIdDepot().equals(idDepot) && history.getNameProduct().equals(nameProduct))
                .collect(Collectors.toList());
        List<ESDto> esDtoList = getSortiesParProduitDepot(idDepot,nameProduct);
        List<StockEsDto> stockEsDtoList = new ArrayList<>();
        double stockInitial = 1000.0; // Initialiser avec une valeur par défaut

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
                   if (esDto.getTypeES() == true){
                       entre += esDto.getQuantite();
                   } else if(esDto.getTypeES() == false){
                       sortie += esDto.getQuantite();
                   }
                }
            }


            // Rechercher les données historiques pour la date actuelle
            boolean found = false;

            // Rechercher les données historiques pour la date actuelle
            for (HistoryDto history : filteredHistoryList) {
                Calendar historyCal = Calendar.getInstance();
                historyCal.setTime(history.getDateJour());
                if (historyCal.get(Calendar.YEAR) == year &&
                        historyCal.get(Calendar.MONTH) == month - 1 &&
                        historyCal.get(Calendar.DAY_OF_MONTH) == day) {
                    stockInitial = history.getStock(); // Met à jour le stockInitial si une entrée correspondante est trouvée
                    found = true;
                    break; // On peut arrêter la boucle après avoir trouvé la correspondance
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
//        List<HistoryDto> historyDtoList = historyStockService.getAllHistoryDto();
//        List<EntreSortie> ESList = entreSortieRepository.findAll();
//        Depot depot = depotRepository.findById(idDepot).orElse(null);
//
//
//        List<Bac> bacList = depot.getBacs();
//
//        // Filtrer les bacs par produit
//        List<Bac> bacListFilteredByProductName = bacList.stream()
//                .filter(bac -> {
//                    String productName = bacService.getProductNameById(bac.getIdProduct());
//                    return productName.equals(nameProduct);
//                })
//                .collect(Collectors.toList());
//
//
//
//
//        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//        Date endDate = cal.getTime();
//
//
//        // Liste pour stocker les résultats pour chaque jour du mois
//        List<StockEsDto> stockEsDtoList = new ArrayList<>();
//
//        // Parcourir chaque jour du mois spécifié
//        for (int day = 1; day <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
//            cal.set(Calendar.DAY_OF_MONTH, day);
//            Date currentDate = cal.getTime();
//
//            // Déterminer la date du jour précédent
//            Calendar previousDayCal = Calendar.getInstance();
//            previousDayCal.setTime(currentDate);
//            previousDayCal.add(Calendar.DAY_OF_MONTH, -1);
//            Date previousDay = previousDayCal.getTime();
//
//            // Calculer le stock initial (stock du jour précédent)
//            double stockInitial = historyDtoList.stream()
//                    .filter(history -> {
//                        boolean idDepotMatch = history.getIdDepot().equals(idDepot);
//                        boolean nameProductMatch = history.getNameProduct().equals(nameProduct);
//                        Date historyDate = history.getDateJour();
//                        return idDepotMatch && nameProductMatch && historyDate.equals(previousDay);
//                    })
//                    .map(HistoryDto::getStock)
//                    .findFirst()
//                    .orElse(0.0);
//
//
//
//            // Filtrer les entrées et sorties pour le jour courant
//            List<EntreSortie> filteredES = ESList.stream()
//                    .filter(es -> {
//                        boolean matchesBac = bacListFilteredByProductName.stream()
//                                .anyMatch(bac -> bac.getIdBac().equals(es.getBac().getIdBac()));
//                        boolean matchesDate = es.getDate().equals(currentDate);
//                        return matchesBac && matchesDate;
//                    })
//                    .collect(Collectors.toList());
//
//
//            // Calculer les entrées et sorties
//            double entre = filteredES.stream()
//                    .filter(EntreSortie::getTypeES) // Assumant que 'type' est vrai pour les entrées
//                    .mapToDouble(EntreSortie::getQuantite)
//                    .sum();
//
//            double sortie = filteredES.stream()
//                    .filter(es -> !es.getTypeES()) // Assumant que 'type' est faux pour les sorties
//                    .mapToDouble(EntreSortie::getQuantite)
//                    .sum();
//
//            // Calculer le stock final
//            double stockFinale = stockInitial + entre - sortie;
//
//
//            // Créer et ajouter l'objet StockEsDto pour le jour courant à la liste
//            StockEsDto stockEsDto = StockEsDto.builder()
//                    .stockInitial(stockInitial)
//                    .entre(entre)
//                    .sortie(sortie)
//                    .stockFinale(stockFinale)
//                    .build();
//            stockEsDtoList.add(stockEsDto);
//        }
//
//        return stockEsDtoList;
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
        List<ESDto> esDtoList = ESList.stream()
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
        return esDtoList;
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
//       return entreSortieRepository.findAll().stream().map(esMapper::toModel).toList();
    }
//
}
