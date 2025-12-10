package com.testautomatizados;

import com.testautomatizados.services.ComissionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComissionServiceTest {
    private final ComissionService service = new ComissionService();


    @Test
    void testZeroAmount() {
        BigDecimal expected = BigDecimal.ZERO;
        BigDecimal actual = service.calculateCommission(BigDecimal.ZERO);
        assertTrue(expected.compareTo(actual) == 0);
    }

    @Test
    void testTier1() {
        assertEquals(new BigDecimal("10.00"), service.calculateCommission(new BigDecimal("1000")));
    }

    @Test
    void testTier2() {
        assertEquals(new BigDecimal("100.00"), service.calculateCommission(new BigDecimal("5000")));
    }

    @Test
    void testTier3() {
        assertEquals(new BigDecimal("150.00"), service.calculateCommission(new BigDecimal("5000").add(BigDecimal.valueOf(1000))));
    }
}
