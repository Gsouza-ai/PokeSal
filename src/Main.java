
public class Main {
    public static void main(String[] args) {
         // Cria os dois treinadores
        Treinador treinador1 = new Treinador("Ana");
        Treinador treinador2 = new Treinador("Bruno");

        // Cria os Pokesal iniciais 
        PokeSal charSal = new PokeSal("CharSal", 100, 20, 10, 15, TipoElemental.FOGO);
        PokeSal squirtSal = new PokeSal("SquirtSal", 100, 18, 12, 12, TipoElemental.AGUA);

        // Cada Pokesal ganha alguns golpes
        Golpe labareda = new Golpe("Labareda", TipoElemental.FOGO, 15, 0.9);
        Golpe jatoDeAgua = new Golpe("Jato De Agua", TipoElemental.AGUA, 15, 0.9);
        Golpe tsunami = new Tsunami();

        charSal.adicionarGolpe(labareda);
        squirtSal.adicionarGolpe(jatoDeAgua);
        squirtSal.adicionarGolpe(tsunami);

        // Cada treinador escolhe seu inicial 
        treinador1.escolherInicial(charSal);
        treinador2.escolherInicial(squirtSal);

        // Escolhe o terreno da batalha 
        Terreno terreno = Terreno.ASFALTO_QUENTE;

        // Coloca uns itens na mochila de cada treinador
        treinador1.getMochila().adicionarItem(new Potion());
        treinador2.getMochila().adicionarItem(new SuperPotion());

        // Cria e inicia a batalha 
        Batalha batalha = new Batalha(treinador1, treinador2, terreno);
        batalha.iniciarBatalha();

        // Roda algumas rodadas até alguém vencer 
        int limiteDeRodadasDeSeguranca = 10;

        while (!batalha.verificarFimBatalha() && batalha.getRodadaAtual() <= limiteDeRodadasDeSeguranca) {

            // a partir da rodada 2 o treinador2 usa um item em vez de atacar
            if (batalha.getRodadaAtual() == 2) {
                treinador2.usarItem(new SuperPotion(), batalha.getRodadaAtual());
            }

            // caso de uso Realizar golpe cada um escolhe um golpe para usar na rodada
            batalha.proximoTurno(labareda, jatoDeAgua);

            System.out.println("HP " + charSal.getNome() + ": " + charSal.getHpAtual());
            System.out.println("HP " + squirtSal.getNome() + ": " + squirtSal.getHpAtual());
        }

        System.out.println("\n=== Fim da batalha ===");
    }
}



    

