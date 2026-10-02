package br.com.fiap.petfiap.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Testes novos do CP5 (Parte B): duracao de cada tipo de atendimento (regra sem cobertura).
public class DuracaoAtendimentoTest {

    private LocalDateTime data = LocalDateTime.of(2026, 10, 1, 10, 0);

    @Test
    public void deveDurar60MinutosQuandoAtendimentoForTosa() {
        // Arrange
        Atendimento tosa = new Tosa(1, "Rex", "PEQUENO", "Ana", data);

        // Act + Assert
        assertEquals(60, tosa.getDuracaoMinutos());
    }
}