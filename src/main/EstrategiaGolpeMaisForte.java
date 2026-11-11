package main;

/**
 * Escolhe o golpe utilizável de maior poder base. Se nenhum puder ser usado,
 * escolhe o primeiro golpe da lista.
 **/
public class EstrategiaGolpeMaisForte implements EstrategiaDeAcao {

  @Override
  public Golpe escolherGolpe(PokeSal pokesal) {
    if (pokesal.getGolpes().isEmpty()) {
      throw new IllegalStateException(pokesal.getNome() + " não possui golpes.");
    }
    Golpe escolhido = pokesal.getGolpes().get(0);
    for (Golpe golpe : pokesal.getGolpes()) {
      boolean melhor = golpe.podeSerUsado()
          && (!escolhido.podeSerUsado() || golpe.getPoderBase() > escolhido.getPoderBase());
      if (melhor) {
        escolhido = golpe;
      }
    }
    return escolhido;
  }
}
