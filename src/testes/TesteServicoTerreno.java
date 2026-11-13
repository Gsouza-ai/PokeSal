package testes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Clock;
import main.Golpe;
import main.PokeSal;
import main.ServicoTerrenoPadrao;
import main.Terreno;
import main.TipoElemental;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes do ServicoTerrenoPadrao. O Clock é mockado para controlar dia e noite.
 * (Clock.fixed também funcionaria; o mock deixa explícita a interação com o relógio.)
 */
@ExtendWith(MockitoExtension.class)
public class TesteServicoTerreno {
  private static final ZoneId ZONA = ZoneId.of("America/Bahia");

  @Mock
  private Clock relogio;

  @InjectMocks
  private ServicoTerrenoPadrao servico;

  private static Instant instanteEm(int hora, int minuto) {
    return LocalDateTime.of(2026, 11, 9, hora, minuto).atZone(ZONA).toInstant();
  }

  @Test
  @DisplayName("Asfalto Quente só bonifica Fogo de dia (dia e noite mockados em sequência)")
  public void testAsfaltoQuenteApenasDuranteODia() {
    Golpe golpeFogo = new Golpe("Labareda", TipoElemental.FOGO, 15f, 90f);
    when(relogio.getZone()).thenReturn(ZONA);
    when(relogio.instant()).thenReturn(instanteEm(12, 0), instanteEm(22, 0));

    float danoDeDia = servico.aplicarEfeitoAtivo(Terreno.ASFALTO_QUENTE, golpeFogo, 100f);
    float danoDeNoite = servico.aplicarEfeitoAtivo(Terreno.ASFALTO_QUENTE, golpeFogo, 100f);

    assertEquals(115.0f, danoDeDia, 0.001f, "de dia: x1.15");
    assertEquals(100.0f, danoDeNoite, 0.001f, "de noite: sem bônus");
    verify(relogio, times(2)).instant();
  }

  @Test
  @DisplayName("Limites do dia: 06:00 já é dia; 18:00 já é noite")
  public void testLimitesDoDia() {
    when(relogio.getZone()).thenReturn(ZONA);
    when(relogio.instant()).thenReturn(instanteEm(6, 0), instanteEm(17, 59), instanteEm(18, 0),
        instanteEm(5, 59));

    assertTrue(servico.isDia(), "06:00");
    assertTrue(servico.isDia(), "17:59");
    assertFalse(servico.isDia(), "18:00");
    assertFalse(servico.isDia(), "05:59");
  }

  @Test
  @DisplayName("Poça de Chuva multiplica dano de Água por 1.10 (a qualquer hora)")
  public void testPocaChuvaComAgua() {
    Golpe golpeAgua = new Golpe("Jato De Agua", TipoElemental.AGUA, 15f, 90f);

    assertEquals(110.0f, servico.aplicarEfeitoAtivo(Terreno.POCA_CHUVA, golpeAgua, 100f), 0.001f);
  }

  @Test
  @DisplayName("Sem combinação entre terreno e elemento o dano não muda")
  public void testSemCombinacao() {
    Golpe golpeAgua = new Golpe("Jato De Agua", TipoElemental.AGUA, 15f, 90f);

    assertEquals(100.0f, servico.aplicarEfeitoAtivo(Terreno.ASFALTO_QUENTE, golpeAgua, 100f),
        0.001f);
    assertEquals(100.0f, servico.aplicarEfeitoAtivo(null, golpeAgua, 100f), 0.001f);
  }

  @Test
  @DisplayName("Canteiro Central cura 5% do HP máximo de um Pokésal de Planta")
  public void testCanteiroCentralComPlanta() {
    PokeSal planta = new PokeSal("BulbaSal", 100, 18, 12, 12, TipoElemental.PLANTA);
    planta.receberDano(20);

    float cura = servico.aplicarEfeitoPassivo(Terreno.CANTEIRO_CENTRAL, planta);

    assertEquals(5.0f, cura, 0.001f);
    assertEquals(85.0f, planta.getHpAtual(), 0.001f);
  }

  @Test
  @DisplayName("Canteiro Central não cura Pokésal que não é de Planta")
  public void testCanteiroCentralComFogo() {
    PokeSal fogo = new PokeSal("CharSal", 100, 20, 10, 15, TipoElemental.FOGO);
    fogo.receberDano(20);

    float cura = servico.aplicarEfeitoPassivo(Terreno.CANTEIRO_CENTRAL, fogo);

    assertEquals(0.0f, cura, 0.001f);
    assertEquals(80.0f, fogo.getHpAtual(), 0.001f);
  }

  @Test
  @DisplayName("BUG: o terreno não pode reanimar um Pokésal de Planta derrotado")
  public void testCanteiroCentralNaoReanimaPokesalDerrotado() {
    PokeSal planta = new PokeSal("BulbaSal", 100, 18, 12, 12, TipoElemental.PLANTA);
    planta.receberDano(9999);

    float cura = servico.aplicarEfeitoPassivo(Terreno.CANTEIRO_CENTRAL, planta);

    assertEquals(0.0f, cura, 0.001f);
    assertFalse(planta.estaVivo());
  }

  @Test
  @DisplayName("Terreno nulo ou outro terreno não cura ninguém")
  public void testSemEfeitoPassivo() {
    PokeSal planta = new PokeSal("BulbaSal", 100, 18, 12, 12, TipoElemental.PLANTA);
    planta.receberDano(20);

    assertEquals(0.0f, servico.aplicarEfeitoPassivo(null, planta), 0.001f);
    assertEquals(0.0f, servico.aplicarEfeitoPassivo(Terreno.POCA_CHUVA, planta), 0.001f);
    assertEquals(80.0f, planta.getHpAtual(), 0.001f);
  }
}
