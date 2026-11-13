package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import main.Batalha;
import main.Confronto;
import main.EstrategiaDeAcao;
import main.ExecutorDeGolpe;
import main.GeradorAleatorio;
import main.Golpe;
import main.PokeSal;
import main.RegistroBatalha;
import main.RepositorioResultados;
import main.ResultadoAtaque;
import main.ResultadoBatalha;
import main.ServicoTerreno;
import main.Terreno;
import main.TipoElemental;
import main.Treinador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Requisito autoral RA-F3-01: Decisão por Limite de Rodadas.
 * Se ninguém cair em 10 rodadas, vence quem tem maior percentual de HP; em empate exato
 * de HP o GeradorAleatorio desempata (sorteio &lt; 0.5 = treinador 1). Colaboradores
 * mockados: GeradorAleatorio e RegistroBatalha (além do executor, que não causa dano).
 */
@ExtendWith(MockitoExtension.class)
public class TesteDecisaoPorLimiteDeRodadas {

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

  @Mock
  private EstrategiaDeAcao estrategia;

  private PokeSal pokesal1;
  private PokeSal pokesal2;
  private Treinador treinador1;
  private Treinador treinador2;
  private Batalha batalha;

  @BeforeEach
  void preparar() {
    pokesal1 = new PokeSal("UmSal", 100f, 20f, 10f, 20f, TipoElemental.FOGO);
    pokesal2 = new PokeSal("DoisSal", 100f, 20f, 10f, 10f, TipoElemental.AGUA);
    treinador1 = new Treinador("Ana");
    treinador1.escolherInicial(pokesal1);
    treinador2 = new Treinador("Bruno");
    treinador2.escolherInicial(pokesal2);
    batalha = new Batalha(new Confronto(treinador1, treinador2, Terreno.ASFALTO_QUENTE),
        executor, servicoTerreno, registro, repositorio, gerador);

    Golpe golpe = new Golpe("Golpe", TipoElemental.FOGO, 15f, 90f);
    when(estrategia.escolherGolpe(any(PokeSal.class))).thenReturn(golpe);
    when(executor.executar(any(PokeSal.class), any(PokeSal.class), any(Golpe.class),
        any(Terreno.class))).thenReturn(ResultadoAtaque.SEM_EFEITO);
  }

  @Test
  @DisplayName("Empate de HP no limite: o gerador mockado desempata a favor do treinador 2")
  public void testEmpateDeHpUsaGeradorParaDesempatar() {
    when(gerador.proximoDouble()).thenReturn(0.7);

    Treinador vencedor = batalha.executarAteOFim(estrategia);

    assertSame(treinador2, vencedor);
    verify(gerador, times(1)).proximoDouble();
    ArgumentCaptor<ResultadoBatalha> captor = ArgumentCaptor.forClass(ResultadoBatalha.class);
    verify(repositorio).salvar(captor.capture());
    assertEquals(Batalha.LIMITE_DE_RODADAS, captor.getValue().turnos());
    verify(registro, atLeastOnce()).registrarMensagem(contains("Limite de"));
  }

  @Test
  @DisplayName("Empate de HP no limite: sorteio baixo favorece o treinador 1")
  public void testEmpateDeHpComSorteioBaixo() {
    when(gerador.proximoDouble()).thenReturn(0.2);

    assertSame(treinador1, batalha.executarAteOFim(estrategia));
  }

  @Test
  @DisplayName("Com HP diferente no limite, vence quem tem mais HP e o gerador não é usado")
  public void testHpDiferenteDecideSemGerador() {
    pokesal2.setHpAtual(80f);

    Treinador vencedor = batalha.executarAteOFim(estrategia);

    assertSame(treinador1, vencedor);
    verify(gerador, never()).proximoDouble();
  }

  @Test
  @DisplayName("O percentual de HP (e não o HP bruto) decide quando os máximos são diferentes")
  public void testPercentualDeHpDecide() {
    PokeSal robusto = new PokeSal("RobustoSal", 200f, 20f, 10f, 5f, TipoElemental.PLANTA);
    robusto.setHpAtual(110f); // 55%
    Treinador treinadorRobusto = new Treinador("Carla");
    treinadorRobusto.escolherInicial(robusto);
    pokesal1.setHpAtual(60f); // 60%: menos HP bruto, mais percentual
    Batalha outra = new Batalha(new Confronto(treinador1, treinadorRobusto, Terreno.ASFALTO_QUENTE),
        executor, servicoTerreno, registro, repositorio, gerador);

    assertSame(treinador1, outra.executarAteOFim(estrategia));
    verify(gerador, never()).proximoDouble();
  }
}
