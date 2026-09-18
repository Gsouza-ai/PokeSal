public abstract class Item {
    private String nome;

    public Item(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    // Cada item concreto decide o que acontece quando é usado.
    public abstract void aplicarEfeito(PokeSal pokesal);

}
