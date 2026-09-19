/**
 * Classe main.
 **/
public class Main {

  /**
   * Método main.
   **/
  public static void main(String[] args) {
    // Cria os Pokesal iniciais
    PokeSal charSal = new PokeSal("CharSal", 100, 20, 10, 15, TipoElemental.FOGO);
    PokeSal squirtSal = new PokeSal("SquirtSal", 100, 18, 12, 12, TipoElemental.AGUA);

    // Cada Pokesal ganha alguns golpes
    Golpe labareda = new Golpe("Labareda", TipoElemental.FOGO, 15f, 0.9f);
    Golpe jatoDeAgua = new Golpe("Jato De Agua", TipoElemental.AGUA, 15f, 0.9f);
    Golpe tsunami = new Tsunami();

    charSal.adicionarGolpe(labareda);
    squirtSal.adicionarGolpe(jatoDeAgua);
    squirtSal.adicionarGolpe(tsunami);

    // Cria os dois treinadores
    Treinador treinador1 = new Treinador("Ana");
    Treinador treinador2 = new Treinador("Bruno");

    // Cada treinador escolhe seu inicial
    treinador1.escolherInicial(charSal);
    treinador2.escolherInicial(squirtSal);

    // Escolhe o terreno da batalha
    Terreno terreno = Terreno.ASFALTO_QUENTE;

    // Coloca uns itens na mochila de cada treinador
    treinador1.getMochila().adicionarItem(new Potion());
    treinador2.getMochila().adicionarItem(new SuperPotion());

    // Cria e inicia a batalha
    Batalha batalha = new Batalha(treinador1, treinador2, terreno);
    batalha.iniciarBatalha();

    // Roda algumas rodadas até alguém vencer
    int limiteDeRodadasDeSeguranca = 10;

    while (!batalha.verificarFimBatalha() && batalha.getRodadaAtual()
            <= limiteDeRodadasDeSeguranca) {

      // a partir da rodada 2 o treinador2 usa um item em vez de atacar
      if (batalha.getRodadaAtual() == 2) {
        treinador2.usarItem(new SuperPotion(), batalha.getRodadaAtual());
      }

      // a partir da rodada 3 o treinador2 usa Tsunami em vez de Jato De Agua
      Golpe golpeTreinador2 = jatoDeAgua;
      if (batalha.getRodadaAtual() == 2) {
        golpeTreinador2 = tsunami;
      }

      // cada um escolhe um golpe para usar na rodada
      batalha.proximoTurno(labareda, golpeTreinador2);

      System.out.println("HP " + charSal.getNome() + ": " + charSal.getHpAtual());
      System.out.println("HP " + squirtSal.getNome() + ": " + squirtSal.getHpAtual());
    }
  }
}