package net.lahlalia.stock.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lahlalia.stock.dtos.BacDto;
import net.lahlalia.stock.dtos.Product;
import net.lahlalia.stock.entities.Bac;
import net.lahlalia.stock.entities.Depot;
import net.lahlalia.stock.entities.EntreSortie;
import net.lahlalia.stock.mappers.MapperBac;
import net.lahlalia.stock.repositories.BacRepository;
import net.lahlalia.stock.repositories.DepotRepository;
import net.lahlalia.stock.repositories.EntreSortieRepository;
import net.lahlalia.stock.restClients.ProductRestClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BacService {
    private final BacRepository bacRepository;
    private final MapperBac bacMapper;
    private final ProductRestClient productRestClient;
    private final EntreSortieRepository entreSortieRepository;
    private final DepotRepository depotRepository;

    public BacDto getBacById(String idBac ) throws EntityNotFoundException {
        if(idBac == null){
            log.error("id Bac is null");
            return null;
        }
        Bac bac = bacRepository.findById(idBac).get();
        BacDto bacDto =  bacMapper.toModel(bac);
        bacDto.setIdProduct(bac.getIdProduct());
        bacDto.setIdDepot(bac.getDepot().getIdDepot());
        return bacDto;
    }
    public String getProductNameById(Long idProduit){
        Product product = productRestClient.getProductById(idProduit);
        String productName = product.getName();
        return productName;

    }
    public List<BacDto> getAllBacs(){
        List<Bac> bacList = bacRepository.findAll();
        List<BacDto> bacsDto = new ArrayList<>();
        for (Bac bac : bacList) {
            BacDto bacDto = bacMapper.toModel(bac);
            try {
                bacDto.setIdProduct(bac.getIdProduct());
                bacDto.setIdDepot(bac.getDepot().getIdDepot());
            } catch (EntityNotFoundException e) {
                log.error("Product not found for Bac ID: " + bac.getIdProduct());
            }
            bacsDto.add(bacDto);
        }
        return bacsDto;
    }
    public double calculerCreux(String idBac)throws EntityNotFoundException{
        Bac bac = bacRepository.findById(idBac).get();
        BacDto dto = bacMapper.toModel(bac);
        double creux = dto.getCapacity() - dto.getCapacityUsed() - dto.getTotalImpom();
        return creux;
    }
    public boolean deleteBacById(String idBac)throws EntityNotFoundException{
        BacDto dto = getBacById(idBac);
        if( dto != null){
            bacRepository.deleteById(idBac);
            return true;
        } else {
            return false;
        }

    }
    public BacDto updateBac(String idBac, BacDto bacDto) throws EntityNotFoundException {
        try {
            Bac existingBac = bacRepository.findById(idBac)
                    .orElseThrow(() -> new EntityNotFoundException("Bac with ID " + idBac + " not found"));

            existingBac.setCapacity(bacDto.getCapacity());
            existingBac.setTotalImpom(bacDto.getTotalImpom());
            existingBac.setStatus(bacDto.isStatus());
            existingBac.setDateOuverture(bacDto.getDateOuverture());
            Bac updatedBac = bacRepository.save(existingBac);

            return bacMapper.toModel(updatedBac);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Error updating Bac: " + ex.getMessage());
        }
    }
    public List<BacDto> getBacsByNameProduct(String nameProduct){
        List<Bac> bacList = bacRepository.findAll();
        List<Bac> filteredBacList = bacList.stream()
                .filter(bac->{
                    String productName = getProductNameById(bac.getIdProduct());
                    return productName != null && productName.equals(nameProduct);
                })
                .collect(Collectors.toList());
        List<BacDto> filteredBacDtoList = filteredBacList.stream()
                .map(bacMapper::toModel)
                .collect(Collectors.toList());

        return filteredBacDtoList;


    }

    public BacDto saveBac(BacDto dto)throws EntityNotFoundException {
        if(dto == null){
            log.error("Bac is null");
            return null;
        }
        Bac bac = bacMapper.toEntity(dto);
        if(dto.getIdProduct() != null || dto.getIdDepot() != null){
            bac.setIdProduct(dto.getIdProduct());
            Depot depot = depotRepository.findById(dto.getIdDepot()).get();
            bac.setDepot(depot);
        }

        Bac savedBac = bacRepository.save(bac);
        return bacMapper.toModel(savedBac);


    }
    public List<BacDto> getAllBacsForDepot(String idDepot){
        List<Bac> bacs = bacRepository.findAllByDepotId(idDepot);
        List<BacDto> bacDtos = bacs.stream().map(bacMapper::toModel).toList();
        return bacDtos;
    }
    public BacDto ESrProduit(EntreSortie es,String idBac)throws EntityNotFoundException{
        if(es == null || idBac == null || es.getBac().getIdBac()== null  ){
            log.error("value is null");
            return null;
        }

        Bac bac = bacRepository.findById(idBac).get();
        if(es.getTypeES()){
            double quantity = bac.getCapacityUsed() + es.getQuantite();
            if(quantity > bac.getCapacity()){
                log.error("vous avez depassé la capicité possible");
                throw new IllegalArgumentException("Cannot add quantity: exceeding bac capacity");
            }else{
                bac.setCapacityUsed(quantity);
                Bac savedBac = bacRepository.save(bac);
                es.setBusiness(null);
                entreSortieRepository.save(es);
                return bacMapper.toModel(savedBac);
            }

        }else if (!es.getTypeES()){
            double quantity = bac.getCapacityUsed() - es.getQuantite();
            if(quantity < 0){
                log.error("Insufficient quantity in bac");
                throw new IllegalArgumentException("Cannot subtract quantity: insufficient quantity in bac");
            }
            bac.setCapacityUsed(quantity);
            Bac savedBac = bacRepository.save(bac);
            if(es.getBusiness() == null ){
                return null;
            }else{
                entreSortieRepository.save(es);
                return bacMapper.toModel(savedBac);
            }

        }else{
            log.error("invalid type of EntreeSortie" + es.getTypeES());
            return null;

        }
    }
}
