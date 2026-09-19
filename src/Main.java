
public class Main {
    public static void main(String[] args) {
         // 1. Cria os dois treinadores
        Treinador treinador1 = new Treinador("Ana");
        Treinador treinador2 = new Treinador("Bruno");

        // 2. Cria os Pokesal iniciais (poderia ser BulbaSal, CharSal, SquirtSal, etc)
        Pokesal charSal = new Pokesal("CharSal", 100, 20, 10, 15, TipoElemental.FOGO);
        Pokesal squirtSal = new Pokesal("SquirtSal", 100, 18, 12, 12, TipoElemental.AGUA);

        // 3. Cada Pokesal ganha alguns golpes
        Golpe labareda = new Golpe("Labareda", TipoElemental.FOGO, 15, 0.9);
        Golpe jatoDagua = new Golpe("Jato d'Agua", TipoElemental.AGUA, 15, 0.9);
        Golpe tsunami = new Tsunami();

        charSal.adicionarGolpe(labareda);
        squirtSal.adicionarGolpe(jatoDagua);
        squirtSal.adicionarGolpe(tsunami);

        // 4. Cada treinador escolhe seu inicial (caso de uso "Escolher PokeSal")
        treinador1.escolherInicial(charSal);
        treinador2.escolherInicial(squirtSal);

        // 5. Escolhe o terreno da batalha (caso de uso "Escolher terreno")
        Terreno terreno = Terreno.ASFALTO_QUENTE;

        // 6. Coloca uns itens na mochila de cada treinador
        treinador1.getMochila().adicionarItem(new Potion());
        treinador2.getMochila().adicionarItem(new SuperPotion());

        // 7. Cria e inicia a batalha (caso de uso "Participar da batalha")
        Batalha batalha = new Batalha(treinador1, treinador2, terreno);
        batalha.iniciarBatalha();

        // 8. Roda algumas rodadas até alguém vencer (ou até um limite de segurança)
        int limiteDeRodadasDeSeguranca = 10;

        while (!batalha.verificarFimBatalha() && batalha.getRodadaAtual() <= limiteDeRodadasDeSeguranca) {

            // exemplo: a partir da rodada 2, o treinador2 usa um item em vez de atacar
            if (batalha.getRodadaAtual() == 2) {
                treinador2.usarItem(new SuperPotion(), batalha.getRodadaAtual());
            }

            // caso de uso "Realizar golpe": cada um escolhe um golpe para usar na rodada
            batalha.proximoTurno(labareda, jatoDagua);

            System.out.println("HP " + charSal.getNome() + ": " + charSal.getHpAtual());
            System.out.println("HP " + squirtSal.getNome() + ": " + squirtSal.getHpAtual());
        }

        System.out.println("\n=== Fim da batalha ===");
    }
}



    }
}
