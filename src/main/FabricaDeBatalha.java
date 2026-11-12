package main;

/**
 * Cria batalhas entre dois treinadores (permite ao Torneio não dar new em Batalha).
 **/
public interface FabricaDeBatalha {

  /**
   * Cria uma batalha.
   *
   * @param treinador1 primeiro treinador.
   * @param treinador2 segundo treinador.
   * @return batalha pronta para ser disputada.
   **/
  Batalha criar(Treinador treinador1, Treinador treinador2);
}
