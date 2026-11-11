package main;

/**
 * Classe Tsunami.
 * Tsunami pode ser usado 1 vez por batalha e deixa o Pokesal sem atacar na rodada seguinte.
 * Causa dano fixo de 20% do HP máximo do defensor, tem precisão 100 e não causa crítico.
 **/
public class Tsunami extends Golpe {
  private static final float PERCENTUAL_DANO = 0.20f;
  private boolean jaUtilizadoNaBatalha;

  /**
   * Construtor do Tsunami.
   **/
  public Tsunami() {
    // nome do golpe, tipo água, poder base alto, precisão de 100%
    super("Tsunami", TipoElemental.AGUA, 40, 100f);
    this.jaUtilizadoNaBatalha = false;
  }

  public boolean isJaUtilizadoNaBatalha() {
    return jaUtilizadoNaBatalha;
  }

  @Override
  public float calcularDano(PokeSal atacante, PokeSal defensor) {
    return defensor.getHpMaximo() * PERCENTUAL_DANO;
  }

  @Override
  public boolean podeSerUsado() {
    return !jaUtilizadoNaBatalha;
  }

  @Override
  public boolean permiteCritico() {
    return false;
  }

  @Override
  public void aposAcertar(PokeSal atacante) {
    jaUtilizadoNaBatalha = true;
    // o Pokesal fica exausto e não ataca na próxima rodada
    atacante.setExaustoTurnoAtual(true);
  }

  @Override
  public void reiniciarParaNovaBatalha() {
    jaUtilizadoNaBatalha = false;
  }
}
