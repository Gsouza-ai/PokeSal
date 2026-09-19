public class Golpe {

    private String nomeDoGolpe;
    private TipoElemental tipoElemental;
    private double poderBase;
    private double precisao; 
    

    public Golpe(String nomeDoGolpe, TipoElemental tipoElemental, double poderBase, double precisao) {
        this.nomeDoGolpe = nomeDoGolpe;
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
    public double executar(PokeSal atacante, PokeSal defensor, Terreno terreno) {

        //  testa se o golpe acerta, usando a precisão
        double sorteioPrecisao = Math.random();
        if (sorteioPrecisao > precisao) {
            System.out.println(atacante.getNome() + " usou " + nomeDoGolpe + ", mas errou o golpe!");
            return 0;
        }

        // calcula o dano base 
        double dano = poderBase + atacante.getAtk() - defensor.getDef();
        if (dano < 0) {
            dano = 0;
        }

        // aplica a vantagem elemental
        double multiplicador = tipoElemental.calcularMultiplicador(defensor.getTipo());
        dano = dano * multiplicador;

        //  aplica o efeito do terreno 
        if (terreno != null) {
            dano = terreno.aplicarEfeitoAtivo(this, dano);
        }

        //  aplica o dano no defensor
        defensor.receberDano(dano);

        System.out.println(atacante.getNome() + " usou " + nomeDoGolpe + " em " + defensor.getNome()
                + " e causou " + dano + " de dano!");

        return dano;
    }


}
