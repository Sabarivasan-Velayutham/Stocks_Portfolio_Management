package com.example.sample_neueda.repo;

import com.example.sample_neueda.entities.stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.*;
import com.example.sample_neueda.entities.accounts;
import java.util.*;

@Repository
public interface accountsRepo extends JpaRepository<accounts,Long> {
    accounts findByNumber(String number);

}
