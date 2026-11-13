package main;

import java.util.List;

/**
 * Armazena os resultados das batalhas do torneio.
 **/
public interface RepositorioResultados {

  /**
   * Salva um resultado.
   *
   * @param resultado resultado da batalha.
   * @throws exception.RepositorioException se não for possível salvar.
   **/
  void salvar(ResultadoBatalha resultado);

  /**
   * Lista todos os resultados já salvos.
   *
   * @throws exception.RepositorioException se não for possível ler.
   **/
  List<ResultadoBatalha> listar();
}
