package main;

/**
 * Classe golpe. Guarda os dados do golpe e o cálculo do dano bruto; a sorte
 * (precisão e crítico) e o terreno ficam no ExecutorDeGolpe.
 **/
public class Golpe {
  private final String nomeDoGolpe;
  private final TipoElemental tipoElemental;
  private final float poderBase;
  private final float precisao;

  /**
   * Método construtor.
   *
   * @param nomeDoGolpe passa o nome do golpe.
   * @param tipoElemental passa o tipo elemental do golpe.
   * @param poderBase passa o valor do poder base.
   * @param precisao passa a precisão do golpe, de 0 a 100.
   * @throws IllegalArgumentException se a precisão estiver fora do intervalo de 0 a 100.
   **/
  public Golpe(String nomeDoGolpe, TipoElemental tipoElemental, float poderBase, float precisao) {
    if (precisao < 0 || precisao > 100) {
      throw new IllegalArgumentException("A precisão deve estar entre 0 e 100.");
    }
    this.nomeDoGolpe = nomeDoGolpe;
    this.tipoElemental = tipoElemental;
    this.poderBase = poderBase;
    this.precisao = precisao;
  }

  public String getNome() {
    return nomeDoGolpe;
  }

  public TipoElemental getTipo() {
    return tipoElemental;
  }

  public float getPoderBase() {
    return poderBase;
  }

  public float getPrecisao() {
    return precisao;
  }

  /**
   * Calcula o dano antes do terreno e do crítico: poder + ataque - defesa (mínimo 0),
   * multiplicado pela vantagem elemental. Golpes especiais sobrescrevem este método.
   *
   * @param atacante Pokésal que ataca.
   * @param defensor Pokésal que recebe o golpe.
   * @return dano calculado.
   **/
  public float calcularDano(PokeSal atacante, PokeSal defensor) {
    float dano = poderBase + atacante.getAtk() - defensor.getDef();
    if (dano < 0) {
      dano = 0;
    }
    return dano * tipoElemental.calcularMultiplicador(defensor.getTipo());
  }

  /**
   * Indica se o golpe pode ser usado agora (golpes especiais podem ter limite de uso).
   **/
  public boolean podeSerUsado() {
    return true;
  }

  /**
   * Indica se o golpe pode causar acerto crítico.
   **/
  public boolean permiteCritico() {
    return true;
  }

  /**
   * Gancho chamado depois que o golpe acertou o alvo (efeitos colaterais no atacante).
   *
   * @param atacante Pokésal que atacou.
   **/
  public void aposAcertar(PokeSal atacante) {
    // golpes comuns não têm efeito colateral
  }

  /**
   * Restaura o estado do golpe para uma nova batalha.
   **/
  public void reiniciarParaNovaBatalha() {
    // golpes comuns não guardam estado entre batalhas
  }
}
