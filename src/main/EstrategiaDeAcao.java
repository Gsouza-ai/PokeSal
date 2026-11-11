package main;

/**
 * Decide qual golpe um Pokésal usa em cada turno (usado nas batalhas automáticas).
 **/
public interface EstrategiaDeAcao {

  /**
   * Escolhe o golpe do turno.
   *
   * @param pokesal Pokésal que vai atacar.
   * @return golpe escolhido.
   **/
  Golpe escolherGolpe(PokeSal pokesal);
}
