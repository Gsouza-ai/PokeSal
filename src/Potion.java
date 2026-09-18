public class Potion extends Item{
    public Potion() {
        super("Potion");
    }

    @Override
    public void aplicarEfeito(PokeSal pokesal) {
        pokesal.curar(20); // cura 20 de HP
        System.out.println(pokesal.getNome() + " usou Potion e curou 20 de HP!");
    }
}
