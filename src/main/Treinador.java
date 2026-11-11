package main;

import exception.LimiteDeItensExcedidoException;

/**
 * Classe treinador.
 **/
public class Treinador {
  private final String nomeTreinador;
  private PokeSal pokesalInicial;
  private final Mochila mochila;
  private boolean usouEsquivaNaRodada;
  private int itensUsadosNaBatalha;

  /**
   * Método construtor.
   *
   * @param nomeTreinador nome do treinador.
   **/
  public Treinador(String nomeTreinador) {
    this.nomeTreinador = nomeTreinador;
    this.mochila = new Mochila();
    this.usouEsquivaNaRodada = false;
    this.itensUsadosNaBatalha = 0;
  }

  public String getNome() {
    return nomeTreinador;
  }

  public PokeSal getPokesalInicial() {
    return pokesalInicial;
  }

  public Mochila getMochila() {
    return mochila;
  }

  public int getItensUsadosNaBatalha() {
    return itensUsadosNaBatalha;
  }

  /**
   * O treinador escolhe o Pokesal inicial.
   *
   * @param pokesal passa o pokesal escolhido.
   **/
  public void escolherInicial(PokeSal pokesal) {
    this.pokesalInicial = pokesal;
  }

  /**
   * Usa um item da mochila na rodadaAtual informada. O item usado sai da mochila.
   *
   * @param item passa o item da mochila.
   * @param rodadaAtual mostra qual a rodada atual.
   * @return true se o item foi usado; false se a regra de uso bloqueou (antes da 2ª rodada
   *     ou limite de itens por batalha atingido).
   **/
  public boolean usarItem(Item item, int rodadaAtual) {
    try {
      if (!mochila.podeUsarItem(rodadaAtual, itensUsadosNaBatalha)) {
        return false;
      }
    } catch (LimiteDeItensExcedidoException e) {
      // limite atingido: o uso é bloqueado, mas a batalha segue normalmente
      return false;
    }
    item.aplicarEfeito(pokesalInicial);
    mochila.removerItem(item);
    itensUsadosNaBatalha++;
    return true;
  }

  /**
   * Método que permite o treinador se esquivar do ataque.
   **/
  public boolean esquivar() {
    if (usouEsquivaNaRodada) {
      return false;
    }
    usouEsquivaNaRodada = true;
    return true;
  }

  /**
   * Reinicia a permissão da esquiva.
   **/
  public void reiniciarEsquiva() {
    usouEsquivaNaRodada = false;
  }

  /**
   * Prepara o treinador para uma nova batalha: zera o contador de itens, a esquiva
   * e restaura o Pokesal inicial (se houver).
   **/
  public void prepararParaNovaBatalha() {
    itensUsadosNaBatalha = 0;
    usouEsquivaNaRodada = false;
    if (pokesalInicial != null) {
      pokesalInicial.restaurarParaNovaBatalha();
    }
  }
}
