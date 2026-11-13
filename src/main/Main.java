package main;

import java.time.Clock;
import java.util.List;
import java.util.Random;

/**
 * Classe main. É o único lugar que instancia os colaboradores (composition root).
 **/
public class Main {

  /**
   * Método main.
   **/
  public static void main(String[] args) {
    RegistroBatalha registro = new RegistroBatalhaConsole(System.out);
    GeradorAleatorio gerador = new GeradorAleatorioPadrao(new Random());
    ServicoTerreno servicoTerreno = new ServicoTerrenoPadrao(Clock.systemDefaultZone());
    RepositorioResultados repositorio = new RepositorioResultadosEmMemoria();
    ExecutorDeGolpe executor = new ExecutorDeGolpe(gerador, servicoTerreno);

    batalhaIndividual(registro, gerador, servicoTerreno, repositorio, executor);

    registro.registrarMensagem("\n\n########## TORNEIO ##########");
    FabricaDeBatalha fabrica = new FabricaDeBatalhaPadrao(Terreno.CANTEIRO_CENTRAL, executor,
            servicoTerreno, registro, repositorio, gerador);
    ServicoRanking ranking = new ServicoRanking(repositorio, registro);
    Torneio torneio = new Torneio(fabrica, new EstrategiaGolpeMaisForte(), ranking, registro);
    torneio.disputar(List.of(
            criarTreinador("Ana", "CharSal", TipoElemental.FOGO),
            criarTreinador("Bruno", "SquirtSal", TipoElemental.AGUA),
            criarTreinador("Carla", "BulbaSal", TipoElemental.PLANTA),
            criarTreinador("Diego", "FlareSal", TipoElemental.FOGO)));
  }

  private static void batalhaIndividual(RegistroBatalha registro, GeradorAleatorio gerador,
                                        ServicoTerreno servicoTerreno, RepositorioResultados repositorio, ExecutorDeGolpe executor) {
    Treinador ana = criarTreinador("Ana", "CharSal", TipoElemental.FOGO);
    Treinador bruno = criarTreinador("Bruno", "SquirtSal", TipoElemental.AGUA);
    ana.getMochila().adicionarItem(new Potion());
    Item superPotion = new SuperPotion();
    bruno.getMochila().adicionarItem(superPotion);

    Golpe labareda = ana.getPokesalInicial().getGolpes().get(0);
    Golpe jatoDeAgua = bruno.getPokesalInicial().getGolpes().get(0);
    Golpe tsunami = bruno.getPokesalInicial().getGolpes().get(1);

    Batalha batalha = new Batalha(new Confronto(ana, bruno, Terreno.ASFALTO_QUENTE), executor,
            servicoTerreno, registro, repositorio, gerador);
    batalha.iniciarBatalha();

    while (!batalha.verificarFimBatalha() && batalha.getRodadaAtual()
            <= Batalha.LIMITE_DE_RODADAS) {
      // na rodada 2 o Bruno usa um item (no início do turno) e o Tsunami
      Golpe golpeBruno = jatoDeAgua;
      if (batalha.getRodadaAtual() == 2) {
        batalha.usarItem(bruno, superPotion);
        golpeBruno = tsunami;
      }
      batalha.proximoTurno(labareda, golpeBruno);
    }
    registro.registrarMensagem("\n=== Fim da batalha ===");
  }

  private static Treinador criarTreinador(String nomeTreinador, String nomePokesal,
                                          TipoElemental tipo) {
    PokeSal pokesal = new PokeSal(nomePokesal, 100, 20, 10, 15, tipo);
    pokesal.adicionarGolpe(new Golpe("Golpe " + tipo, tipo, 15f, 90f));
    if (tipo == TipoElemental.AGUA) {
      pokesal.adicionarGolpe(new Tsunami());
    }
    Treinador treinador = new Treinador(nomeTreinador);
    treinador.escolherInicial(pokesal);
    return treinador;
  }
}
