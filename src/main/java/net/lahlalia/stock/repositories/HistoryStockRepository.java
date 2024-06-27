package net.lahlalia.stock.repositories;


import net.lahlalia.stock.entities.HistoryStock;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistoryStockRepository extends JpaRepository<HistoryStock,Long> {
}
