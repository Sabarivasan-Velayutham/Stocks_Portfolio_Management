package com.example.sample_neueda.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.*;
import com.example.sample_neueda.entities.stocks;
import java.util.*;

@Repository
public interface stocksRepo extends JpaRepository<stocks,Long> {
    stocks findBySymbol(String symbol);
}
