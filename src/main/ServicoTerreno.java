package main;

/**
 * Serviço que decide o efeito do terreno considerando clima e horário.
 **/
public interface ServicoTerreno {

    /**
     * Aplica o efeito ativo do terreno sobre o dano de um golpe.
     *
     * @param terreno terreno da batalha (pode ser nulo).
     * @param golpe golpe usado.
     * @param dano dano antes do efeito do terreno.
     * @return dano depois do efeito do terreno.
     **/
    float aplicarEfeitoAtivo(Terreno terreno, Golpe golpe, float dano);

    /**
     * Aplica o efeito passivo (fim de turno) do terreno em um Pokésal.
     *
     * @param terreno terreno da batalha (pode ser nulo).
     * @param pokesal Pokésal que recebe o efeito.
     * @return quantidade de HP efetivamente recuperada (0 se nada aconteceu).
     **/
    float aplicarEfeitoPassivo(Terreno terreno, PokeSal pokesal);
}
