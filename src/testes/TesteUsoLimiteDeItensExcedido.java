package testes;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.Mochila;

public class TesteUsoLimiteDeItensExcedido {
	
	@Test
    @DisplayName("Deve retornar false ao tentar usar mais itens que o limite")
    public void testUsoLimiteDeItensExcedidoRetornandoFalse() {
        // cria objeto mochila para testar
        Mochila mochila = new Mochila();
        int rodadaAtual = 3; // Uma rodada válida (maior que 2)
        int itensJaUsados = 2; // O limite da mochila é 2

        boolean podeUsar = mochila.podeUsarItem(rodadaAtual, itensJaUsados);

        // Como já usou 2, tem que dar false!
        assertFalse(podeUsar, "A mochila não deveria permitir o uso após o limite."); 
    }

}
