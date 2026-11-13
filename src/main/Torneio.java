package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Torneio eliminatório de 4 ou 8 treinadores. O seeding vem do ServicoRanking
 * (1º x último, 2º x penúltimo, de modo que os melhores só se enfrentem no fim) e cada
 * confronto é disputado por uma Batalha criada pela FabricaDeBatalha.
 **/
public class Torneio {
  private final FabricaDeBatalha fabrica;
  private final EstrategiaDeAcao estrategia;
  private final ServicoRanking ranking;
  private final RegistroBatalha registro;

  /**
   * Método construtor.
   *
   * @param fabrica cria as batalhas.
   * @param estrategia escolhe os golpes nas batalhas automáticas.
   * @param ranking define o seeding da primeira fase.
   * @param registro recebe as mensagens do torneio.
   **/
  public Torneio(FabricaDeBatalha fabrica, EstrategiaDeAcao estrategia, ServicoRanking ranking,
      RegistroBatalha registro) {
    this.fabrica = fabrica;
    this.estrategia = estrategia;
    this.ranking = ranking;
    this.registro = registro;
  }

  /**
   * Disputa o torneio e devolve o campeão.
   *
   * @param inscritos 4 ou 8 treinadores.
   * @return treinador campeão.
   * @throws IllegalArgumentException se não houver 4 ou 8 inscritos.
   **/
  public Treinador disputar(List<Treinador> inscritos) {
    if (inscritos.size() != 4 && inscritos.size() != 8) {
      throw new IllegalArgumentException("O torneio exige 4 ou 8 treinadores.");
    }
    List<Treinador> fase = montarChaveamento(ranking.ordenarPorRanking(inscritos));

    while (fase.size() > 1) {
      registro.registrarMensagem("##### " + nomeDaFase(fase.size()) + " #####");
      List<Treinador> vencedores = new ArrayList<>();
      for (int i = 0; i < fase.size(); i += 2) {
        vencedores.add(disputarConfronto(fase.get(i), fase.get(i + 1)));
      }
      fase = vencedores;
    }
    Treinador campeao = fase.get(0);
    registro.registrarMensagem("Campeão do torneio: " + campeao.getNome() + "!");
    return campeao;
  }

  private Treinador disputarConfronto(Treinador a, Treinador b) {
    a.prepararParaNovaBatalha();
    b.prepararParaNovaBatalha();
    Batalha batalha = fabrica.criar(a, b);
    return batalha.executarAteOFim(estrategia);
  }

  /**
   * Monta a ordem dos confrontos a partir do ranking (posições vizinhas se enfrentam).
   * Para 8 treinadores: 1x8, 4x5, 2x7, 3x6. Para 4: 1x4, 2x3.
   **/
  private List<Treinador> montarChaveamento(List<Treinador> ranqueados) {
    List<Integer> posicoes = new ArrayList<>(List.of(1));
    while (posicoes.size() < ranqueados.size()) {
      List<Integer> proxima = new ArrayList<>();
      int total = posicoes.size() * 2 + 1;
      for (int posicao : posicoes) {
        proxima.add(posicao);
        proxima.add(total - posicao);
      }
      posicoes = proxima;
    }
    List<Treinador> chave = new ArrayList<>();
    for (int posicao : posicoes) {
      chave.add(ranqueados.get(posicao - 1));
    }
    return chave;
  }

  private String nomeDaFase(int participantes) {
    if (participantes == 2) {
      return "Final";
    }
    if (participantes == 4) {
      return "Semifinais";
    }
    return "Quartas de final";
  }
}
