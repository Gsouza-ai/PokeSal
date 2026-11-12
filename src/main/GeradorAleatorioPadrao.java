package main;

import java.util.Random;

/**
 * Implementação de produção do gerador, baseada em java.util.Random.
 **/
public class GeradorAleatorioPadrao implements GeradorAleatorio {
  private final Random random;

  /**
   * Método construtor.
   *
   * @param random fonte de números aleatórios (pode ter semente fixa para reprodução).
   **/
  public GeradorAleatorioPadrao(Random random) {
    this.random = random;
  }

  @Override
  public double proximoDouble() {
    return random.nextDouble();
  }
}
