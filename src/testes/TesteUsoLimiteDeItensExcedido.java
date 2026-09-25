package testes;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.Mochila;
import exception.LimiteDeItensExcedidoException;


public class TesteUsoLimiteDeItensExcedido {

    @Test
    @DisplayName("Deve lançar exceção ao tentar usar mais itens que o limite")
    public void testUsoLimiteDeItensExcedido() {
        // cria objeto mochila para testar
        Mochila mochila = new Mochila();
        int rodadaAtual = 3; // simula rodada valida
        int itensJaUsados = 2; // O limite da mochila é 2

        // Como já usou 2, tem que lançar a exceção
        assertThrows(
                LimiteDeItensExcedidoException.class,
                () -> mochila.podeUsarItem(rodadaAtual, itensJaUsados),
                "A mochila deveria lançar exceção ao exceder o limite de itens."
        );
    }

}
