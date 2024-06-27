package net.lahlalia.stock.repositories;

import net.lahlalia.stock.entities.Depot;
import net.lahlalia.stock.entities.HistoryStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface HistoryStockRepository extends JpaRepository<HistoryStock,Long> {
}
