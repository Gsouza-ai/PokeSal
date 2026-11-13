package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import main.ExecutorDeGolpe;
import main.GeradorAleatorio;
import main.Golpe;
import main.PokeSal;
import main.ResultadoAtaque;
import main.ServicoTerreno;
import main.ServicoTerrenoPadrao;
import main.Terreno;
import main.TipoElemental;
import main.Tsunami;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes do golpe especial Tsunami, executado pelo ExecutorDeGolpe.
 */
@ExtendWith(MockitoExtension.class)
public class TesteTsunami {

    @Mock
    private GeradorAleatorio gerador;

    @Mock
    private ServicoTerreno servicoTerreno;

    @Test
    @DisplayName("Deve causar 20% do HP máximo de dano e deixar o atacante exausto")
    public void testTsunamiPrimeiroUso() {
        PokeSal atacante = new PokeSal("AtacanteSal", 100f, 15f, 10f, 20f, TipoElemental.AGUA);
        PokeSal defensor = new PokeSal("DefensorSal", 200f, 15f, 10f, 10f, TipoElemental.FOGO);
        Tsunami tsunami = new Tsunami();
        ExecutorDeGolpe executor = new ExecutorDeGolpe(gerador, servicoTerreno);
        // Tsunami tem precisão 100: qualquer sorteio em [0,1) acerta.
        when(gerador.proximoDouble()).thenReturn(0.0);
        when(servicoTerreno.aplicarEfeitoAtivo(any(Terreno.class), any(Golpe.class), anyFloat()))
                .thenAnswer(AdditionalAnswers.returnsLastArg());

        ResultadoAtaque resultado = executor.executar(atacante, defensor, tsunami,
                Terreno.CANTEIRO_CENTRAL);

        assertEquals(40.0f, resultado.dano(), 0.001f, "20% de 200");
        assertEquals(160.0f, defensor.getHpAtual(), 0.001f);
        assertTrue(atacante.isExaustoTurnoAtual(), "o atacante deve ficar exausto");
        assertTrue(tsunami.isJaUtilizadoNaBatalha());
        assertFalse(resultado.critico());
        // Tsunami não admite crítico: só o sorteio de precisão é consumido.
        verify(gerador, times(1)).proximoDouble();
    }

    @Test
    @DisplayName("Não deve causar dano nem sortear nada se for usado pela segunda vez")
    public void testTsunamiUsoRepetido() {
        PokeSal atacante = new PokeSal("AtacanteSal", 100f, 15f, 10f, 20f, TipoElemental.AGUA);
        PokeSal defensor = new PokeSal("DefensorSal", 100f, 15f, 10f, 10f, TipoElemental.FOGO);
        Tsunami tsunami = new Tsunami();
        ExecutorDeGolpe executor = new ExecutorDeGolpe(gerador, servicoTerreno);
        when(gerador.proximoDouble()).thenReturn(0.0);
        when(servicoTerreno.aplicarEfeitoAtivo(any(Terreno.class), any(Golpe.class), anyFloat()))
                .thenAnswer(AdditionalAnswers.returnsLastArg());

        executor.executar(atacante, defensor, tsunami, Terreno.CANTEIRO_CENTRAL);
        ResultadoAtaque segundoUso = executor.executar(atacante, defensor, tsunami,
                Terreno.CANTEIRO_CENTRAL);

        assertEquals(0.0f, segundoUso.dano(), 0.001f);
        assertEquals(80.0f, defensor.getHpAtual(), 0.001f, "só o 1º uso causou dano");
        verify(gerador, times(1)).proximoDouble(); // o 2º uso nem chegou a sortear
    }

    @Test
    @DisplayName("Deve aplicar bônus de dano se o terreno for POCA_CHUVA (serviço real)")
    public void testTsunamiComTerrenoPocaChuva() {
        PokeSal atacante = new PokeSal("AtacanteSal", 100f, 15f, 10f, 20f, TipoElemental.AGUA);
        PokeSal defensor = new PokeSal("DefensorSal", 100f, 15f, 10f, 10f, TipoElemental.PLANTA);
        // Aqui o ServicoTerrenoPadrao é real: queremos o bônus de 10% de verdade (a Poça de Chuva
        // não consulta o relógio, então Clock.systemDefaultZone() é suficiente).
        ExecutorDeGolpe executor = new ExecutorDeGolpe(gerador,
                new ServicoTerrenoPadrao(Clock.systemDefaultZone()));
        when(gerador.proximoDouble()).thenReturn(0.0);

        ResultadoAtaque resultado = executor.executar(atacante, defensor, new Tsunami(),
                Terreno.POCA_CHUVA);

        assertEquals(22.0f, resultado.dano(), 0.001f, "20 + 10% da chuva");
    }

    @Test
    @DisplayName("reiniciarParaNovaBatalha libera o Tsunami novamente")
    public void testTsunamiReiniciaParaNovaBatalha() {
        PokeSal atacante = new PokeSal("AtacanteSal", 100f, 15f, 10f, 20f, TipoElemental.AGUA);
        Tsunami tsunami = new Tsunami();
        tsunami.aposAcertar(atacante);
        assertFalse(tsunami.podeSerUsado());

        tsunami.reiniciarParaNovaBatalha();

        assertTrue(tsunami.podeSerUsado());
    }
}
