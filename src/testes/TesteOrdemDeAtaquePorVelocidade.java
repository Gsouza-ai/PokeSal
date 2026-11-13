package testes;

import static org.junit.jupiter.api.Assertions.assertSame;

import main.Batalha;
import main.Confronto;
import main.ExecutorDeGolpe;
import main.GeradorAleatorio;
import main.PokeSal;
import main.RegistroBatalha;
import main.RepositorioResultados;
import main.ServicoTerreno;
import main.Terreno;
import main.TipoElemental;
import main.Treinador;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes da iniciativa por velocidade. Os colaboradores da Batalha são mocks sem comportamento
 * (a iniciativa não usa nenhum deles).
 */
@ExtendWith(MockitoExtension.class)
public class TesteOrdemDeAtaquePorVelocidade {

    @Mock
    private ExecutorDeGolpe executor;

    @Mock
    private ServicoTerreno servicoTerreno;

    @Mock
    private RegistroBatalha registro;

    @Mock
    private RepositorioResultados repositorio;

    @Mock
    private GeradorAleatorio gerador;

    private PokeSal primeiroDaBatalha(PokeSal poke1, PokeSal poke2) {
        Treinador treinador1 = new Treinador("Treinador 1");
        treinador1.escolherInicial(poke1);
        Treinador treinador2 = new Treinador("Treinador 2");
        treinador2.escolherInicial(poke2);
        Batalha batalha = new Batalha(new Confronto(treinador1, treinador2, Terreno.CANTEIRO_CENTRAL),
                executor, servicoTerreno, registro, repositorio, gerador);
        return batalha.determinarIniciativa();
    }

    @Test
    @DisplayName("Deve retornar o pokesal1 quando ele for mais rapido que o pokesal2")
    public void testIniciativaPokesal1MaisRapido() {
        PokeSal poke1 = new PokeSal("RapidoSal", 100, 15, 10, 20, TipoElemental.AGUA);
        PokeSal poke2 = new PokeSal("LentoSal", 100, 15, 10, 10, TipoElemental.FOGO);

        assertSame(poke1, primeiroDaBatalha(poke1, poke2), "O poke1 deveria agir primeiro");
    }

    @Test
    @DisplayName("Deve retornar o pokesal2 quando ele for mais rapido que o pokesal1")
    public void testIniciativaPokesal2MaisRapido() {
        PokeSal poke1 = new PokeSal("LentoSal", 100, 15, 10, 10, TipoElemental.AGUA);
        PokeSal poke2 = new PokeSal("RapidoSal", 100, 15, 10, 25, TipoElemental.FOGO);

        assertSame(poke2, primeiroDaBatalha(poke1, poke2), "O poke2 deveria agir primeiro");
    }

    @Test
    @DisplayName("Deve desempatar a favor do pokesal1 quando as velocidades forem iguais")
    public void testIniciativaVelocidadesIguais() {
        PokeSal poke1 = new PokeSal("SalUm", 100, 15, 10, 15, TipoElemental.PLANTA);
        PokeSal poke2 = new PokeSal("SalDois", 100, 15, 10, 15, TipoElemental.AGUA);

        assertSame(poke1, primeiroDaBatalha(poke1, poke2), "No empate, o poke1 tem prioridade");
    }

    @Test
    @DisplayName("BUG: paralisia reduz a velocidade uma vez só e muda a iniciativa")
    public void testParalisiaMudaIniciativa() {
        PokeSal poke1 = new PokeSal("ParalisadoSal", 100, 15, 10, 20, TipoElemental.AGUA);
        PokeSal poke2 = new PokeSal("NormalSal", 100, 15, 10, 15, TipoElemental.FOGO);
        poke1.aplicarStatus(main.StatusEffect.PARALISADO); // 20 x 0.5 = 10 < 15

        assertSame(poke2, primeiroDaBatalha(poke1, poke2));
    }
}
