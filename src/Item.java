/**
 * Classe item.
 **/
public abstract class Item {
  private String nomeItem;

  /**
   * Método construtor.
   *
   * @param nomeItem passa o nome do item.
   **/
  public Item(String nomeItem) {
    this.nomeItem = nomeItem;
  }

  public String getNome() {
    return nomeItem;
  }

  /**
   * Cada item concreto decide o que acontece quando é usado.
   **/
  public abstract void aplicarEfeito(PokeSal pokesal);
}
