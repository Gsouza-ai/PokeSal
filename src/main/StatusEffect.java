package main;
/**
 * Classe efeito status.
 **/
public enum StatusEffect {
  QUEIMADO,
  ENVENENADO,
  PARALISADO;

  /** Fração do HP máximo perdida por turno ao estar queimado. **/
  public static final float DANO_QUEIMADURA = 0.05f;
  /** Fração do HP máximo perdida por turno ao estar envenenado. **/
  public static final float DANO_VENENO = 0.08f;
  /** Multiplicador de ataque de um Pokésal queimado. **/
  public static final float MULTIPLICADOR_ATAQUE_QUEIMADO = 0.95f;
  /** Multiplicador de velocidade de um Pokésal paralisado. **/
  public static final float MULTIPLICADOR_VELOCIDADE_PARALISADO = 0.5f;

  /**
   * Método para aplicar o efeito do status no fim do turno. A redução de ataque (queimado)
   * e de velocidade (paralisado) é aplicada pelo PokeSal enquanto o status estiver ativo,
   * por isso não acumula a cada turno.
   *
   * @param pokesal passa o pokesal que receberá o efeito.
   * @return impacto em HP causado neste turno (0 se o status não causa dano direto).
   **/
  public float aplicarEfeito(PokeSal pokesal) {
    float dano = 0f;
    if (this == QUEIMADO) {
      dano = Math.round(pokesal.getHpMaximo() * DANO_QUEIMADURA);
    } else if (this == ENVENENADO) {
      dano = Math.round(pokesal.getHpMaximo() * DANO_VENENO);
    }
    if (dano > 0) {
      pokesal.receberDano(dano);
    }
    return dano;
  }
}
