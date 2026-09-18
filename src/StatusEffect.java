public enum StatusEffect {
    QUEIMADO,
    ENVENENADO,
    PARALISADO;

    public void aplicarEfeito(Pokesal pokesal) {

        if (this == QUEIMADO) {
            double danoQueimadura = pokesal.getHpMaximo() * 0.05; // queimado perde HP e tem o ataque reduzido em 5%
            pokesal.receberDano(danoQueimadura);
            System.out.println(pokesal.getNome() + " sofreu " + danoQueimadura + " de dano da queimadura!");
        }

        if (this == ENVENENADO) { 
            double danoVeneno = pokesal.getHpMaximo() * 0.08;// veneno tira um dano progressivo de 8%
            pokesal.receberDano(danoVeneno);
            System.out.println(pokesal.getNome() + " sofreu " + danoVeneno + " de dano do veneno!");
        }

        if (this == PARALISADO) {
            pokesal.setSpd(pokesal.getSpd() * 0.5); // paralisado reduz a velocidade em 50%
            System.out.println(pokesal.getNome() + " está paralisado e ficou mais lento!");
        }
    }
}
