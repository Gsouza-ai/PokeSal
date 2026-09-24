package main;
/**
 * Classe golpe.
 **/
public class Golpe {
  private String nomeDoGolpe;
  private TipoElemental tipoElemental;
  private float poderBase;
  private float precisao;

  /**
   * Método construtor.
   *
   * @param nomeDoGolpe passa o nome do golpe.
   * @param tipoElemental passa o tipo elemental do golpe.
   * @param poderBase passa o valor do poder base.
   * @param precisao passa o valor da precisão do golpe.
   **/
  public Golpe(String nomeDoGolpe, TipoElemental tipoElemental, float poderBase, float precisao) {
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
   * Executa o golpe e devolve o dano final causado.
   * A classe tsunami sobrescreve esse metodo.
   *
   * @param atacante passa o pokesal que irá atacar.
   * @param defensor passa o pokesal que irá se defender.
   * @param terreno passa o terreno.
   **/
  public float executar(PokeSal atacante, PokeSal defensor, Terreno terreno) {
    // Math.random retorna double, precisa converter pra float
    float sorteioPrecisao = (float) Math.random();
    if (sorteioPrecisao > precisao) {
      System.out.println(atacante.getNome() + " usou " + nomeDoGolpe + ", mas errou o golpe!");
      return 0;
    }

    // calcula o dano base
    float dano = poderBase + atacante.getAtk() - defensor.getDef();
    if (dano < 0) {
      dano = 0;
    }

    // aplica a vantagem elemental
    float multiplicador = tipoElemental.calcularMultiplicador(defensor.getTipo());
    dano = dano * multiplicador;

    // aplica o efeito do terreno
    if (terreno != null) {
      dano = terreno.aplicarEfeitoAtivo(this, dano);
    }

    // arredonda pra ter valores "inteiros" de dano, como num jogo de verdade
    dano = Math.round(dano);

    // aplica o dano no defensor
    defensor.receberDano(dano);
    System.out.println(atacante.getNome() + " usou " + nomeDoGolpe + " em " + defensor.getNome()
        + " e causou " + dano + " de dano!");

    return dano;
  }
}