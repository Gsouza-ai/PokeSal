package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.PokeSal;
import main.TipoElemental;

public class TesteCalculoDanoBoundaryValues {
	
    @Test
    @DisplayName("Validação de valores limite de HP, ATK e DEF")
    public void testCalculoDanoBoundaryValues() {
        // Preparação: Criamos um PokeSal com 100 de HP Maximo
        PokeSal poke = new PokeSal("BoundarySal", 100f, 50f, 40f, 30f, TipoElemental.AGUA);

        // LIMITE INFERIOR DO HP (Dano Massivo)      
        // Ação: Tomar um dano muito maior que a vida atual
        poke.receberDano(9999f);
        
        // Verificação: A vida não pode ser -9899, tem que travar no limite mínimo (0)
        assertEquals(0f, poke.getHpAtual(), "O HP não pode ficar negativo ao receber dano extremo.");
        assertFalse(poke.estaVivo(), "O PokeSal não deve estar vivo com 0 de HP.");


        // LIMITE SUPERIOR DO HP (Cura Massiva)
        // Ãção: Revivemos o PokeSal com 10 de vida
        poke.setHpAtual(10f);
        
        // Ação: Curar um valor absurdamente alto
        poke.curar(9999f);

        // Verificar: A vida não pode passar de 100 (que é o hpMaximo dele)
        assertEquals(100f, poke.getHpAtual(), "O HP não pode ultrapassar o HP Máximo ao curar.");


        // LIMITES DE ATK E DEF (Zero absoluto)
        // Ação: Setar atributos de combate para o limite mais baixo possível (Zero)
        poke.setAtk(0f);
        poke.setDef(0f);

        // Verificação: Garante que a classe aceita e armazena o valor limite de zero
        assertEquals(0f, poke.getAtk(), "O ATK deve aceitar o valor limite inferior de 0.");
        assertEquals(0f, poke.getDef(), "A DEF deve aceitar o valor limite inferior de 0.");
    }

}
