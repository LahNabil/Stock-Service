package net.lahlalia.stock.repositories;

import net.lahlalia.stock.entities.Depot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepotRepository extends JpaRepository<Depot,String> {

}
