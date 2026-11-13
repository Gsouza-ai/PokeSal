package main;

import exception.RepositorioException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Requisito autoral RA-F3-02: ranking e seeding do torneio.
 * Cada vitória vale 3 pontos, mais 1 ponto de bônus se foi uma vitória rápida
 * (até {@value #TURNOS_VITORIA_RAPIDA} turnos). O histórico vem do RepositorioResultados.
 * Se o repositório falhar, o torneio segue com a ordem de inscrição.
 **/
public class ServicoRanking {
  static final int PONTOS_VITORIA = 3;
  static final int BONUS_VITORIA_RAPIDA = 1;
  static final int TURNOS_VITORIA_RAPIDA = 5;

  private final RepositorioResultados repositorio;
  private final RegistroBatalha registro;

  /**
   * Método construtor.
   *
   * @param repositorio histórico de resultados.
   * @param registro registro de eventos (usado para avisar falhas do repositório).
   **/
  public ServicoRanking(RepositorioResultados repositorio, RegistroBatalha registro) {
    this.repositorio = repositorio;
    this.registro = registro;
  }

  /**
   * Ordena os inscritos do melhor para o pior ranqueado (empates mantêm a ordem de inscrição).
   *
   * @param inscritos treinadores inscritos, na ordem de inscrição.
   * @return nova lista ordenada.
   **/
  public List<Treinador> ordenarPorRanking(List<Treinador> inscritos) {
    List<Treinador> ordenados = new ArrayList<>(inscritos);
    List<ResultadoBatalha> historico;
    try {
      historico = repositorio.listar();
    } catch (RepositorioException e) {
      registro.registrarErro("Falha ao ler o histórico; seeding pela ordem de inscrição", e);
      return ordenados;
    }
    Map<String, Integer> pontos = calcularPontos(historico);
    ordenados.sort((a, b) -> Integer.compare(
        pontos.getOrDefault(b.getNome(), 0), pontos.getOrDefault(a.getNome(), 0)));
    return ordenados;
  }

  /**
   * Calcula a pontuação de cada treinador (pelo nome) a partir do histórico.
   *
   * @param historico resultados já salvos.
   * @return mapa nome do treinador para pontos.
   **/
  public Map<String, Integer> calcularPontos(List<ResultadoBatalha> historico) {
    Map<String, Integer> pontos = new HashMap<>();
    for (ResultadoBatalha resultado : historico) {
      int ganho = PONTOS_VITORIA;
      if (resultado.turnos() <= TURNOS_VITORIA_RAPIDA) {
        ganho += BONUS_VITORIA_RAPIDA;
      }
      pontos.merge(resultado.vencedor().getNome(), ganho, Integer::sum);
    }
    return pontos;
  }
}
