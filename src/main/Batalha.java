package main;
/**
 * Classe batalha.
 **/
public class Batalha {
  private Treinador treinador1;
  private Treinador treinador2;
  private Terreno terreno;
  private int rodadaAtual;

  /**
   * Método construtor.
   *
   * @param treinador1 passa o treinador 1.
   * @param treinador2 passa o treinador 2.
   * @param terreno passa o terreno que a batalha irá ocorrer.
   **/
  public Batalha(Treinador treinador1, Treinador treinador2, Terreno terreno) {
    this.treinador1 = treinador1;
    this.treinador2 = treinador2;
    this.terreno = terreno;
    this.rodadaAtual = 1;
  }

  public int getRodadaAtual() {
    return rodadaAtual;
  }

  public Terreno getTerreno() {
    return terreno;
  }

  /**
   * Anuncia o começo da batalha.
   **/
  public void iniciarBatalha() {
    System.out.println("=== Batalha iniciada! ===");
    System.out.println(treinador1.getNome() + " (" + treinador1.getPokesalInicial().getNome() + ")"
        + " VS " + treinador2.getNome() + " (" + treinador2.getPokesalInicial().getNome() + ")");
    System.out.println("Terreno: " + terreno);
  }

  /**
   * Decide quem ataca primeiro, olhando o spd de cada pokesal.
   **/
  public PokeSal determinarIniciativa() {
    PokeSal pokesal1 = treinador1.getPokesalInicial();
    PokeSal pokesal2 = treinador2.getPokesalInicial();

    if (pokesal1.getSpd() >= pokesal2.getSpd()) {
      return pokesal1;
    } else {
      return pokesal2;
    }
  }

  /**
   * Executa uma rodada simples onde quem tem mais spd ataca primeiro com o golpe indicado.
   **/
  public void proximoTurno(Golpe golpeTreinador1, Golpe golpeTreinador2) {
    PokeSal pokesal1 = treinador1.getPokesalInicial();
    PokeSal pokesal2 = treinador2.getPokesalInicial();

    System.out.println("\n--- Rodada " + rodadaAtual + " ---");

    PokeSal primeiro = determinarIniciativa();

    if (primeiro == pokesal1) {
      pokesal1.atacar(pokesal2, golpeTreinador1, terreno);
      if (pokesal2.estaVivo()) {
        pokesal2.atacar(pokesal1, golpeTreinador2, terreno);
      }
    } else {
      pokesal2.atacar(pokesal1, golpeTreinador2, terreno);
      if (pokesal1.estaVivo()) {
        pokesal1.atacar(pokesal2, golpeTreinador1, terreno);
      }
    }

    aplicarEfeitosFimDeTurno();

    // reseta coisas que valem só para 1 rodada
    treinador1.reiniciarEsquiva();
    treinador2.reiniciarEsquiva();

    rodadaAtual++;
  }

  /**
   * Aplica os efeitos de status dos pokesal e os efeitos passivos do terreno no final do turno.
   */
  public void aplicarEfeitosFimDeTurno() {
    PokeSal pokesal1 = treinador1.getPokesalInicial();
    PokeSal pokesal2 = treinador2.getPokesalInicial();

    aplicarStatusEmPokesal(pokesal1);
    aplicarStatusEmPokesal(pokesal2);

    terreno.aplicarEfeitoPassivo(pokesal1);
    terreno.aplicarEfeitoPassivo(pokesal2);
  }

  /**
   * Percorre a lista de status ativos de um pokesal e aplica cada um dos seus efeitos.
   *
   * @param pokesal passa o pokesal que receberá o efeito de status.
   */
  private void aplicarStatusEmPokesal(PokeSal pokesal) {
    for (int i = 0; i < pokesal.getStatus().size(); i++) {
      StatusEffect efeito = pokesal.getStatus().get(i);
      efeito.aplicarEfeito(pokesal);
    }
  }

  /**
   * Verifica se a batalha já acabou.
   */
  public boolean verificarFimBatalha() {
    PokeSal pokesal1 = treinador1.getPokesalInicial();
    PokeSal pokesal2 = treinador2.getPokesalInicial();

    if (!pokesal1.estaVivo()) {
      System.out.println("\n" + treinador2.getNome() + " venceu a batalha!");
      return true;
    }
    if (!pokesal2.estaVivo()) {
      System.out.println("\n" + treinador1.getNome() + " venceu a batalha!");
      return true;
    }
    return false;
  }
}