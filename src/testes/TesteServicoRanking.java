package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import exception.RepositorioException;
import java.util.List;
import java.util.Map;
import main.RegistroBatalha;
import main.RepositorioResultados;
import main.ResultadoBatalha;
import main.ServicoRanking;
import main.Treinador;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Requisito autoral RA-F3-02: Ranking e Seeding do Torneio.
 * Vitória = 3 pontos, +1 se foi vitória rápida (até 5 turnos). Colaborador mockado:
 * RepositorioResultados (histórico); RegistroBatalha recebe o aviso de falha.
 */
@ExtendWith(MockitoExtension.class)
public class TesteServicoRanking {

  @Mock
  private RepositorioResultados repositorio;

  @Mock
  private RegistroBatalha registro;

  @InjectMocks
  private ServicoRanking servico;

  private final Treinador ana = new Treinador("Ana");
  private final Treinador bruno = new Treinador("Bruno");
  private final Treinador carla = new Treinador("Carla");
  private final Treinador diego = new Treinador("Diego");

  private ResultadoBatalha vitoria(Treinador vencedor, Treinador perdedor, int turnos) {
    return new ResultadoBatalha(vencedor, perdedor, turnos, List.of());
  }

  @Test
  @DisplayName("O histórico do repositório mockado define a ordem do seeding")
  public void testRankingOrdenaPorPontosDoHistorico() {
    when(repositorio.listar()).thenReturn(List.of(
        vitoria(bruno, carla, 3),   // 4 pontos (rápida)
        vitoria(ana, diego, 7),     // 3 pontos
        vitoria(ana, carla, 4)));   // 4 pontos -> Ana soma 7

    List<Treinador> ordenados = servico.ordenarPorRanking(List.of(diego, carla, bruno, ana));

    // Ana 7, Bruno 4; Diego e Carla têm 0 e mantêm a ordem de inscrição (Diego antes de Carla)
    assertEquals(List.of(ana, bruno, diego, carla), ordenados);
  }

  @Test
  @DisplayName("Falha ao ler o histórico mantém a ordem de inscrição e registra o erro")
  public void testFalhaNoRepositorioMantemOrdemDeInscricao() {
    RepositorioException falha = new RepositorioException("repositório indisponível");
    when(repositorio.listar()).thenThrow(falha);
    List<Treinador> inscritos = List.of(diego, carla, bruno, ana);

    List<Treinador> ordenados = servico.ordenarPorRanking(inscritos);

    assertEquals(inscritos, ordenados);
    ArgumentCaptor<Throwable> causa = ArgumentCaptor.forClass(Throwable.class);
    verify(registro).registrarErro(anyString(), causa.capture());
    assertEquals("repositório indisponível", causa.getValue().getMessage());
  }

  @Test
  @DisplayName("Vitória em até 5 turnos ganha bônus; em 6 turnos não")
  public void testBonusDeVitoriaRapida() {
    Map<String, Integer> pontos = servico.calcularPontos(List.of(
        vitoria(ana, diego, 5),
        vitoria(bruno, diego, 6)));

    assertEquals(4, pontos.get("Ana"));
    assertEquals(3, pontos.get("Bruno"));
  }

  @Test
  @DisplayName("Sem histórico, ninguém tem pontos e a ordem de inscrição é mantida")
  public void testSemHistorico() {
    when(repositorio.listar()).thenReturn(List.of());
    List<Treinador> inscritos = List.of(ana, bruno, carla, diego);

    assertEquals(inscritos, servico.ordenarPorRanking(inscritos));
  }
}
