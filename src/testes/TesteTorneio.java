package testes;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import main.Batalha;
import main.EstrategiaDeAcao;
import main.FabricaDeBatalha;
import main.RegistroBatalha;
import main.ServicoRanking;
import main.Torneio;
import main.Treinador;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes do Torneio. A fábrica de batalhas, a estratégia, o ranking e o registro são mockados;
 * cada Batalha também é um mock, e seu resultado (quem venceu) é definido pelo teste.
 */
@ExtendWith(MockitoExtension.class)
public class TesteTorneio {

  @Mock
  private FabricaDeBatalha fabrica;

  @Mock
  private EstrategiaDeAcao estrategia;

  @Mock
  private ServicoRanking ranking;

  @Mock
  private RegistroBatalha registro;

  @InjectMocks
  private Torneio torneio;

  private static Treinador treinador(String nome) {
    return new Treinador(nome);
  }

  /** Programa a fábrica para devolver uma batalha cujo vencedor já é conhecido. */
  private void batalhaComVencedor(Treinador a, Treinador b, Treinador vencedor) {
    Batalha batalha = mock(Batalha.class);
    // doReturn().when() porque o strict stubs reclama de when() repetido com argumentos diferentes
    doReturn(batalha).when(fabrica).criar(a, b);
    doReturn(vencedor).when(batalha).executarAteOFim(estrategia);
  }

  @Test
  @DisplayName("Resultados de batalha mockados determinam quem avança de fase (4 treinadores)")
  public void testChaveamentoTorneioAvancaVencedores() {
    Treinador t1 = treinador("T1");
    Treinador t2 = treinador("T2");
    Treinador t3 = treinador("T3");
    Treinador t4 = treinador("T4");
    List<Treinador> inscritos = List.of(t1, t2, t3, t4);
    when(ranking.ordenarPorRanking(inscritos)).thenReturn(inscritos);
    batalhaComVencedor(t1, t4, t4); // zebra na semifinal 1
    batalhaComVencedor(t2, t3, t2);
    batalhaComVencedor(t4, t2, t4); // final entre os vencedores das semifinais

    Treinador campeao = torneio.disputar(inscritos);

    assertSame(t4, campeao);
    InOrder ordem = inOrder(fabrica);
    ordem.verify(fabrica).criar(t1, t4);
    ordem.verify(fabrica).criar(t2, t3);
    ordem.verify(fabrica).criar(t4, t2);
    verify(fabrica, times(3)).criar(any(Treinador.class), any(Treinador.class));
  }

  @Test
  @DisplayName("Com 8 treinadores o chaveamento é 1x8, 4x5, 2x7 e 3x6")
  public void testChaveamentoDeOitoTreinadores() {
    Treinador t1 = treinador("T1");
    Treinador t2 = treinador("T2");
    Treinador t3 = treinador("T3");
    Treinador t4 = treinador("T4");
    Treinador t5 = treinador("T5");
    Treinador t6 = treinador("T6");
    Treinador t7 = treinador("T7");
    Treinador t8 = treinador("T8");
    List<Treinador> inscritos = List.of(t1, t2, t3, t4, t5, t6, t7, t8);
    when(ranking.ordenarPorRanking(inscritos)).thenReturn(inscritos);
    // os mais bem ranqueados vencem sempre
    batalhaComVencedor(t1, t8, t1);
    batalhaComVencedor(t4, t5, t4);
    batalhaComVencedor(t2, t7, t2);
    batalhaComVencedor(t3, t6, t3);
    batalhaComVencedor(t1, t4, t1);
    batalhaComVencedor(t2, t3, t2);
    batalhaComVencedor(t1, t2, t1);

    assertSame(t1, torneio.disputar(inscritos));

    verify(fabrica, times(7)).criar(any(Treinador.class), any(Treinador.class));
  }

  @Test
  @DisplayName("Torneio só aceita 4 ou 8 treinadores")
  public void testNumeroInvalidoDeTreinadores() {
    List<Treinador> seis = List.of(treinador("A"), treinador("B"), treinador("C"),
        treinador("D"), treinador("E"), treinador("F"));

    assertThrows(IllegalArgumentException.class, () -> torneio.disputar(seis));

    verify(fabrica, never()).criar(any(Treinador.class), any(Treinador.class));
  }
}
