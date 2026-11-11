package main;

/**
 * Define quem se enfrenta e em qual terreno.
 *
 * @param treinador1 primeiro treinador.
 * @param treinador2 segundo treinador.
 * @param terreno terreno da batalha.
 **/
public record Confronto(Treinador treinador1, Treinador treinador2, Terreno terreno) {
}
