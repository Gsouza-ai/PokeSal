package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import main.ExecutorDeGolpe;
import main.GeradorAleatorio;
import main.Golpe;
import main.PokeSal;
import main.ResultadoAtaque;
import main.ServicoTerreno;
import main.Terreno;
import main.TipoElemental;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes do ExecutorDeGolpe. Mockados: GeradorAleatorio (controla a sorte) e ServicoTerreno
 * (isola o efeito do terreno). Reais: PokeSal, Golpe e TipoElemental (dados e regras puras).
 * Dano base usado: 15 (poder) + 25 (atk) - 10 (def) = 30, tipos iguais (x1.0).
 */
@ExtendWith(MockitoExtension.class)
public class TesteExecutorDeGolpe {

  @Mock
  private GeradorAleatorio gerador;

  @Mock
  private ServicoTerreno servicoTerreno;

  @InjectMocks
  private ExecutorDeGolpe executor;

  private PokeSal atacante;
  private PokeSal defensor;
  private Golpe golpe;

  @BeforeEach
  void preparar() {
    atacante = new PokeSal("Atacante", 100f, 25f, 10f, 20f, TipoElemental.FOGO);
    defensor = new PokeSal("Defensor", 100f, 15f, 10f, 10f, TipoElemental.FOGO);
    golpe = new Golpe("Labareda", TipoElemental.FOGO, 15f, 90f);
  }

  /** O terreno é neutro: devolve o próprio dano recebido (último argumento). */
  private void terrenoNeutro() {
    // Matchers (eq/any) aqui porque o valor do dano varia e só o Terreno e o Golpe importam.
    when(servicoTerreno.aplicarEfeitoAtivo(eq(Terreno.POCA_CHUVA), eq(golpe), anyFloat()))
        .thenAnswer(AdditionalAnswers.returnsLastArg());
  }

  @Test
  @DisplayName("Dano x1.5 quando o gerador mockado retorna valor dentro da faixa de crítico")
  public void testAcertoCriticoComGeradorMockado() {
    // retornos em sequência: 1ª chamada = precisão (0.10 -> 10 < 90 acerta),
    // 2ª chamada = crítico (0.03 < 0.0625)
    when(gerador.proximoDouble()).thenReturn(0.10, 0.03);
    terrenoNeutro();

    ResultadoAtaque resultado = executor.executar(atacante, defensor, golpe, Terreno.POCA_CHUVA);

    assertTrue(resultado.acertou());
    assertTrue(resultado.critico());
    assertEquals(45f, resultado.dano(), 0.001f, "30 x 1.5 = 45");
    assertEquals(55f, defensor.getHpAtual(), 0.001f);
    verify(gerador, times(2)).proximoDouble();
    // Valores exatos (sem matcher) porque aqui o dano antes do terreno é conhecido: 30.
    verify(servicoTerreno).aplicarEfeitoAtivo(Terreno.POCA_CHUVA, golpe, 30f);
  }

  @Test
  @DisplayName("Sem crítico quando o sorteio fica fora da faixa de 6,25%")
  public void testAcertoSemCriticoQuandoSorteioForaDaFaixa() {
    when(gerador.proximoDouble()).thenReturn(0.10, 0.50);
    terrenoNeutro();

    ResultadoAtaque resultado = executor.executar(atacante, defensor, golpe, Terreno.POCA_CHUVA);

    assertTrue(resultado.acertou());
    assertFalse(resultado.critico());
    assertEquals(30f, resultado.dano(), 0.001f);
    assertEquals(70f, defensor.getHpAtual(), 0.001f);
  }

  @Test
  @DisplayName("O limite da faixa de crítico (0.0625) não é crítico; logo abaixo é")
  public void testLimiteDaFaixaDeCritico() {
    when(gerador.proximoDouble()).thenReturn(0.10, 0.0625, 0.10, 0.0624);
    terrenoNeutro();

    ResultadoAtaque noLimite = executor.executar(atacante, defensor, golpe, Terreno.POCA_CHUVA);
    ResultadoAtaque abaixo = executor.executar(atacante, defensor, golpe, Terreno.POCA_CHUVA);

    assertFalse(noLimite.critico());
    assertTrue(abaixo.critico());
  }

  @Test
  @DisplayName("Dano zero e HP do alvo inalterado quando a precisão falha")
  public void testGolpeErraQuandoPrecisaoFalha() {
    when(gerador.proximoDouble()).thenReturn(0.95); // 95 >= 90: erra

    ResultadoAtaque resultado = executor.executar(atacante, defensor, golpe, Terreno.POCA_CHUVA);

    assertFalse(resultado.acertou());
    assertEquals(0f, resultado.dano(), 0.001f);
    assertEquals(100f, defensor.getHpAtual(), 0.001f, "HP do alvo não pode mudar");
    verify(gerador, times(1)).proximoDouble(); // só sorteou a precisão
    verify(servicoTerreno, never()).aplicarEfeitoAtivo(any(Terreno.class), any(Golpe.class),
        anyFloat());
  }

  @Test
  @DisplayName("Precisão 90: sorteio 0.90 erra e 0.8999 acerta (limite)")
  public void testPrecisaoNoLimite() {
    when(gerador.proximoDouble()).thenReturn(0.90, 0.8999, 0.99);
    terrenoNeutro();

    ResultadoAtaque erra = executor.executar(atacante, defensor, golpe, Terreno.POCA_CHUVA);
    ResultadoAtaque acerta = executor.executar(atacante, defensor, golpe, Terreno.POCA_CHUVA);

    assertFalse(erra.acertou());
    assertTrue(acerta.acertou());
  }

  @Test
  @DisplayName("Precisão 100 sempre acerta e precisão 0 nunca acerta")
  public void testPrecisoesExtremas() {
    Golpe certeiro = new Golpe("Certeiro", TipoElemental.FOGO, 15f, 100f);
    Golpe impossivel = new Golpe("Impossivel", TipoElemental.FOGO, 15f, 0f);
    when(gerador.proximoDouble()).thenReturn(0.9999, 0.0);
    when(servicoTerreno.aplicarEfeitoAtivo(eq(Terreno.POCA_CHUVA), eq(certeiro), anyFloat()))
        .thenAnswer(AdditionalAnswers.returnsLastArg());

    assertTrue(executor.executar(atacante, defensor, certeiro, Terreno.POCA_CHUVA).acertou());
    assertFalse(executor.executar(atacante, defensor, impossivel, Terreno.POCA_CHUVA).acertou());
  }

  @Test
  @DisplayName("O efeito do terreno entra no cálculo antes do crítico")
  public void testDanoUsaValorDevolvidoPeloServicoDeTerreno() {
    when(gerador.proximoDouble()).thenReturn(0.10, 0.50);
    when(servicoTerreno.aplicarEfeitoAtivo(Terreno.ASFALTO_QUENTE, golpe, 30f)).thenReturn(34.5f);

    ResultadoAtaque resultado = executor.executar(atacante, defensor, golpe,
        Terreno.ASFALTO_QUENTE);

    assertEquals(35f, resultado.dano(), 0.001f, "34.5 arredonda para 35");
  }
}
