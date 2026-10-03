package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Testes novos do CP5 (Parte B): preco da tosa por porte (regra sem cobertura).
public class PrecoTosaTest {

    private LocalDateTime data = LocalDateTime.of(2026, 10, 1, 10, 0);

    @Test
    public void deveCobrarPrecoDoPorteQuandoTosaForMediaOuGrande() {
        // Arrange
        Tosa medio = new Tosa(1, "Mimi", "MEDIO", "Ana", data);
        Tosa grande = new Tosa(2, "Thor", "GRANDE", "Bruno", data);

        // Act + Assert
        assertEquals(90.0, medio.calcularPreco());
        assertEquals(120.0, grande.calcularPreco());
    }
}