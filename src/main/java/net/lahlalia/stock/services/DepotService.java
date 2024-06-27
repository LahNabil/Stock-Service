package net.lahlalia.stock.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.stock.dtos.*;
import net.lahlalia.stock.entities.Bac;
import net.lahlalia.stock.entities.Depot;
import net.lahlalia.stock.entities.HistoryStock;
import net.lahlalia.stock.exceptions.DepotNotFoundException;
import net.lahlalia.stock.mappers.*;
import net.lahlalia.stock.repositories.BacRepository;
import net.lahlalia.stock.repositories.DepotRepository;
import net.lahlalia.stock.repositories.HistoryStockRepository;
import net.lahlalia.stock.restClients.ProductRestClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepotService {
    private final DepotRepository depotRepository;
    private final MapperDepot depotMapper;
    private final BacRepository bacRepository;
    private final BacService bacService;
    private final MapperBac bacMapper;
    private final ProductRestClient productRestClient;
    private final HistoryStockRepository historyStockRepository;
    private final MapperHistoryStock historyStockMapper;
    private final HistoryStockService historyStockService;

    public DepotDTO saveDepot(DepotDTO dto){
        return depotMapper.toModel(
                depotRepository.save(
                        depotMapper.toEntity(dto)
                )
        );

    }
    public List<BacDto> findBacsByProductAndZone(String nameProduct, String zoneDepot) {
        List<DepotDTO> allDepots = geAllDepots();
        List<BacDto> result = new ArrayList<>();
        List<DepotDTO> filteredDepots = allDepots.stream()
                .filter(depot -> depot.getZone().equalsIgnoreCase(zoneDepot))
                .collect(Collectors.toList());

        // Filter Bacs by product name within the filtered depots
        for (DepotDTO depot : filteredDepots) {
            String depotId = depot.getIdDepot(); // Get the ID of the current depot
            List<BacDto> filteredBacs = depot.getBacDtos().stream()
                    .filter(bac -> {
                        String productName = bacService.getProductNameById(bac.getIdProduct());
                        return productName != null && productName.equalsIgnoreCase(nameProduct);
                    })
                    .peek(bac -> bac.setIdDepot(depotId)) // Set the idDepot field of each BacDto
                    .collect(Collectors.toList());
            result.addAll(filteredBacs);
        }


        return result;
    }

    public DepotDTO editDepot(String idDepot, DepotDTO depotDTO)throws EntityNotFoundException{
        Depot existingdepot = depotRepository.findById(idDepot)
                .orElseThrow(() -> new EntityNotFoundException("Depot with ID " + idDepot + " not found"));
        existingdepot.setNameDepot(depotDTO.getNameDepot());
        existingdepot.setZone(depotDTO.getZone());
        existingdepot.setArea(depotDTO.getArea());

        Depot updatedDepot = depotRepository.save(existingdepot);

        return depotMapper.toModel(updatedDepot);

    }
    public double CalculerStockProduitDepot(String idDepot,String nameProduct){
        if (idDepot == null) {
            throw new IllegalArgumentException("idDepot  must not be null");
        }
        if (nameProduct == null) {
            throw new IllegalArgumentException("nameProduct  must not be null");
        }
        Depot depot = depotRepository.findById(idDepot).get();
        List<Bac> bacList = depot.getBacs();
        List<Bac> bacListFilteredByProductName = bacList.stream()
                .filter(bac-> {
                    String productName = bacService.getProductNameById(bac.getIdProduct());
                    return productName.equals(nameProduct);
                })
                .collect(Collectors.toList());
        return bacListFilteredByProductName.stream()
                .mapToDouble(Bac::getCapacityUsed)
                .sum();



    }
    public double CalculerStockProduitDepotYearMonth(String idDepot, String nameProduct, int year, int month) {


        if (idDepot == null) {
            throw new IllegalArgumentException("idDepot must not be null");
        }
        if (nameProduct == null) {
            throw new IllegalArgumentException("nameProduct must not be null");
        }

        // Correct the month value (0-based index)
        month = month - 1;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = cal.getTime();
        log.info("Start Date: {}", startDate);

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = cal.getTime();
        log.info("End Date: {}", endDate);

        // Get all history data
        List<HistoryDto> historyDtoList = historyStockService.getAllHistoryDto();
        List<HistoryDto> filteredHistory = historyDtoList.stream()
                .filter(history -> {
                    boolean idDepotMatch = history.getIdDepot().equals(idDepot);
                    boolean nameProductMatch = history.getNameProduct().equals(nameProduct);
                    Date historyDate = history.getDateJour();
                    boolean dateMatch = !historyDate.before(startDate) && !historyDate.after(endDate);
                    log.info("History Record - ID: {}, Product: {}, Date: {}, ID Match: {}, Product Match: {}, Date Match: {}",
                            history.getIdDepot(), history.getNameProduct(), historyDate, idDepotMatch, nameProductMatch, dateMatch);
                    return idDepotMatch && nameProductMatch && dateMatch;
                })
                .collect(Collectors.toList());

        return filteredHistory.stream()
                .mapToDouble(HistoryDto::getStock)
                .sum();

    }

    public double CalculerStockDepotAllProducts(String idDepot){
        List<BacDto> bacDtos = bacService.getAllBacsForDepot(idDepot);
        return bacDtos.stream().mapToDouble(BacDto::getCapacityUsed).sum();


    }
    public List<Double> calculerStockDepot(){
        List<Depot> depots = depotRepository.findAll();
        List<Double> stockList = new ArrayList<>();
        for(Depot depot: depots){
            double stock = CalculerStockDepotAllProducts(depot.getIdDepot());
            stockList.add(stock);
        }
        return stockList;

    }

    public double CalculerCapacityDepot(String idDepot){
        if(idDepot == null){
            log.error("null values");
            return 0;
        }
        List<BacDto> bacDtos = bacService.getAllBacsForDepot(idDepot);
        return bacDtos.stream().mapToDouble(BacDto::getCapacity).sum();


    }
    public List<Double> CalculerCapacites(){
        List<Depot> depots = depotRepository.findAll();
        List<Double> capacities = new ArrayList<>();
        for(Depot depot: depots){
            double capacity = CalculerCapacityDepot(depot.getIdDepot());
            capacities.add(capacity);
        }
        return capacities;
    }

    public boolean deleteDepotById(String idDepot)throws EntityNotFoundException{
        DepotDTO dto = getDepotById(idDepot);
        if( dto != null){
            depotRepository.deleteById(idDepot);
            return true;
        } else {
            return false;
        }

    }
    public DepotDTO getDepotById(String idDepot )throws EntityNotFoundException{
        if(idDepot == null){
            log.error("id Depot is null");
            return null;
        }
        Depot depot = depotRepository.findById(idDepot).get();
        DepotDTO depotDTO = depotMapper.toModel(depot);
        List<BacDto> bacDtos = bacService.getAllBacsForDepot(idDepot);
        depotDTO.setBacDtos(bacDtos);
        return depotDTO;


    }
    //Executer chaque 24Heures
    @Scheduled(fixedRate = 86400000)
    public void saveDailyStock() {
        List<DepotDTO> depotDTOS = geAllDepots();
        for (DepotDTO depotDTO : depotDTOS) {
            List<StockProduitDto> stockProduitDtos = calculerStocksProduitsDansDepot(depotDTO);
            if (stockProduitDtos != null) {
                for (StockProduitDto stockProduitDto : stockProduitDtos) {
                    HistoryStock historyStock = new HistoryStock().builder()
                            .dateJour(new Date())
                            .stock(stockProduitDto.getQuantite())
                            .depot(depotMapper.toEntity(depotDTO))
                            .nameProduct(stockProduitDto.getNameProduit())
                            .build();
                    historyStockRepository.save(historyStock);
                    HistoryDto historyDto = historyStockMapper.toModel(historyStock);

                }
            } else {
                log.error("stockProduitDtos is null for depot: {}", depotDTO.getIdDepot());
                // Handle the null case accordingly
            }
        }
    }
    public double calculerStockSecurite(double quantity)throws DepotNotFoundException{
        return 1130;

    }

    public List<StockProduitDto> calculerStocksProduitsDansDepot(DepotDTO depotDTO) {
        List<BacDto> bacDtos = depotDTO.getBacDtos();
        if (bacDtos == null) {
            log.error("BacDtos is null for depot: {}", depotDTO.getIdDepot());
            return Collections.emptyList();// Return an empty list or handle it according to your use case
        }

        // Collecter les ID de produits distincts présents dans les Bacs
        Set<String> distinctProductNames = bacDtos.stream()
                .map(bac -> bacService.getProductNameById(bac.getIdProduct()))
                .collect(Collectors.toSet());

        // Pour chaque produit distinct, calculer le stock
        List<StockProduitDto> stocksProduits = new ArrayList<>();
        for (String productName : distinctProductNames) {
            double stockProduit = bacDtos.stream()
                    .filter(bac -> bacService.getProductNameById(bac.getIdProduct()).equals(productName))
                    .mapToDouble(BacDto::getCapacityUsed)
                    .sum();
            stocksProduits.add(new StockProduitDto(productName, stockProduit,calculerStockSecurite(stockProduit)));
        }

        return stocksProduits;
    }


    public List<DepotDTO> geAllDepots(){
        List<Depot> depots = depotRepository.findAll();
        List<DepotDTO> depotDTOS = new ArrayList<>();

        for(Depot depot : depots){
            List<BacDto> bacDtos = bacService.getAllBacsForDepot(depot.getIdDepot());
            DepotDTO depotDTO = depotMapper.toModel(depot);
            depotDTO.setBacDtos(bacDtos);
            depotDTOS.add(depotDTO);
        }
        return depotDTOS;

    }
    public String getCity(String idDepot)throws DepotNotFoundException{
        if(idDepot == null){
           log.error("value is null");
            return null;
        }
        Depot depot = depotRepository.findById(idDepot).get();
        return depot.getZone();

    }







}
