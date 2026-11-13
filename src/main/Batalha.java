package main;

import exception.RepositorioException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe batalha. Todos os colaboradores são recebidos pelo construtor
 * (Princípio da Inversão de Dependência).
 **/
public class Batalha {
  /** Número máximo de rodadas antes da decisão por limite de rodadas. **/
  public static final int LIMITE_DE_RODADAS = 10;
  private static final double LIMIAR_SORTEIO = 0.5;

  private final Treinador treinador1;
  private final Treinador treinador2;
  private final Terreno terreno;
  private final ExecutorDeGolpe executor;
  private final ServicoTerreno servicoTerreno;
  private final RegistroBatalha registro;
  private final RepositorioResultados repositorio;
  private final GeradorAleatorio gerador;
  private final List<UsoDeItem> itensDoTurno = new ArrayList<>();

  private int rodadaAtual;
  private boolean finalizada;
  private Treinador vencedor;
  private Treinador perdedor;

  /** Item usado no turno, com o evento ainda pendente de registro. **/
  private record UsoDeItem(Treinador treinador, Item item) {
  }

  /**
   * Método construtor.
   *
   * @param confronto treinadores e terreno da batalha.
   * @param executor executa os golpes (precisão, crítico, terreno).
   * @param servicoTerreno decide os efeitos passivos do terreno.
   * @param registro recebe os eventos da batalha.
   * @param repositorio guarda o resultado ao final da batalha.
   * @param gerador desempata decisões que dependem de sorte.
   **/
  public Batalha(Confronto confronto, ExecutorDeGolpe executor, ServicoTerreno servicoTerreno,
                 RegistroBatalha registro, RepositorioResultados repositorio, GeradorAleatorio gerador) {
    this.treinador1 = confronto.treinador1();
    this.treinador2 = confronto.treinador2();
    this.terreno = confronto.terreno();
    this.executor = executor;
    this.servicoTerreno = servicoTerreno;
    this.registro = registro;
    this.repositorio = repositorio;
    this.gerador = gerador;
    this.rodadaAtual = 1;
  }

  public int getRodadaAtual() {
    return rodadaAtual;
  }

  /** Número de turnos já disputados. **/
  public int getTurnos() {
    return rodadaAtual - 1;
  }

  public Terreno getTerreno() {
    return terreno;
  }

  public Treinador getVencedor() {
    return vencedor;
  }

  public Treinador getPerdedor() {
    return perdedor;
  }

  public boolean isFinalizada() {
    return finalizada;
  }

  /**
   * Anuncia o começo da batalha.
   **/
  public void iniciarBatalha() {
    registro.registrarMensagem("=== Batalha iniciada! ===");
    registro.registrarMensagem(treinador1.getNome() + " ("
            + treinador1.getPokesalInicial().getNome() + ") VS " + treinador2.getNome() + " ("
            + treinador2.getPokesalInicial().getNome() + ")");
    registro.registrarMensagem("Terreno: " + terreno);
  }

  /**
   * Decide quem ataca primeiro, olhando o spd de cada pokesal.
   **/
  public PokeSal determinarIniciativa() {
    PokeSal pokesal1 = treinador1.getPokesalInicial();
    PokeSal pokesal2 = treinador2.getPokesalInicial();

    if (pokesal1.getSpd() >= pokesal2.getSpd()) {
      return pokesal1;
    }
    return pokesal2;
  }

  /**
   * Usa um item no início do turno. O efeito é imediato, mas o evento de uso de item
   * só é enviado ao registro no fim do turno (depois de ataque, dano, status e terreno).
   *
   * @param treinador treinador que usa o item.
   * @param item item da mochila.
   * @return true se o item foi usado.
   **/
  public boolean usarItem(Treinador treinador, Item item) {
    boolean usou = treinador.usarItem(item, rodadaAtual);
    if (usou) {
      itensDoTurno.add(new UsoDeItem(treinador, item));
    }
    return usou;
  }

  /**
   * Executa uma rodada onde quem tem mais spd ataca primeiro com o golpe indicado.
   *
   * @param golpeTreinador1 golpe do treinador 1.
   * @param golpeTreinador2 golpe do treinador 2.
   **/
  public void proximoTurno(Golpe golpeTreinador1, Golpe golpeTreinador2) {
    if (finalizada) {
      return;
    }
    PokeSal pokesal1 = treinador1.getPokesalInicial();
    PokeSal pokesal2 = treinador2.getPokesalInicial();

    registro.registrarMensagem("--- Rodada " + rodadaAtual + " ---");

    if (determinarIniciativa() == pokesal1) {
      executarAtaque(pokesal1, pokesal2, golpeTreinador1);
      if (pokesal2.estaVivo()) {
        executarAtaque(pokesal2, pokesal1, golpeTreinador2);
      }
    } else {
      executarAtaque(pokesal2, pokesal1, golpeTreinador2);
      if (pokesal1.estaVivo()) {
        executarAtaque(pokesal1, pokesal2, golpeTreinador1);
      }
    }

    aplicarEfeitosFimDeTurno();

    // reseta coisas que valem só para 1 rodada
    treinador1.reiniciarEsquiva();
    treinador2.reiniciarEsquiva();

    rodadaAtual++;
  }

  private void executarAtaque(PokeSal atacante, PokeSal alvo, Golpe golpe) {
    if (!atacante.isAptoAtacar()) {
      if (atacante.estaVivo()) {
        registro.registrarMensagem(atacante.getNome()
                + " está exausto e não pode atacar nesta rodada!");
        atacante.setExaustoTurnoAtual(false);
      }
      return;
    }
    registro.registrarAtaque(atacante, alvo, golpe);
    ResultadoAtaque resultado = executor.executar(atacante, alvo, golpe, terreno);
    registro.registrarDano(alvo, resultado.dano(), resultado.critico());
  }

  /**
   * Aplica, nesta ordem, os efeitos de status, os efeitos passivos do terreno e registra
   * os itens usados no turno. Pokésal derrotados não recebem efeitos.
   */
  public void aplicarEfeitosFimDeTurno() {
    PokeSal pokesal1 = treinador1.getPokesalInicial();
    PokeSal pokesal2 = treinador2.getPokesalInicial();

    aplicarStatusEmPokesal(pokesal1);
    aplicarStatusEmPokesal(pokesal2);

    aplicarTerrenoEmPokesal(pokesal1);
    aplicarTerrenoEmPokesal(pokesal2);

    registrarItensDoTurno();
  }

  private void aplicarStatusEmPokesal(PokeSal pokesal) {
    if (!pokesal.estaVivo()) {
      return;
    }
    for (StatusEffect efeito : new ArrayList<>(pokesal.getStatus())) {
      float impacto = efeito.aplicarEfeito(pokesal);
      registro.registrarEfeitoStatus(pokesal, efeito, impacto);
    }
  }

  private void aplicarTerrenoEmPokesal(PokeSal pokesal) {
    if (!pokesal.estaVivo()) {
      return;
    }
    float cura = servicoTerreno.aplicarEfeitoPassivo(terreno, pokesal);
    if (cura > 0) {
      registro.registrarEfeitoTerreno(terreno, pokesal, cura);
    }
  }

  private void registrarItensDoTurno() {
    for (UsoDeItem uso : itensDoTurno) {
      registro.registrarUsoItem(uso.treinador(), uso.item());
    }
    itensDoTurno.clear();
  }

  /**
   * Verifica se a batalha já acabou. Se acabou, define vencedor e perdedor e salva o
   * resultado (uma única vez). Se os dois Pokésal caírem no mesmo turno, o gerador desempata.
   *
   * @return true se a batalha terminou.
   */
  public boolean verificarFimBatalha() {
    if (finalizada) {
      return true;
    }
    boolean vivo1 = treinador1.getPokesalInicial().estaVivo();
    boolean vivo2 = treinador2.getPokesalInicial().estaVivo();

    if (vivo1 && vivo2) {
      return false;
    }
    if (!vivo1 && !vivo2) {
      registro.registrarMensagem("Os dois Pokésal caíram no mesmo turno! Desempate por sorteio.");
      finalizar(sortearTreinador());
    } else {
      finalizar(vivo1 ? treinador1 : treinador2);
    }
    return true;
  }

  /**
   * Disputa a batalha inteira com a estratégia informada. Se ninguém cair em
   * {@link #LIMITE_DE_RODADAS} rodadas, vence quem tem maior percentual de HP;
   * em empate exato de HP o gerador desempata.
   *
   * @param estrategia escolhe o golpe de cada Pokésal em cada turno.
   * @return o treinador vencedor.
   **/
  public Treinador executarAteOFim(EstrategiaDeAcao estrategia) {
    iniciarBatalha();
    PokeSal pokesal1 = treinador1.getPokesalInicial();
    PokeSal pokesal2 = treinador2.getPokesalInicial();

    while (!verificarFimBatalha() && rodadaAtual <= LIMITE_DE_RODADAS) {
      proximoTurno(estrategia.escolherGolpe(pokesal1), estrategia.escolherGolpe(pokesal2));
    }
    if (!finalizada) {
      decidirPorLimiteDeRodadas();
    }
    return vencedor;
  }

  private void decidirPorLimiteDeRodadas() {
    float percentual1 = percentualDeHp(treinador1.getPokesalInicial());
    float percentual2 = percentualDeHp(treinador2.getPokesalInicial());
    registro.registrarMensagem("Limite de " + LIMITE_DE_RODADAS + " rodadas atingido!");

    if (Float.compare(percentual1, percentual2) == 0) {
      registro.registrarMensagem("HP igual nos dois lados! Desempate por sorteio.");
      finalizar(sortearTreinador());
    } else if (percentual1 > percentual2) {
      finalizar(treinador1);
    } else {
      finalizar(treinador2);
    }
  }

  private float percentualDeHp(PokeSal pokesal) {
    return pokesal.getHpAtual() / pokesal.getHpMaximo();
  }

  private Treinador sortearTreinador() {
    return gerador.proximoDouble() < LIMIAR_SORTEIO ? treinador1 : treinador2;
  }

  private void finalizar(Treinador treinadorVencedor) {
    this.vencedor = treinadorVencedor;
    this.perdedor = treinadorVencedor == treinador1 ? treinador2 : treinador1;
    this.finalizada = true;
    registro.registrarMensagem(vencedor.getNome() + " venceu a batalha!");
    salvarResultado();
  }

  private void salvarResultado() {
    ResultadoBatalha resultado = new ResultadoBatalha(vencedor, perdedor, getTurnos(),
            List.of(treinador1.getPokesalInicial(), treinador2.getPokesalInicial()));
    try {
      repositorio.salvar(resultado);
    } catch (RepositorioException e) {
      // a batalha continua válida: só registramos o erro
      registro.registrarErro("Falha ao salvar o resultado da batalha", e);
    }
  }
}
