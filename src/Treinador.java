public class Treinador {
    private String nome;
    private PokeSal pokesalInicial;
    private Mochila mochila;
    private boolean usouEsquivaNaRodada;
    private int itensUsadosNaBatalha;

    public Treinador(String nome) {
        this.nome = nome;
        this.mochila = new Mochila();
        this.usouEsquivaNaRodada = false;
        this.itensUsadosNaBatalha = 0;
    }

    public String getNome() {
        return nome;
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

    // O treinador escolhe o Pokesal inicial (regra: só 1).
    public void escolherInicial(PokeSal pokesal) {
        this.pokesalInicial = pokesal;
        System.out.println(nome + " escolheu " + pokesal.getNome() + " como Pokesal inicial!");
    }

    // Usa um item da mochila no rodadaAtual informado.
    public void usarItem(Item item, int rodadaAtual) {
        boolean pode = mochila.podeUsarItem(rodadaAtual, itensUsadosNaBatalha); //Criar método na classe mochila
        if (!pode) {
            return;
        }
        item.aplicarEfeito(pokesalInicial); //criar método na classe Item
        itensUsadosNaBatalha++;
        // usar item consome o turno do treinador (regra do enunciado)
    }

    // Regra: cada treinador pode esquivar de 1 ataque por rodada.
    public boolean esquivar() {
        if (usouEsquivaNaRodada) {
            System.out.println(nome + " já esquivou nesta rodada!");
            return false;
        }
        usouEsquivaNaRodada = true;
        System.out.println(nome + " esquivou do ataque!");
        return true;
    }

    // Reseta a esquiva no começo de cada rodada nova.
    public void reiniciarEsquiva() {
        usouEsquivaNaRodada = false;
    }

}
