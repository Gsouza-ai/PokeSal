public class Treinador {
    private String nomeTreinador;
    private PokeSal pokesalInicial;
    private Mochila mochila;
    private boolean usouEsquivaNaRodada;
    private int itensUsadosNaBatalha;

    public Treinador(String nomeTreinador) {
        this.nomeTreinador = nomeTreinador;
        this.mochila = new Mochila();
        this.usouEsquivaNaRodada = false;
        this.itensUsadosNaBatalha = 0;
    }

    public String getNome() {
        return nomeTreinador;
    }

    public PokeSal getPokesalInicial() {
        return pokesalInicial;
    }

    public Mochila getMochila() {
        return mochila;
    }

    public int getItensUsadosNaBatalha() {
        return itensUsadosNaBatalha;
    }

    // O treinador escolhe o Pokesal inicial
    public void escolherInicial(PokeSal pokesal) {
        this.pokesalInicial = pokesal;
        System.out.println(nomeTreinador + " escolheu " + pokesal.getNome() + " como Pokesal inicial!");
    }

    // Usa um item da mochila no rodadaAtual informado.
    public void usarItem(Item item, int rodadaAtual) {
        boolean podeUsarItem = mochila.podeUsarItem(rodadaAtual, itensUsadosNaBatalha);
        if (!podeUsarItem) {
            return ;
        }
        item.aplicarEfeito(pokesalInicial);
        itensUsadosNaBatalha++;
    }

    public boolean esquivar() {
        if (usouEsquivaNaRodada) {
            System.out.println(nomeTreinador + " já esquivou nesta rodada!");
            return false;
        }
        usouEsquivaNaRodada = true;
        System.out.println(nomeTreinador + " esquivou do ataque!");
        return true;
    }

    public void reiniciarEsquiva() {
        usouEsquivaNaRodada = false;
    }

}
