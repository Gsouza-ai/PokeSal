package testes;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.Mochila;

public class TestPodeUsarItem {
	
	@Test
    @DisplayName("Deve retornar false se tentar usar o item na rodada 1 (menor que 2)")
    public void testUsoItemAntesDaSegundaRodada() {
        // cria a mochila
        Mochila mochila = new Mochila();
        
        // Passando rodadaAtual = 1. Obriga o seu código a entrar no "if (rodadaAtual < 2)"
        // O segundo número (itens usados) não importa nesse teste, então mandamos 0.
        boolean resultado = mochila.podeUsarItem(1, 0);
        
        // Garantimos que o método barrou a ação e devolveu "false"
        assertFalse(resultado, "O sistema deveria bloquear o uso de itens e retornar false na rodada 1.");
    }

}
