package com.ppojin.warehouse.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, String> {
    @Override
    List<StockEntity> findAll();

    List<StockEntity> findAllByName(String name);
}
