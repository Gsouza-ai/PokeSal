package main;
import exception.LimiteDeItensExcedidoException;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe mochila.
 **/
public class Mochila {
  private List<Item> itens;
  private int limitePorBatalha;

  /**
   * Método construtor.
   **/
  public Mochila() {
    this.itens = new ArrayList<Item>();
    this.limitePorBatalha = 2;
  }

  public List<Item> getItens() {
    return itens;
  }

  /**
   * Adicionar item na mochila.
   *
   * @param item adiciona o item.
   **/
  public void adicionarItem(Item item) {
    itens.add(item);
  }

  /**
   * Remover item na mochila.
   *
   * @param item remove o item.
   **/
  public void removerItem(Item item) {
    itens.remove(item);
  }

  /**
   * Verifica se ainda dá pra usar o item.
   *
   * @param rodadaAtual mostra a rodada atual.
   * @param itensUsadosNaBatalha mostra os itens utilizados na batalha.
   * @throws LimiteDeItensExcedidoException se o limite de itens por batalha já foi atingido.
   **/
  public boolean podeUsarItem(int rodadaAtual, int itensUsadosNaBatalha) {
    if (rodadaAtual < 2) {
      return false;
    }
    if (itensUsadosNaBatalha >= limitePorBatalha) {
      throw new LimiteDeItensExcedidoException("Limite de itens por batalha já foi atingido!");
    }
    return true;
  }
}
