package main;
/**
 * Classe super poção.
 **/
public class SuperPotion extends Item {

  /**
   * Método super poção.
   **/
  public SuperPotion() {
    super("Super Potion");
  }

  @Override
  public void aplicarEfeito(PokeSal pokesal) {
    pokesal.curar(50); // cura 50 de HP
    System.out.println(pokesal.getNome() + " usou Super Potion e curou 50 de HP!");
  }
}
