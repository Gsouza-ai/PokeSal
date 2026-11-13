package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import main.Antidote;
import main.PokeSal;
import main.StatusEffect;
import main.TipoElemental;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes dos efeitos de status (sem mocks: PokeSal e StatusEffect são regra pura).
 */
public class TesteStatusEffect {

  private PokeSal novo() {
    return new PokeSal("TesteSal", 100f, 20f, 10f, 20f, TipoElemental.FOGO);
  }

  @Test
  @DisplayName("Queimado perde 5% do HP máximo por turno")
  public void testDanoDaQueimadura() {
    PokeSal pokesal = novo();

    float impacto = StatusEffect.QUEIMADO.aplicarEfeito(pokesal);

    assertEquals(5f, impacto, 0.001f);
    assertEquals(95f, pokesal.getHpAtual(), 0.001f);
  }

  @Test
  @DisplayName("Envenenado perde 8% do HP máximo por turno")
  public void testDanoDoVeneno() {
    PokeSal pokesal = novo();

    assertEquals(8f, StatusEffect.ENVENENADO.aplicarEfeito(pokesal), 0.001f);
    assertEquals(92f, pokesal.getHpAtual(), 0.001f);
  }

  @Test
  @DisplayName("BUG: a paralisia reduz a velocidade uma única vez, sem acumular a cada turno")
  public void testParalisiaNaoAcumula() {
    PokeSal pokesal = novo();
    pokesal.aplicarStatus(StatusEffect.PARALISADO);

    StatusEffect.PARALISADO.aplicarEfeito(pokesal);
    StatusEffect.PARALISADO.aplicarEfeito(pokesal);
    StatusEffect.PARALISADO.aplicarEfeito(pokesal);

    assertEquals(10f, pokesal.getSpd(), 0.001f, "20 x 0.5, mesmo após vários turnos");
    assertEquals(100f, pokesal.getHpAtual(), 0.001f, "paralisia não causa dano direto");
  }

  @Test
  @DisplayName("BUG: queimado reduz o ataque em 5% enquanto durar o status")
  public void testQueimaduraReduzAtaque() {
    PokeSal pokesal = novo();
    pokesal.aplicarStatus(StatusEffect.QUEIMADO);

    assertEquals(19f, pokesal.getAtk(), 0.001f);
  }

  @Test
  @DisplayName("Limpar status devolve ataque e velocidade normais")
  public void testAntidotoRestauraAtributos() {
    PokeSal pokesal = novo();
    pokesal.aplicarStatus(StatusEffect.QUEIMADO);
    pokesal.aplicarStatus(StatusEffect.PARALISADO);

    new Antidote().aplicarEfeito(pokesal);

    assertTrue(pokesal.getStatus().isEmpty());
    assertEquals(20f, pokesal.getAtk(), 0.001f);
    assertEquals(20f, pokesal.getSpd(), 0.001f);
  }

  @Test
  @DisplayName("Aplicar o mesmo status duas vezes não duplica")
  public void testStatusNaoDuplica() {
    PokeSal pokesal = novo();
    pokesal.aplicarStatus(StatusEffect.QUEIMADO);
    pokesal.aplicarStatus(StatusEffect.QUEIMADO);

    assertEquals(1, pokesal.getStatus().size());
    assertFalse(pokesal.getStatus().isEmpty());
  }
}
