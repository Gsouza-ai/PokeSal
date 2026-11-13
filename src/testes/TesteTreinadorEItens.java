package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import main.PokeSal;
import main.Potion;
import main.SuperPotion;
import main.TipoElemental;
import main.Treinador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes do Treinador e dos itens (sem mocks: são regras puras de domínio).
 */
public class TesteTreinadorEItens {
  private Treinador treinador;
  private PokeSal pokesal;

  @BeforeEach
  void preparar() {
    pokesal = new PokeSal("TesteSal", 100f, 20f, 10f, 20f, TipoElemental.FOGO);
    pokesal.receberDano(70f);
    treinador = new Treinador("Ana");
    treinador.escolherInicial(pokesal);
  }

  @Test
  @DisplayName("Item não pode ser usado na rodada 1")
  public void testItemBloqueadoNaRodada1() {
    Potion potion = new Potion();
    treinador.getMochila().adicionarItem(potion);

    assertFalse(treinador.usarItem(potion, 1));
    assertEquals(30f, pokesal.getHpAtual(), 0.001f);
    assertEquals(0, treinador.getItensUsadosNaBatalha());
  }

  @Test
  @DisplayName("Item usado cura, sai da mochila e conta para o limite")
  public void testUsoDeItemCuraERemoveDaMochila() {
    SuperPotion superPotion = new SuperPotion();
    treinador.getMochila().adicionarItem(superPotion);

    assertTrue(treinador.usarItem(superPotion, 2));

    assertEquals(80f, pokesal.getHpAtual(), 0.001f);
    assertEquals(1, treinador.getItensUsadosNaBatalha());
    assertFalse(treinador.getMochila().getItens().contains(superPotion));
  }

  @Test
  @DisplayName("O terceiro item da batalha é bloqueado (limite de 2)")
  public void testLimiteDeItens() {
    Potion p1 = new Potion();
    Potion p2 = new Potion();
    Potion p3 = new Potion();
    treinador.getMochila().adicionarItem(p1);
    treinador.getMochila().adicionarItem(p2);
    treinador.getMochila().adicionarItem(p3);

    assertTrue(treinador.usarItem(p1, 2));
    assertTrue(treinador.usarItem(p2, 3));
    assertFalse(treinador.usarItem(p3, 4));
    assertEquals(2, treinador.getItensUsadosNaBatalha());
  }

  @Test
  @DisplayName("Esquiva só pode ser usada uma vez por rodada")
  public void testEsquivaUmaVezPorRodada() {
    assertTrue(treinador.esquivar());
    assertFalse(treinador.esquivar());
    treinador.reiniciarEsquiva();
    assertTrue(treinador.esquivar());
  }

  @Test
  @DisplayName("Preparar para nova batalha zera itens e restaura o Pokésal")
  public void testPrepararParaNovaBatalha() {
    Potion potion = new Potion();
    treinador.getMochila().adicionarItem(potion);
    treinador.usarItem(potion, 2);

    treinador.prepararParaNovaBatalha();

    assertEquals(0, treinador.getItensUsadosNaBatalha());
    assertEquals(100f, pokesal.getHpAtual(), 0.001f);
  }

  @Test
  @DisplayName("Treinador sem Pokésal pode ser preparado sem erro")
  public void testPrepararSemPokesal() {
    Treinador semPokesal = new Treinador("Sem Pokesal");

    semPokesal.prepararParaNovaBatalha();

    assertEquals(0, semPokesal.getItensUsadosNaBatalha());
  }
}
