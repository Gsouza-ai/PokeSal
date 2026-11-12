package main;

import java.io.PrintStream;

/**
 * Registro de batalha que escreve os eventos em um PrintStream (ex.: System.out).
 **/
public class RegistroBatalhaConsole implements RegistroBatalha {
  private final PrintStream saida;

  /**
   * Método construtor.
   *
   * @param saida destino das mensagens.
   **/
  public RegistroBatalhaConsole(PrintStream saida) {
    this.saida = saida;
  }

  @Override
  public void registrarAtaque(PokeSal atacante, PokeSal alvo, Golpe golpe) {
    saida.println(atacante.getNome() + " usou " + golpe.getNome() + " em " + alvo.getNome() + "!");
  }

  @Override
  public void registrarDano(PokeSal alvo, float dano, boolean critico) {
    if (dano <= 0) {
      saida.println("  " + alvo.getNome() + " não sofreu dano (golpe errou ou foi anulado).");
      return;
    }
    String complemento = critico ? " Acerto crítico!" : "";
    saida.println("  " + alvo.getNome() + " sofreu " + dano + " de dano!" + complemento);
  }

  @Override
  public void registrarEfeitoStatus(PokeSal pokesal, StatusEffect efeito, float impacto) {
    saida.println(pokesal.getNome() + " sofreu o efeito " + efeito
        + " (impacto: " + impacto + ").");
  }

  @Override
  public void registrarEfeitoTerreno(Terreno terreno, PokeSal pokesal, float cura) {
    saida.println(pokesal.getNome() + " recuperou " + cura
        + " de HP com o terreno " + terreno + ".");
  }

  @Override
  public void registrarUsoItem(Treinador treinador, Item item) {
    saida.println(treinador.getNome() + " usou o item " + item.getNome() + ".");
  }

  @Override
  public void registrarMensagem(String mensagem) {
    saida.println(mensagem);
  }

  @Override
  public void registrarErro(String mensagem, Throwable causa) {
    saida.println("[ERRO] " + mensagem + ": " + causa.getMessage());
  }
}
