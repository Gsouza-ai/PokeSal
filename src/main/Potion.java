package main;
/**
 * Classe poção.
 **/
public class Potion extends Item {

  /**
   * Método construtor.
   **/
  public Potion() {
    super("Potion");
  }

  @Override
  public void aplicarEfeito(PokeSal pokesal) {
    pokesal.curar(20); // cura 20 de HP
  }
}
