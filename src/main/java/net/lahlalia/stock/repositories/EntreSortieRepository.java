package net.lahlalia.stock.repositories;

import net.lahlalia.stock.entities.EntreSortie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntreSortieRepository extends JpaRepository<EntreSortie, Long> {
}
