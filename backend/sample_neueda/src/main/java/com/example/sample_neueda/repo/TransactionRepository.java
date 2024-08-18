package com.example.sample_neueda.repo;

import com.example.sample_neueda.entities.stocks;
import com.example.sample_neueda.entities.transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<transaction, Long> {
    List<transaction> findByStock(stocks stock);
}
