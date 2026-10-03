package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Testes novos do CP5 (Parte B): preco fixo da consulta, independente do porte (regra sem cobertura).
public class PrecoConsultaTest {

    private LocalDateTime data = LocalDateTime.of(2026, 10, 1, 14, 0);

    @Test
    public void deveCobrar150QualquerQueSejaOPorteQuandoConsulta() {
        // Arrange
        ConsultaVeterinaria pequeno = new ConsultaVeterinaria(1, "Rex", "PEQUENO", "Ana", data);
        ConsultaVeterinaria medio = new ConsultaVeterinaria(2, "Mimi", "MEDIO", "Bruno", data);
        ConsultaVeterinaria grande = new ConsultaVeterinaria(3, "Thor", "GRANDE", "Carla", data);

        // Act + Assert
        assertEquals(150.0, pequeno.calcularPreco());
        assertEquals(150.0, medio.calcularPreco());
        assertEquals(150.0, grande.calcularPreco());
    }
}