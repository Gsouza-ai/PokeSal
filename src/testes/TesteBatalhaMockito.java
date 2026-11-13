package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import exception.RepositorioException;
import java.util.List;
import main.Batalha;
import main.Confronto;
import main.ExecutorDeGolpe;
import main.GeradorAleatorio;
import main.Golpe;
import main.Item;
import main.PokeSal;
import main.Potion;
import main.RegistroBatalha;
import main.RepositorioResultados;
import main.ResultadoAtaque;
import main.ResultadoBatalha;
import main.ServicoTerreno;
import main.StatusEffect;
import main.Terreno;
import main.TipoElemental;
import main.Treinador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes da Batalha. Todos os colaboradores são mockados (executor, serviço de terreno,
 * registro, repositório e gerador). Reais: Treinador, PokeSal, Golpe e Item, que são os dados
 * do domínio. A Batalha é montada à mão no @BeforeEach porque o construtor também recebe o
 * Confronto (treinadores reais), que o @InjectMocks não consegue fornecer.
 */
@ExtendWith(MockitoExtension.class)
public class TesteBatalhaMockito {

  @Mock
  private ExecutorDeGolpe executor;

  @Mock
  private ServicoTerreno servicoTerreno;

  @Mock
  private RegistroBatalha registro;

  @Mock
  private RepositorioResultados repositorio;

  @Mock
  private GeradorAleatorio gerador;

  private PokeSal rapido;
  private PokeSal lento;
  private Treinador treinador1;
  private Treinador treinador2;
  private Golpe golpe1;
  private Golpe golpe2;
  private Batalha batalha;

  @BeforeEach
  void preparar() {
    rapido = new PokeSal("RapidoSal", 100f, 20f, 10f, 20f, TipoElemental.FOGO);
    lento = new PokeSal("LentoSal", 100f, 20f, 10f, 10f, TipoElemental.PLANTA);
    treinador1 = new Treinador("Ana");
    treinador1.escolherInicial(rapido);
    treinador2 = new Treinador("Bruno");
    treinador2.escolherInicial(lento);
    golpe1 = new Golpe("Labareda", TipoElemental.FOGO, 15f, 90f);
    golpe2 = new Golpe("Chicote", TipoElemental.PLANTA, 15f, 90f);
    batalha = new Batalha(new Confronto(treinador1, treinador2, Terreno.CANTEIRO_CENTRAL),
        executor, servicoTerreno, registro, repositorio, gerador);
  }

  /** O executor mockado causa 'dano' fixo no defensor (2º argumento) a cada golpe. */
  private void executorCausaDano(float dano) {
    when(executor.executar(any(PokeSal.class), any(PokeSal.class), any(Golpe.class),
        any(Terreno.class))).thenAnswer(invocacao -> {
          PokeSal alvo = invocacao.getArgument(1);
          alvo.receberDano(dano);
          return new ResultadoAtaque(dano, true, false);
        });
  }

  private void executorSemDano() {
    when(executor.executar(any(PokeSal.class), any(PokeSal.class), any(Golpe.class),
        any(Terreno.class))).thenReturn(ResultadoAtaque.SEM_EFEITO);
  }

  private void disputarAteOFim() {
    while (!batalha.verificarFimBatalha()) {
      batalha.proximoTurno(golpe1, golpe2);
    }
  }

  @Test
  @DisplayName("Ordem dos eventos: ataque, dano, status, terreno e item (item no fim)")
  public void testSequenciaDeEventosNoTurno() {
    rapido.aplicarStatus(StatusEffect.QUEIMADO);
    Item potion = new Potion();
    treinador1.getMochila().adicionarItem(potion);
    when(executor.executar(any(PokeSal.class), any(PokeSal.class), any(Golpe.class),
        any(Terreno.class))).thenReturn(new ResultadoAtaque(10f, true, false));
    // Matcher same(): cada Pokésal tem um retorno diferente do serviço de terreno.
    // (doReturn porque o strict stubs não aceita when() repetido com argumentos diferentes)
    doReturn(0f).when(servicoTerreno)
        .aplicarEfeitoPassivo(eq(Terreno.CANTEIRO_CENTRAL), same(rapido));
    doReturn(5f).when(servicoTerreno)
        .aplicarEfeitoPassivo(eq(Terreno.CANTEIRO_CENTRAL), same(lento));

    batalha.proximoTurno(golpe1, golpe2); // rodada 1: item ainda é proibido
    clearInvocations(registro);

    assertTrue(batalha.usarItem(treinador1, potion), "na rodada 2 o item é liberado");
    batalha.proximoTurno(golpe1, golpe2);

    InOrder ordem = inOrder(registro);
    // Valores exatos: o turno é determinístico, então verificamos exatamente o que foi enviado.
    ordem.verify(registro).registrarAtaque(rapido, lento, golpe1);
    ordem.verify(registro).registrarDano(lento, 10f, false);
    ordem.verify(registro).registrarAtaque(lento, rapido, golpe2);
    ordem.verify(registro).registrarDano(rapido, 10f, false);
    ordem.verify(registro).registrarEfeitoStatus(rapido, StatusEffect.QUEIMADO, 5f);
    ordem.verify(registro).registrarEfeitoTerreno(Terreno.CANTEIRO_CENTRAL, lento, 5f);
    ordem.verify(registro).registrarUsoItem(treinador1, potion);
  }

  @Test
  @DisplayName("Item não pode ser usado na rodada 1 e nenhum evento de item é enviado")
  public void testItemBloqueadoNaPrimeiraRodada() {
    Item potion = new Potion();
    treinador1.getMochila().adicionarItem(potion);
    executorSemDano();

    assertFalse(batalha.usarItem(treinador1, potion));
    batalha.proximoTurno(golpe1, golpe2);

    verify(registro, never()).registrarUsoItem(any(Treinador.class), any(Item.class));
  }

  @Test
  @DisplayName("ArgumentCaptor: resultado salvo ao fim da batalha com vencedor, perdedor e turnos")
  public void testResultadoSalvoAoFimDaBatalha() {
    executorCausaDano(40f); // o lento (100 HP) cai no 3º turno; o rápido sobrevive com 20 HP

    disputarAteOFim();

    ArgumentCaptor<ResultadoBatalha> captor = ArgumentCaptor.forClass(ResultadoBatalha.class);
    verify(repositorio, times(1)).salvar(captor.capture());
    ResultadoBatalha salvo = captor.getValue();
    assertSame(treinador1, salvo.vencedor());
    assertSame(treinador2, salvo.perdedor());
    assertEquals(3, salvo.turnos());
    assertEquals(List.of(rapido, lento), salvo.pokesalUtilizados());
    verify(registro, atLeastOnce()).registrarMensagem(anyString());
  }

  @Test
  @DisplayName("Repositório lança exceção e o vencedor continua definido")
  public void testFalhaNoRepositorioNaoInvalidaBatalha() {
    executorCausaDano(40f);
    RepositorioException falha = new RepositorioException("disco cheio");
    doThrow(falha).when(repositorio).salvar(any(ResultadoBatalha.class));

    disputarAteOFim();

    assertTrue(batalha.isFinalizada());
    assertSame(treinador1, batalha.getVencedor());
    assertSame(treinador2, batalha.getPerdedor());
    verify(repositorio, times(1)).salvar(any(ResultadoBatalha.class));
    verify(registro).registrarErro(anyString(), same(falha));
  }

  @Test
  @DisplayName("Repositório não é chamado se a batalha não terminou")
  public void testRepositorioNaoChamadoSeBatalhaNaoTerminou() {
    executorSemDano();

    batalha.proximoTurno(golpe1, golpe2);

    assertFalse(batalha.verificarFimBatalha());
    verify(repositorio, never()).salvar(any(ResultadoBatalha.class));
  }

  @Test
  @DisplayName("O resultado é salvo uma única vez mesmo se o fim for verificado de novo")
  public void testResultadoSalvoUmaUnicaVez() {
    executorCausaDano(100f);

    batalha.proximoTurno(golpe1, golpe2);
    assertTrue(batalha.verificarFimBatalha());
    assertTrue(batalha.verificarFimBatalha());
    batalha.proximoTurno(golpe1, golpe2); // batalha já finalizada: nada acontece

    verify(repositorio, times(1)).salvar(any(ResultadoBatalha.class));
    assertEquals(1, batalha.getTurnos());
  }

  @Test
  @DisplayName("BUG: dois Pokésal derrotados no mesmo turno são desempatados pelo gerador")
  public void testEmpateDuploDesempatadoPeloGerador() {
    rapido.setHpAtual(5f);
    lento.setHpAtual(5f);
    rapido.aplicarStatus(StatusEffect.QUEIMADO);
    lento.aplicarStatus(StatusEffect.QUEIMADO);
    executorSemDano();
    when(gerador.proximoDouble()).thenReturn(0.2); // < 0.5: treinador 1

    batalha.proximoTurno(golpe1, golpe2); // a queimadura (5) derruba os dois

    assertTrue(batalha.verificarFimBatalha());
    assertSame(treinador1, batalha.getVencedor(),
        "antes da correção o treinador 2 sempre vencia");
    verify(gerador, times(1)).proximoDouble();
  }

  @Test
  @DisplayName("BUG: Pokésal derrotado não recebe efeito de terreno (não é reanimado)")
  public void testPokesalDerrotadoNaoRecebeEfeitoDeTerreno() {
    executorCausaDano(100f);

    batalha.proximoTurno(golpe1, golpe2);

    assertFalse(lento.estaVivo());
    verify(servicoTerreno, never()).aplicarEfeitoPassivo(any(Terreno.class), same(lento));
    verify(servicoTerreno, times(1)).aplicarEfeitoPassivo(any(Terreno.class), same(rapido));
  }

  @Test
  @DisplayName("Pokésal exausto não ataca na rodada e volta ao normal depois")
  public void testPokesalExaustoNaoAtaca() {
    rapido.setExaustoTurnoAtual(true);
    executorSemDano();

    batalha.proximoTurno(golpe1, golpe2);

    verify(executor, never()).executar(same(rapido), any(PokeSal.class), any(Golpe.class),
        any(Terreno.class));
    verify(executor, times(1)).executar(same(lento), any(PokeSal.class), any(Golpe.class),
        any(Terreno.class));
    assertFalse(rapido.isExaustoTurnoAtual());
  }
}
