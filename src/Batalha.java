public class Batalha {
   private Treinador treinador1;
    private Treinador treinador2;
    private Terreno terreno;
    private int rodadaAtual;

    public Batalha(Treinador treinador1, Treinador treinador2, Terreno terreno) {
        this.treinador1 = treinador1;
        this.treinador2 = treinador2;
        this.terreno = terreno;
        this.rodadaAtual = 1; // começa na rodada 1
    }

    public int getRodadaAtual() {
        return rodadaAtual;
    }

    public Terreno getTerreno() {
        return terreno;
    }

    // Anuncia o começo da batalha.
    public void iniciarBatalha() {
        System.out.println("=== Batalha iniciada! ===");
        System.out.println(treinador1.getNome() + " (" + treinador1.getPokesalInicial().getNome() + ")"
                + " VS " + treinador2.getNome() + " (" + treinador2.getPokesalInicial().getNome() + ")");
        System.out.println("Terreno: " + terreno);
    }

    // Decide quem ataca primeiro, olhando o SPD (velocidade) de cada Pokesal.
    public PokeSal determinarIniciativa() {
        PokeSal pokesal1 = treinador1.getPokesalInicial();
        PokeSal pokesal2 = treinador2.getPokesalInicial();

        if (pokesal1.getSpd() >= pokesal2.getSpd()) {
            return pokesal1;
        } else {
            return pokesal2;
        }
    }

    // Executa uma rodada simples: quem tem mais SPD ataca primeiro com o golpe indicado.
    public void proximoTurno(Golpe golpeTreinador1, Golpe golpeTreinador2) {

        PokeSal pokesal1 = treinador1.getPokesalInicial();
        PokeSal pokesal2 = treinador2.getPokesalInicial();

        System.out.println("\n--- Rodada " + rodadaAtual + " ---");

        PokeSal primeiro = determinarIniciativa();

        if (primeiro == pokesal1) {
            pokesal1.atacar(pokesal2, golpeTreinador1, terreno);
            if (pokesal2.estaVivo()) {
                pokesal2.atacar(pokesal1, golpeTreinador2, terreno);
            }
        } else {
            pokesal2.atacar(pokesal1, golpeTreinador2, terreno);
            if (pokesal1.estaVivo()) {
                pokesal1.atacar(pokesal2, golpeTreinador1, terreno);
            }
        }

        aplicarEfeitosFimDeTurno();

        // reseta coisas que valem só para 1 rodada
        pokesal1.setExaustoTurnoAtual(false);
        pokesal2.setExaustoTurnoAtual(false);
        treinador1.reiniciarEsquiva();
        treinador2.reiniciarEsquiva();

        rodadaAtual++;
    }

    // Aplica status (queimado, envenenado, paralisado) e efeito de terreno no final do turno.
    public void aplicarEfeitosFimDeTurno() {
        PokeSal pokesal1 = treinador1.getPokesalInicial();
        PokeSal pokesal2 = treinador2.getPokesalInicial();

        aplicarStatusEmPokesal(pokesal1);
        aplicarStatusEmPokesal(pokesal2);

        terreno.aplicarEfeitoPassivo(pokesal1);
        terreno.aplicarEfeitoPassivo(pokesal2);
    }

    private void aplicarStatusEmPokesal(PokeSal pokesal) {
        for (int i = 0; i < pokesal.getStatus().size(); i++) {
            StatusEffect efeito = pokesal.getStatus().get(i);
            efeito.aplicarEfeito(pokesal);
        }
    }

    // Verifica se a batalha já acabou (algum Pokesal morreu).
    public boolean verificarFimBatalha() {
        PokeSal pokesal1 = treinador1.getPokesalInicial();
        PokeSal pokesal2 = treinador2.getPokesalInicial();

        if (!pokesal1.estaVivo()) {
            System.out.println("\n" + treinador2.getNome() + " venceu a batalha!");
            return true;
        }
        if (!pokesal2.estaVivo()) {
            System.out.println("\n" + treinador1.getNome() + " venceu a batalha!");
            return true;
        }
        return false;
    }
}

