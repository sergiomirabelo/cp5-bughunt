package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Testes novos do CP5 (Parte B): preco do banho por porte (regra sem cobertura).
public class PrecoBanhoTest {

    private LocalDateTime data = LocalDateTime.of(2026, 10, 1, 10, 0);

    @Test
    public void deveCobrarPrecoDoPorteQuandoBanho() {
        // Arrange
        Banho pequeno = new Banho(1, "Rex", "PEQUENO", "Ana", data);
        Banho grande = new Banho(2, "Thor", "GRANDE", "Bruno", data);

        // Act + Assert
        assertEquals(60.0, pequeno.calcularPreco());
        assertEquals(100.0, grande.calcularPreco());
    }
}