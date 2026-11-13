package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import main.Batalha;
import main.EstrategiaGolpeMaisForte;
import main.ExecutorDeGolpe;
import main.FabricaDeBatalhaPadrao;
import main.GeradorAleatorio;
import main.GeradorAleatorioPadrao;
import main.Golpe;
import main.PokeSal;
import main.Potion;
import main.RegistroBatalha;
import main.RegistroBatalhaConsole;
import main.RepositorioResultados;
import main.RepositorioResultadosEmMemoria;
import main.ResultadoBatalha;
import main.ServicoTerreno;
import main.StatusEffect;
import main.Terreno;
import main.TipoElemental;
import main.Treinador;
import main.Tsunami;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes da estratégia de ação, do repositório em memória, do registro em console, da fábrica
 * de batalhas, do gerador padrão e das validações de Golpe.
 */
@ExtendWith(MockitoExtension.class)
public class TesteEstrategiaEInfraestrutura {

  @Mock
  private ExecutorDeGolpe executor;

  @Mock
  private ServicoTerreno servicoTerreno;

  @Mock
  private RegistroBatalha registro;

  @Mock
  private RepositorioResultados repositorioMock;

  @Mock
  private GeradorAleatorio geradorMock;

  private PokeSal pokesal() {
    return new PokeSal("TesteSal", 100f, 20f, 10f, 20f, TipoElemental.AGUA);
  }

  @Test
  @DisplayName("Estratégia escolhe o golpe utilizável de maior poder")
  public void testEstrategiaEscolheMaisForte() {
    PokeSal pokesal = pokesal();
    Golpe fraco = new Golpe("Fraco", TipoElemental.AGUA, 10f, 90f);
    Tsunami tsunami = new Tsunami();
    pokesal.adicionarGolpe(fraco);
    pokesal.adicionarGolpe(tsunami);
    EstrategiaGolpeMaisForte estrategia = new EstrategiaGolpeMaisForte();

    assertSame(tsunami, estrategia.escolherGolpe(pokesal));

    tsunami.aposAcertar(pokesal); // Tsunami esgotado: cai para o golpe comum
    assertSame(fraco, estrategia.escolherGolpe(pokesal));
  }

  @Test
  @DisplayName("Estratégia devolve o primeiro golpe se nenhum puder ser usado")
  public void testEstrategiaSemGolpeUtilizavel() {
    PokeSal pokesal = pokesal();
    Tsunami tsunami = new Tsunami();
    pokesal.adicionarGolpe(tsunami);
    tsunami.aposAcertar(pokesal);

    assertSame(tsunami, new EstrategiaGolpeMaisForte().escolherGolpe(pokesal));
  }

  @Test
  @DisplayName("Estratégia falha de forma clara se o Pokésal não tem golpes")
  public void testEstrategiaSemGolpes() {
    assertThrows(IllegalStateException.class,
        () -> new EstrategiaGolpeMaisForte().escolherGolpe(pokesal()));
  }

  @Test
  @DisplayName("Repositório em memória guarda e lista resultados sem expor a lista interna")
  public void testRepositorioEmMemoria() {
    RepositorioResultadosEmMemoria repositorio = new RepositorioResultadosEmMemoria();
    Treinador ana = new Treinador("Ana");
    Treinador bruno = new Treinador("Bruno");
    ResultadoBatalha resultado = new ResultadoBatalha(ana, bruno, 4, List.of());

    repositorio.salvar(resultado);

    assertEquals(List.of(resultado), repositorio.listar());
    assertThrows(UnsupportedOperationException.class, () -> repositorio.listar().clear());
  }

  @Test
  @DisplayName("Registro em console escreve cada tipo de evento")
  public void testRegistroConsole() {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    RegistroBatalhaConsole console = new RegistroBatalhaConsole(
        new PrintStream(buffer, true, StandardCharsets.UTF_8));
    PokeSal a = pokesal();
    PokeSal b = new PokeSal("OutroSal", 100f, 20f, 10f, 20f, TipoElemental.FOGO);
    Golpe golpe = new Golpe("Jato", TipoElemental.AGUA, 10f, 90f);
    Treinador treinador = new Treinador("Ana");

    console.registrarAtaque(a, b, golpe);
    console.registrarDano(b, 30f, true);
    console.registrarDano(b, 0f, false);
    console.registrarEfeitoStatus(b, StatusEffect.QUEIMADO, 5f);
    console.registrarEfeitoTerreno(Terreno.CANTEIRO_CENTRAL, b, 5f);
    console.registrarUsoItem(treinador, new Potion());
    console.registrarMensagem("mensagem geral");
    console.registrarErro("falhou", new IllegalStateException("causa"));

    String saida = buffer.toString(StandardCharsets.UTF_8);
    assertTrue(saida.contains("TesteSal usou Jato em OutroSal"));
    assertTrue(saida.contains("Acerto crítico"));
    assertTrue(saida.contains("não sofreu dano"));
    assertTrue(saida.contains("QUEIMADO"));
    assertTrue(saida.contains("CANTEIRO_CENTRAL"));
    assertTrue(saida.contains("Ana usou o item Potion"));
    assertTrue(saida.contains("mensagem geral"));
    assertTrue(saida.contains("[ERRO] falhou: causa"));
  }

  @Test
  @DisplayName("Fábrica de batalhas monta a Batalha com o terreno configurado")
  public void testFabricaDeBatalha() {
    FabricaDeBatalhaPadrao fabrica = new FabricaDeBatalhaPadrao(Terreno.POCA_CHUVA, executor,
        servicoTerreno, registro, repositorioMock, geradorMock);

    Batalha batalha = fabrica.criar(new Treinador("Ana"), new Treinador("Bruno"));

    assertNotNull(batalha);
    assertEquals(Terreno.POCA_CHUVA, batalha.getTerreno());
    assertEquals(1, batalha.getRodadaAtual());
  }

  @Test
  @DisplayName("Gerador padrão devolve valores em [0,1) e respeita a semente")
  public void testGeradorPadrao() {
    GeradorAleatorioPadrao g1 = new GeradorAleatorioPadrao(new Random(42));
    GeradorAleatorioPadrao g2 = new GeradorAleatorioPadrao(new Random(42));

    double v = g1.proximoDouble();

    assertTrue(v >= 0.0 && v < 1.0);
    assertEquals(v, g2.proximoDouble(), 0.0);
  }

  @Test
  @DisplayName("Golpe rejeita precisão fora de 0 a 100 e dano nunca fica negativo")
  public void testValidacoesDeGolpe() {
    assertThrows(IllegalArgumentException.class,
        () -> new Golpe("Ruim", TipoElemental.AGUA, 10f, 101f));
    assertThrows(IllegalArgumentException.class,
        () -> new Golpe("Ruim", TipoElemental.AGUA, 10f, -1f));

    PokeSal fraco = new PokeSal("Fraco", 100f, 0f, 10f, 5f, TipoElemental.FOGO);
    PokeSal tanque = new PokeSal("Tanque", 100f, 0f, 500f, 5f, TipoElemental.FOGO);
    Golpe golpe = new Golpe("Cocada", TipoElemental.FOGO, 5f, 100f);
    assertEquals(0f, golpe.calcularDano(fraco, tanque), 0.001f);
  }
}
