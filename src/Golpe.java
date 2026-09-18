public class Golpe {

    private String nomeDoGolpe;
    private TipoElemental tipoElemental;
    private double poderBase;
    private double precisao; // chance de acertar, de 0.0 a 1.0   
    

    public Golpe(String nomeDoGolpe, TipoElemental tipoElemental, double poderBase, double precisao) {
        this.nome = nomeDoGolpe;
        this.tipoElemental = tipoElemental;
        this.poderBase = poderBase;
        this.precisao = precisao;
    }

    public String getNome() {
        return nomeDoGolpe;
    }

    public TipoElemental getTipo() {
        return tipoElemental;
    }

    public double getPoderBase() {
        return poderBase;
    }

    public double getPrecisao() {
        return precisao;
    }

    // Executa o golpe e devolve o dano final causado e classe tsunami sobrescreve esse metodo
    public double executar(Pokesal atacante, Pokesal defensor, Terreno terreno) {

        // 1. testa se o golpe acerta, usando a precisão
        double sorteio = Math.random();
        if (sorteio > precisao) {
            System.out.println(atacante.getNome() + " usou " + nome + ", mas errou o golpe!");
            return 0;
        }

        // 2. calcula o dano base (bem simples: poder do golpe + ataque - defesa)
        double dano = poderBase + atacante.getAtk() - defensor.getDef();
        if (dano < 0) {
            dano = 0;
        }

        // 3. aplica a vantagem elemental
        double multiplicador = tipo.calcularMultiplicador(defensor.getTipo());
        dano = dano * multiplicador;

        // 4. aplica o efeito do terreno (se tiver)
        if (terreno != null) {
            dano = terreno.aplicarEfeitoAtivo(this, dano);
        }

        // 5. aplica o dano no defensor
        defensor.receberDano(dano);

        System.out.println(atacante.getNome() + " usou " + nome + " em " + defensor.getNome()
                + " e causou " + dano + " de dano!");

        return dano;
    }
}
