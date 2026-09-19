/**
 * Classe efeito status.
 **/
public enum StatusEffect {
  QUEIMADO,
  ENVENENADO,
  PARALISADO;

  /**
   * Método para aplicar o status de efeito no pokesal.
   *
   * @param pokesal passa o pokesal que receberá o efeito.
   **/
  public void aplicarEfeito(PokeSal pokesal) {
    if (this == QUEIMADO) {
      // queimado perde HP e tem o ataque reduzido em 5%
      double danoQueimadura = pokesal.getHpMaximo() * 0.05;
      pokesal.receberDano(danoQueimadura);
      System.out.println(pokesal.getNome() + " sofreu "
          + danoQueimadura + " de dano da queimadura!");
    }

    if (this == ENVENENADO) {
      double danoVeneno = pokesal.getHpMaximo() * 0.08; // veneno tira um dano progressivo de 8%
      pokesal.receberDano(danoVeneno);
      System.out.println(pokesal.getNome() + " sofreu " + danoVeneno + " de dano do veneno!");
    }

    if (this == PARALISADO) {
      pokesal.setSpd(pokesal.getSpd() * 0.5); // paralisado reduz a velocidade em 50%
      System.out.println(pokesal.getNome() + " está paralisado e ficou mais lento!");
    }
  }
}
