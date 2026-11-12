package main;

/**
 * Recebe os eventos da batalha. Ordem dos eventos de um turno:
 * ataque, dano, efeito de status, efeito de terreno e uso de item.
 **/
public interface RegistroBatalha {

  /** Registra que um Pokésal vai atacar com um golpe. **/
  void registrarAtaque(PokeSal atacante, PokeSal alvo, Golpe golpe);

  /** Registra o dano causado ao alvo (0 se o golpe errou). **/
  void registrarDano(PokeSal alvo, float dano, boolean critico);

  /** Registra o efeito de um status aplicado no fim do turno. **/
  void registrarEfeitoStatus(PokeSal pokesal, StatusEffect efeito, float impacto);

  /** Registra o efeito passivo do terreno (cura recuperada). **/
  void registrarEfeitoTerreno(Terreno terreno, PokeSal pokesal, float cura);

  /** Registra o uso de um item por um treinador. **/
  void registrarUsoItem(Treinador treinador, Item item);

  /** Registra uma mensagem geral da batalha (início, vencedor, decisões). **/
  void registrarMensagem(String mensagem);

  /** Registra uma falha que não invalida a batalha. **/
  void registrarErro(String mensagem, Throwable causa);
}
