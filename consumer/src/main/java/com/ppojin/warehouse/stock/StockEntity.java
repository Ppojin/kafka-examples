package com.ppojin.warehouse.stock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stock", schema = "item")
@Getter @Setter
public class StockEntity {
    @Id
    @Column(nullable = false)
    private Integer id;

    @Column
    private String name;

    @Column
    private Integer stock;
}
