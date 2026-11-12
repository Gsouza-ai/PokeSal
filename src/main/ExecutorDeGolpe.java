package main;

/**
 * Resolve a execução de um golpe: precisão, dano, efeito do terreno e acerto crítico.
 * Toda a aleatoriedade vem do GeradorAleatorio recebido pelo construtor.
 **/
public class ExecutorDeGolpe {
  public static final double CHANCE_CRITICO = 0.0625;
  public static final float MULTIPLICADOR_CRITICO = 1.5f;
  private static final double ESCALA_PRECISAO = 100.0;

  private final GeradorAleatorio gerador;
  private final ServicoTerreno servicoTerreno;

  /**
   * Método construtor.
   *
   * @param gerador fonte de aleatoriedade.
   * @param servicoTerreno serviço que aplica o efeito do terreno.
   **/
  public ExecutorDeGolpe(GeradorAleatorio gerador, ServicoTerreno servicoTerreno) {
    this.gerador = gerador;
    this.servicoTerreno = servicoTerreno;
  }

  /**
   * Executa o golpe. Primeiro sorteio: precisão (acerta se sorteio x 100 &lt; precisão).
   * Segundo sorteio, só se acertou e o golpe permite: acerto crítico.
   *
   * @param atacante Pokésal que ataca.
   * @param defensor Pokésal que recebe o golpe.
   * @param golpe golpe usado.
   * @param terreno terreno da batalha.
   * @return resultado do ataque.
   **/
  public ResultadoAtaque executar(PokeSal atacante, PokeSal defensor, Golpe golpe,
      Terreno terreno) {
    if (!golpe.podeSerUsado()) {
      return ResultadoAtaque.SEM_EFEITO;
    }

    double sorteioPrecisao = gerador.proximoDouble() * ESCALA_PRECISAO;
    if (sorteioPrecisao >= golpe.getPrecisao()) {
      return ResultadoAtaque.SEM_EFEITO;
    }

    float dano = golpe.calcularDano(atacante, defensor);
    dano = servicoTerreno.aplicarEfeitoAtivo(terreno, golpe, dano);

    boolean critico = golpe.permiteCritico() && gerador.proximoDouble() < CHANCE_CRITICO;
    if (critico) {
      dano = dano * MULTIPLICADOR_CRITICO;
    }

    dano = Math.round(dano);
    defensor.receberDano(dano);
    golpe.aposAcertar(atacante);
    return new ResultadoAtaque(dano, true, critico);
  }
}
