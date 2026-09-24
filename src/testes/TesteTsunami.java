package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.PokeSal;
import main.Terreno;
import main.TipoElemental;
import main.Tsunami;

public class TesteTsunami {
	
	@Test
    @DisplayName("Deve causar 20% do HP máximo de dano e deixar o atacante exausto")
    public void testTsunamiPrimeiroUso() {
        // 1. Preparação
        PokeSal atacante = new PokeSal("AtacanteSal", 100f, 15f, 10f, 20f, TipoElemental.AGUA);
        // Defensor com 200 de HP Máximo (20% de 200 = 40 de dano)
        PokeSal defensor = new PokeSal("DefensorSal", 200f, 15f, 10f, 10f, TipoElemental.FOGO);
        
        Tsunami tsunami = new Tsunami();

        // 2. Ação
        // Executa sem terreno (null)
        float danoCausado = tsunami.executar(atacante, defensor, null);

        // 3. Verificações
        // O dano deve ser 40 (20% de 200)
        assertEquals(40.0f, danoCausado, "O dano deve ser 20% do HP Máximo do defensor");
        
        // A vida atual do defensor deve ter caído de 200 para 160
        assertEquals(160.0f, defensor.getHpAtual(), "O HP do defensor não foi reduzido corretamente");
        
        // O atacante deve estar exausto
        assertTrue(atacante.isExaustoTurnoAtual(), "O atacante deveria ficar exausto após usar o Tsunami");
        
        // O golpe deve estar marcado como utilizado
        assertTrue(tsunami.isJaUtilizadoNaBatalha(), "O status jaUtilizadoNaBatalha deveria ser true");
    }

    @Test
    @DisplayName("Não deve causar dano se for usado pela segunda vez na batalha")
    public void testTsunamiUsoRepetido() {
        // Preparação
        PokeSal atacante = new PokeSal("AtacanteSal", 100f, 15f, 10f, 20f, TipoElemental.AGUA);
        PokeSal defensor = new PokeSal("DefensorSal", 100f, 15f, 10f, 10f, TipoElemental.FOGO);
        Tsunami tsunami = new Tsunami();

        // Ação: Usa a primeira vez (Funciona)
        tsunami.executar(atacante, defensor, null);
        
        // Ação: Tenta usar a SEGUNDA vez (Deve falhar e retornar 0)
        float danoSegundoUso = tsunami.executar(atacante, defensor, null);

        // Verificação
        assertEquals(0.0f, danoSegundoUso, "O Tsunami não pode causar dano na segunda vez");
        // Como o defensor tinha 100 de HP, tomou 20 no primeiro uso, sobrou 80.
        // No segundo uso, a vida tem que continuar 80.
        assertEquals(80.0f, defensor.getHpAtual() );
    }

    @Test
    @DisplayName("Deve aplicar bônus de dano se o terreno for POCA_CHUVA")
    public void testTsunamiComTerrenoPocaChuva() {
        // Preparação
        PokeSal atacante = new PokeSal("AtacanteSal", 100f, 15f, 10f, 20f, TipoElemental.AGUA);
        // Defensor com 100 de HP. 20% seria 20 de dano normal.
        PokeSal defensor = new PokeSal("DefensorSal", 100f, 15f, 10f, 10f, TipoElemental.PLANTA);
        Tsunami tsunami = new Tsunami();

        // Ação: Executa COM o terreno POCA_CHUVA
        float danoCausado = tsunami.executar(atacante, defensor, Terreno.POCA_CHUVA);

        // Verificação
        // Dano base = 20. Bônus da chuva = +10%. Dano final = 22.
        assertEquals(22.0f, danoCausado, "O dano deve ser aumentado em 10% pela POCA_CHUVA");
    }

}
