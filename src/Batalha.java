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
    public Pokesal determinarIniciativa() {
        Pokesal p1 = treinador1.getPokesalInicial();
        Pokesal p2 = treinador2.getPokesalInicial();

        if (p1.getSpd() >= p2.getSpd()) {
            return p1;
        } else {
            return p2;
        }
    }

    // Executa uma rodada simples: quem tem mais SPD ataca primeiro com o golpe indicado.
    public void proximoTurno(Golpe golpeTreinador1, Golpe golpeTreinador2) {

        Pokesal p1 = treinador1.getPokesalInicial();
        Pokesal p2 = treinador2.getPokesalInicial();

        System.out.println("\n--- Rodada " + rodadaAtual + " ---");

        Pokesal primeiro = determinarIniciativa();

        if (primeiro == p1) {
            p1.atacar(p2, golpeTreinador1, terreno);
            if (p2.estaVivo()) {
                p2.atacar(p1, golpeTreinador2, terreno);
            }
        } else {
            p2.atacar(p1, golpeTreinador2, terreno);
            if (p1.estaVivo()) {
                p1.atacar(p2, golpeTreinador1, terreno);
            }
        }

        aplicarEfeitosFimDeTurno();

        // reseta coisas que valem só para 1 rodada
        p1.setExaustoTurnoAtual(false);
        p2.setExaustoTurnoAtual(false);
        treinador1.reiniciarEsquiva();
        treinador2.reiniciarEsquiva();

        rodadaAtual++;
    }

    // Aplica status (queimado, envenenado, paralisado) e efeito de terreno no final do turno.
    public void aplicarEfeitosFimDeTurno() {
        Pokesal p1 = treinador1.getPokesalInicial();
        Pokesal p2 = treinador2.getPokesalInicial();

        aplicarStatusEmPokesal(p1);
        aplicarStatusEmPokesal(p2);

        terreno.aplicarEfeitoPassivo(p1);
        terreno.aplicarEfeitoPassivo(p2);
    }

    private void aplicarStatusEmPokesal(Pokesal p) {
        for (int i = 0; i < p.getStatus().size(); i++) {
            StatusEffect efeito = p.getStatus().get(i);
            efeito.aplicarEfeito(p);
        }
    }

    // Verifica se a batalha já acabou (algum Pokesal morreu).
    public boolean verificarFimBatalha() {
        Pokesal p1 = treinador1.getPokesalInicial();
        Pokesal p2 = treinador2.getPokesalInicial();

        if (!p1.estaVivo()) {
            System.out.println("\n" + treinador2.getNome() + " venceu a batalha!");
            return true;
        }
        if (!p2.estaVivo()) {
            System.out.println("\n" + treinador1.getNome() + " venceu a batalha!");
            return true;
        }
        return false;
    }
}
}
