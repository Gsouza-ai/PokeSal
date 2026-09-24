package testes;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.Batalha;
import main.PokeSal;
import main.Terreno;
import main.TipoElemental;
import main.Treinador;

public class TesteOrdemDeAtaquePorVelocidade {
	
	@Test
    @DisplayName("Deve retornar o pokesal1 quando ele for mais rapido que o pokesal2")
    public void testIniciativaPokesal1MaisRapido() {
        // Preparação: poke1 com 20 de velocidade, poke2 com 10 de velocidade
        // Construtor: Nome, HP, Atq, Def, Spd, Tipo
        PokeSal poke1 = new PokeSal("RapidoSal", 100, 15, 10, 20, TipoElemental.AGUA);
        PokeSal poke2 = new PokeSal("LentoSal", 100, 15, 10, 10, TipoElemental.FOGO);
        
        Terreno terreno = Terreno.CANTEIRO_CENTRAL; //terreno só para iniciar a batalha

        Treinador treinador1 = new Treinador("Treinador 1");
        treinador1.escolherInicial(poke1);
        Treinador treinador2 = new Treinador("Treinador 2");
        treinador2.escolherInicial(poke2);

        // Instancia a classe onde o método determinarIniciativa está (ex: Batalha)
        Batalha batalha = new Batalha(treinador1, treinador2, terreno);

        // Ação
        PokeSal primeiro = batalha.determinarIniciativa();

        // Verificação: O retorno deve ser exatamente a referência do poke1
        assertSame(poke1, primeiro, "O poke1 deveria agir primeiro por ter maior Spd");
    }

    @Test
    @DisplayName("Deve retornar o pokesal2 quando ele for mais rapido que o pokesal1")
    public void testIniciativaPokesal2MaisRapido() {
        // Preparação: poke1 com 10 de velocidade, poke2 com 25 de velocidade
        PokeSal poke1 = new PokeSal("LentoSal", 100, 15, 10, 10, TipoElemental.AGUA);
        PokeSal poke2 = new PokeSal("RapidoSal", 100, 15, 10, 25, TipoElemental.FOGO);
        
        Terreno terreno = Terreno.CANTEIRO_CENTRAL; //terreno só para iniciar a batalha


        Treinador treinador1 = new Treinador("Treinador 1");
        treinador1.escolherInicial(poke1);
        Treinador treinador2 = new Treinador("Treinador 2");
        treinador2.escolherInicial(poke2);

        Batalha batalha = new Batalha(treinador1, treinador2, terreno);


        // Ação
        PokeSal primeiro = batalha.determinarIniciativa();

        // Verificação
        assertSame(poke2, primeiro, "O poke2 deveria agir primeiro por ter maior Spd");
    }

    @Test
    @DisplayName("Deve desempatar a favor do pokesal1 quando as velocidades forem iguais")
    public void testIniciativaVelocidadesIguais() {
        // Preparação: Ambos com 15 de velocidade
        PokeSal poke1 = new PokeSal("SalUm", 100, 15, 10, 15, TipoElemental.PLANTA);
        PokeSal poke2 = new PokeSal("SalDois", 100, 15, 10, 15, TipoElemental.AGUA);

        Terreno terreno = Terreno.CANTEIRO_CENTRAL; //terreno só para iniciar a batalha


        Treinador treinador1 = new Treinador("Treinador 1");
        treinador1.escolherInicial(poke1);
        Treinador treinador2 = new Treinador("Treinador 2");
        treinador2.escolherInicial(poke2);

        Batalha batalha = new Batalha(treinador1, treinador2, terreno);



        // Ação
        PokeSal primeiro = batalha.determinarIniciativa();

        // Verificação: Por causa da regra '>=' no if, poke1 leva vantagem
        assertSame(poke1, primeiro, "No empate, o poke1 do treinador1 deve ter prioridade");
    }

}
