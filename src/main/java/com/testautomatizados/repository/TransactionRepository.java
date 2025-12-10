package com.testautomatizados.repository;

import com.testautomatizados.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
public interface TransactionRepository extends JpaRepository <Transaction, Long>{
    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
