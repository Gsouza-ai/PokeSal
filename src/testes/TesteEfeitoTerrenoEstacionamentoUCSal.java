package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.Golpe;
import main.PokeSal;
import main.Terreno;
import main.TipoElemental;


public class TesteEfeitoTerrenoEstacionamentoUCSal {
	
	@Test
    @DisplayName("Deve multiplicar o dano por 1.15 quando for Asfalto Quente e golpe de Fogo")
    public void testAplicarEfeitoAtivo_AsfaltoQuenteComFogo() {
        // Cria um construtor para preparar o teste
        Golpe golpeFogo = new Golpe("Labareda", TipoElemental.FOGO, 15f, 0.9f);
        float danoBase = 100f;

        float danoFinal = Terreno.ASFALTO_QUENTE.aplicarEfeitoAtivo(golpeFogo, danoBase);

        // Verificação: 100 * 1.15 = 115.0
        assertEquals(115.0f, danoFinal);
    }

    @Test
    @DisplayName("Deve multiplicar o dano por 1.10 quando for Poça de Chuva e golpe de Água")
    public void testAplicarEfeitoAtivo_PocaChuvaComAgua() {
        // Cria um construtor para preparar o teste
        Golpe golpeAgua = new Golpe("Jato De Agua", TipoElemental.AGUA, 15f, 0.9f);
        float danoBase = 100f;

        float danoFinal = Terreno.POCA_CHUVA.aplicarEfeitoAtivo(golpeAgua, danoBase);

        // Verificação: 100 * 1.10 = 110.0
        assertEquals(110.0f, danoFinal);
    }

    @Test
    @DisplayName("Deve retornar o dano base normal se o terreno e o elemento não combinarem")
    public void testAplicarEfeitoAtivo_SemCombinacao() {
        // Cria golpe
        Golpe golpeAgua = new Golpe("Jato De Agua", TipoElemental.AGUA, 15f, 0.9f);
        float danoBase = 100f;

        // Ação
        float danoFinal = Terreno.ASFALTO_QUENTE.aplicarEfeitoAtivo(golpeAgua, danoBase);

        // Verificação: O dano deve continuar 100.0
        assertEquals(100.0f, danoFinal);
    }
    
    @Test
    @DisplayName("Deve curar 5% do HP máximo se o PokeSal for de Planta no Canteiro Central")
    public void testAplicarEfeitoPassivo_CanteiroCentralComPlanta() {
        // Criação do pokeSal 
        PokeSal pokesalPlanta = new PokeSal("BulbaSal", 100, 18, 12, 12, TipoElemental.PLANTA);
        pokesalPlanta.receberDano(20); //da um dano no pokeSal para simular o metodo de cura
        
        /* 
         * pokeSal nasce com 100 de HP, 
         * o metodo curar não deixa a vida com mais de 100hp, logo a cura não vai surtir efeito.
         */

        
        Terreno.CANTEIRO_CENTRAL.aplicarEfeitoPassivo(pokesalPlanta);

        // Verificação: 5% de 100 de HP é 5. Logo, se ele puder passar do máximo, vai para 105.
        // (Se não puder passar do máximo e você tirou 20 de dano antes, iria para 85).
        // Substitua 'getHp()' pelo nome real do seu método que pega a vida atual.
        assertEquals(85.0f, pokesalPlanta.getHpAtual()); 
    }

    @Test
    @DisplayName("Não deve curar nada se o PokeSal não for do tipo Planta")
    public void testAplicarEfeitoPassivo_CanteiroCentralComFogo() {
        // Preparação: Um PokeSal de Fogo
        PokeSal pokesalFogo = new PokeSal("CharSal", 100, 20, 10, 15, TipoElemental.FOGO);
		pokesalFogo.receberDano(20); //simula 20 de dano
        
        // Ação
        Terreno.CANTEIRO_CENTRAL.aplicarEfeitoPassivo(pokesalFogo);

        // Verificação: Como não é de Planta, a vida deve permanecer em 80.
        assertEquals(80.0f, pokesalFogo.getHpAtual());
    }

}
