package net.lahlalia.stock.entities;

import jakarta.persistence.*;
import lombok.*;
import net.lahlalia.stock.enums.Area;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Depot {

    @Id
    private String idDepot;
    private String nameDepot;
    private String zone;
    @Enumerated(EnumType.STRING)
    private Area area;

    @OneToMany(mappedBy = "depot", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Bac> bacs;

    @OneToMany(mappedBy = "depot", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistoryStock> historyStocks;





}
