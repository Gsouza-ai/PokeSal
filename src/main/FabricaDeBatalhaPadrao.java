package main;

/**
 * Fábrica de produção: monta cada Batalha com os colaboradores compartilhados.
 **/
public class FabricaDeBatalhaPadrao implements FabricaDeBatalha {
  private final Terreno terreno;
  private final ExecutorDeGolpe executor;
  private final ServicoTerreno servicoTerreno;
  private final RegistroBatalha registro;
  private final RepositorioResultados repositorio;
  private final GeradorAleatorio gerador;

  /**
   * Método construtor.
   *
   * @param terreno terreno das batalhas do torneio.
   * @param executor executor de golpes.
   * @param servicoTerreno serviço de terreno.
   * @param registro registro de eventos.
   * @param repositorio repositório de resultados.
   * @param gerador gerador aleatório (desempates).
   **/
  public FabricaDeBatalhaPadrao(Terreno terreno, ExecutorDeGolpe executor,
      ServicoTerreno servicoTerreno, RegistroBatalha registro,
      RepositorioResultados repositorio, GeradorAleatorio gerador) {
    this.terreno = terreno;
    this.executor = executor;
    this.servicoTerreno = servicoTerreno;
    this.registro = registro;
    this.repositorio = repositorio;
    this.gerador = gerador;
  }

  @Override
  public Batalha criar(Treinador treinador1, Treinador treinador2) {
    return new Batalha(new Confronto(treinador1, treinador2, terreno), executor,
        servicoTerreno, registro, repositorio, gerador);
  }
}
