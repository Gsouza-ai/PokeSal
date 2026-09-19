/**
 * Classe Tsunami.
 * Tsunami pode ser usado 1 vez por batalha e deixa o Pokesal sem atacar na rodada seguinte.
 **/
public class Tsunami extends Golpe {
  private boolean jaUtilizadoNaBatalha;

  /**
   * Construtor do Tsunami.
   **/
  public Tsunami() {
    // nome do golpe, tipo água, poder base alto, precisão de 100%
    super("Tsunami", TipoElemental.AGUA, 40, 1.0f);
    this.jaUtilizadoNaBatalha = false;
  }

  public boolean isJaUtilizadoNaBatalha() {
    return jaUtilizadoNaBatalha;
  }

  @Override
  public float executar(PokeSal atacante, PokeSal defensor, Terreno terreno) {
    if (jaUtilizadoNaBatalha) {
      System.out.println("Tsunami já foi usado nesta batalha! " + atacante.getNome()
          + " não pode usar de novo.");
      return 0;
    }

    // dano de 20% do HP máximo do defensor
    float danoTsunami = defensor.getHpMaximo() * 0.20f;

    // efeito do terreno também pode alterar o dano
    if (terreno != null) {
      danoTsunami = terreno.aplicarEfeitoAtivo(this, danoTsunami);
    }

    defensor.receberDano(danoTsunami);
    jaUtilizadoNaBatalha = true;

    // o Pokesal fica exausto e não ataca na próxima rodada
    atacante.setExaustoTurnoAtual(true);

    System.out.println(atacante.getNome() + " usou TSUNAMI em " + defensor.getNome()
        + " e causou " + danoTsunami + " de dano! Agora ficará exausto na próxima rodada.");

    return danoTsunami;
  }
}
