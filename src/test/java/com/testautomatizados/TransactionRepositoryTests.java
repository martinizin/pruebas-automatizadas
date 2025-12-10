package com.testautomatizados;

import com.testautomatizados.repository.TransactionRepository;
import com.testautomatizados.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TransactionRepositoryTests {
    @Autowired
    private TransactionRepository repository;

    @Test
    void testFindByCreatedAtBetween() {
        Transaction t1 = new Transaction();
        t1.setAmount(BigDecimal.TEN);
        t1.setCreatedAt(LocalDateTime.now().minusDays(1));
        repository.save(t1);

        Transaction t2 = new Transaction();
        t2.setAmount(BigDecimal.valueOf(20));
        t2.setCreatedAt(LocalDateTime.now());
        repository.save(t2);

        List<Transaction> result = repository.findByCreatedAtBetween(
                LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(1)
        );
        assertEquals(2, result.size());
    }

}
