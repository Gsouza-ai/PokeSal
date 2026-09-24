package main;
/**
 * Classe antídoto.
 **/
public class Antidote extends Item {

  /**
   * Método construtor.
   **/
  public Antidote() {
    super("Antidote");
  }

  @Override
  public void aplicarEfeito(PokeSal pokesal) {
    pokesal.limparStatus(); // remove todos os status (queimado, envenenado, paralisado)
    System.out.println(pokesal.getNome() + " usou Antidote e removeu os status negativos!");
  }
}
