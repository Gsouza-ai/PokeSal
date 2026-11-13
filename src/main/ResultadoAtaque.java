package main;

/**
 * Resultado de um golpe executado pelo ExecutorDeGolpe.
 *
 * @param dano dano final aplicado ao alvo (0 se errou).
 * @param acertou indica se o golpe acertou o alvo.
 * @param critico indica se foi acerto crítico.
 **/
public record ResultadoAtaque(float dano, boolean acertou, boolean critico) {

  /** Resultado de um golpe que errou ou não pôde ser usado. **/
  public static final ResultadoAtaque SEM_EFEITO = new ResultadoAtaque(0f, false, false);
}
