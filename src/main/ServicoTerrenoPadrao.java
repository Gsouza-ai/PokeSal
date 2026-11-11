package main;

import java.time.Clock;
import java.time.LocalTime;

/**
 * Implementação do serviço de terreno. O Asfalto Quente só é ativado durante o dia;
 * o clima chuvoso é representado pelo próprio terreno Poça de Chuva.
 **/
public class ServicoTerrenoPadrao implements ServicoTerreno {
  static final LocalTime INICIO_DO_DIA = LocalTime.of(6, 0);
  static final LocalTime FIM_DO_DIA = LocalTime.of(18, 0);
  private static final float MULTIPLICADOR_ASFALTO = 1.15f;
  private static final float MULTIPLICADOR_CHUVA = 1.10f;
  private static final float PERCENTUAL_CURA_CANTEIRO = 0.05f;

  private final Clock relogio;

  /**
   * Método construtor.
   *
   * @param relogio relógio usado para saber se é dia ou noite.
   **/
  public ServicoTerrenoPadrao(Clock relogio) {
    this.relogio = relogio;
  }

  /**
   * Informa se o horário atual do relógio é de dia (06:00 inclusive até 18:00 exclusive).
   **/
  public boolean isDia() {
    LocalTime agora = LocalTime.now(relogio);
    return !agora.isBefore(INICIO_DO_DIA) && agora.isBefore(FIM_DO_DIA);
  }

  @Override
  public float aplicarEfeitoAtivo(Terreno terreno, Golpe golpe, float dano) {
    if (terreno == Terreno.ASFALTO_QUENTE && golpe.getTipo() == TipoElemental.FOGO && isDia()) {
      return dano * MULTIPLICADOR_ASFALTO;
    }
    if (terreno == Terreno.POCA_CHUVA && golpe.getTipo() == TipoElemental.AGUA) {
      return dano * MULTIPLICADOR_CHUVA;
    }
    return dano;
  }

  @Override
  public float aplicarEfeitoPassivo(Terreno terreno, PokeSal pokesal) {
    if (terreno == Terreno.CANTEIRO_CENTRAL && pokesal.getTipo() == TipoElemental.PLANTA
        && pokesal.estaVivo()) {
      float antes = pokesal.getHpAtual();
      pokesal.curar(Math.round(pokesal.getHpMaximo() * PERCENTUAL_CURA_CANTEIRO));
      return pokesal.getHpAtual() - antes;
    }
    return 0f;
  }
}
