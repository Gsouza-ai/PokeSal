package main;
/**
 * Classe tipo elemental.
 **/
public enum TipoElemental {
  AGUA,
  FOGO,
  PLANTA;

  private float danoSueperEfetivo = 2.0f;
  private float danoPoucoEfetivo = 0.5f;
  private float danoNormal = 1.0f;

  /**
   * Método que calcula o multiplicador de dano.
   *
   * @param defensor passa o tipo elemental do pokesal que vai tomar o dano.
   **/
  public float calcularMultiplicador(TipoElemental defensor) {
    // this ataca o defensor
    if (this == FOGO && defensor == PLANTA) {
      return danoSueperEfetivo;
    }

    if (this == FOGO && defensor == AGUA) {
      return danoPoucoEfetivo;
    }

    if (this == AGUA && defensor == FOGO) {
      return danoSueperEfetivo;
    }

    if (this == AGUA && defensor == PLANTA) {
      return danoPoucoEfetivo;
    }

    if (this == PLANTA && defensor == AGUA) {
      return danoSueperEfetivo;
    }

    if (this == PLANTA && defensor == FOGO) {
      return danoPoucoEfetivo;
    }
    // se não é nenhuma dessas combinações dano normal
    return danoNormal;
  }
}