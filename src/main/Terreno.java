package main;
/**
 * Classe terreno.
 **/
public enum Terreno {
  ASFALTO_QUENTE,
  POCA_CHUVA,
  CANTEIRO_CENTRAL;
  private static final float MULTIPLICADOR_ASFALTO = 1.15f;
  private static final float MULTIPLICADOR_CHUVA = 1.10f;

  /**
   * Verifica se o terreno é do mesmo tipo do pokesal e aplica o efeito ativo (dano).
   *
   * @param golpe passa o golpe dado.
   * @param dano passa o dano base.
   **/
  public float aplicarEfeitoAtivo(Golpe golpe, float dano) {
    if (this == ASFALTO_QUENTE && golpe.getTipo() == TipoElemental.FOGO) {
      return dano * MULTIPLICADOR_ASFALTO;
    }

    if (this == POCA_CHUVA && golpe.getTipo() == TipoElemental.AGUA) {
      return dano * MULTIPLICADOR_CHUVA;
    }
    return dano;
  }

  /**
   * Efeito passivo (regenera) acontece em cada pokesal tipo planta.
   *
   * @param pokesal passa o pokesal que receberá o efeito passivo.
   **/
  public void aplicarEfeitoPassivo(PokeSal pokesal) {
    if (this == CANTEIRO_CENTRAL && pokesal.getTipo() == TipoElemental.PLANTA) {
      float cura = Math.round(pokesal.getHpMaximo() * 0.05f);
      pokesal.curar(cura);
      System.out.println(pokesal.getNome() + " recuperou "
          + cura + " de HP com o Canteiro Central!");
    }
  }
}