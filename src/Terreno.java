/**
 * Classe terreno.
 **/
public enum Terreno {
  ASFALTO_QUENTE,
  POCA_CHUVA,
  CANTEIRO_CENTRAL;
  private static final double MULTIPLICADOR_ASFALTO = 1.15;
  private static final double MULTIPLICADOR_CHUVA = 1.10;

  /**
   * Verifica se o terreno é do mesmo tipo do pokesal e aplica o efeito ativo (dano).
   *
   * @param golpe passa o golpe dado.
   * @param danoBase passa o dano base.
   **/
  public double aplicarEfeitoAtivo(Golpe golpe, double danoBase) {
    if (this == ASFALTO_QUENTE && golpe.getTipo() == TipoElemental.FOGO) {
      return danoBase * MULTIPLICADOR_ASFALTO;
    }

    if (this == POCA_CHUVA && golpe.getTipo() == TipoElemental.AGUA) {
      return danoBase * MULTIPLICADOR_CHUVA;
    }
    return danoBase;
  }

  /**
   * Efeito passivo (regenera) acontece em cada pokesal tipo planta.
   *
   * @param pokesal passa o pokesal que receberá o efeito passivo.
   **/
  public void aplicarEfeitoPassivo(PokeSal pokesal) {
    if (this == CANTEIRO_CENTRAL && pokesal.getTipo() == TipoElemental.PLANTA) {
      double cura = pokesal.getHpMaximo() * 0.05;
      pokesal.curar(cura);
      System.out.println(pokesal.getNome() + " recuperou "
          + cura + " de HP com o Canteiro Central!");
    }
  }
}