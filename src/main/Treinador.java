package main;

import exception.LimiteDeItensExcedidoException;

/**
 * Classe treinador.
 **/
public class Treinador {
  private String nomeTreinador;
  private PokeSal pokesalInicial;
  private Mochila mochila;
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
    System.out.println(nomeTreinador + " escolheu " + pokesal.getNome() + " como Pokesal inicial!");
  }

  /**
   * Usa um item da mochila na rodadaAtual informada.
   *
   * @param item passa o item da mochila.
   * @param rodadaAtual mostra qual a rodada atual.
   **/
  public void usarItem(Item item, int rodadaAtual) {
    try {
      boolean podeUsarItem = mochila.podeUsarItem(rodadaAtual, itensUsadosNaBatalha);
      if (!podeUsarItem) {
        System.out.println("Itens só podem ser usados a partir da 2ª rodada!");
        return;
      }
      item.aplicarEfeito(pokesalInicial);
      itensUsadosNaBatalha++;
    } catch (LimiteDeItensExcedidoException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Método que permite o treinador se esquivar do ataque.
   **/
  public boolean esquivar() {
    if (usouEsquivaNaRodada) {
      System.out.println(nomeTreinador + " já esquivou nesta rodada!");
      return false;
    }
    usouEsquivaNaRodada = true;
    System.out.println(nomeTreinador + " esquivou do ataque!");
    return true;
  }

  /**
   * Reinicia a permissão da esquiva.
   **/
  public void reiniciarEsquiva() {
    usouEsquivaNaRodada = false;
  }
}
