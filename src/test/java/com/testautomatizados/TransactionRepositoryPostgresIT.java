package com.testautomatizados;

import com.testautomatizados.model.Transaction;
import com.testautomatizados.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryPostgresIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TransactionRepository repository;

    @Test
    void testFindByCreatedAtBetween_PostgresContainer() {
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
