package main;

/**
 * Fonte de aleatoriedade do sistema. As regras de negócio nunca chamam
 * Math.random() nem new Random(): elas recebem uma implementação desta interface.
 **/
public interface GeradorAleatorio {

  /**
   * Sorteia um número no intervalo [0, 1).
   *
   * @return valor maior ou igual a 0 e menor que 1.
   **/
  double proximoDouble();
}
