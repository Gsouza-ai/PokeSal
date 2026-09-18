    public enum Terreno {
    ASFALTO_QUENTE,
    POCA_CHUVA,
    CANTEIRO_CENTRAL

    // verifica se o terreno é do mesmo tipo do pokesal e aplica o efeito
    public double aplicarEfeitoAtivo(Golpe golpe, double danoBase) {

        if (this == ASFALTO_QUENTE && golpe.getTipo() == TipoElemental.FOGO) {
            return danoBase * 1.15; 
        }

        if (this == POCA_CHUVA && golpe.getTipo() == TipoElemental.AGUA) {
            return danoBase * 1.10; 
        }

        return danoBase;
    }

    // Efeito que acontece em cada pokesal tipo planta.
        public void aplicarEfeitoPassivo(Pokesal pokesal) {
        if (this == CANTEIRO_CENTRAL && pokesal.getTipo() == TipoElemental.PLANTA) {
            double cura = pokesal.getHpMaximo() * 0.05; 
            pokesal.curar(cura);
            System.out.println(pokesal.getNome() + " recuperou " + cura + " de HP com o Canteiro Central!");
        }
    }
}

}
