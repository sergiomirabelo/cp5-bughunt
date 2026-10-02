package br.com.fiap.petfiap.model;

import br.com.fiap.petfiap.exception.StatusInvalidoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Testes novos do CP5 (Parte B): regras de status sem cobertura na suite entregue.
public class AtendimentoStatusTest {

    @Test
    public void deveRecusarCancelamentoQuandoAtendimentoJaConcluido() {
        // Arrange: atendimento ja realizado
        Banho concluido = new Banho(1, "Rex", "PEQUENO", "Ana", LocalDateTime.now().plusDays(1));
        concluido.setStatus("CONCLUIDO");

        // Act + Assert
        assertThrows(StatusInvalidoException.class, () -> concluido.cancelar());

        // O status nao muda quando a operacao e recusada
        assertEquals("CONCLUIDO", concluido.getStatus());
    }
}