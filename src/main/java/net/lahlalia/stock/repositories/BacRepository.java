package net.lahlalia.stock.repositories;

import net.lahlalia.stock.entities.Bac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BacRepository extends JpaRepository<Bac, String> {

    @Query("SELECT b FROM Bac b WHERE b.depot.idDepot = :idDepot")
    List<Bac> findAllByDepotId(String idDepot);

}
