package main;

import java.util.List;

/**
 * Resultado de uma batalha, salvo no RepositorioResultados.
 *
 * @param vencedor treinador vencedor.
 * @param perdedor treinador perdedor.
 * @param turnos número de turnos disputados.
 * @param pokesalUtilizados Pokésal que participaram da batalha.
 **/
public record ResultadoBatalha(Treinador vencedor, Treinador perdedor, int turnos,
    List<PokeSal> pokesalUtilizados) {
}
