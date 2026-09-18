    public enum TipoElemental {
    AGUA,
    FOGO,
    PLANTA
    private float danoSueperEfetivo= 2.0
    private float danoPoucoEfetivo= 0.5
    private float danoNormal= 1.0
   
        // Esse método calcula o multiplicador de dano
    public double calcularMultiplicador(TipoElemental defensor) {
       
        // quando "this" ataca o "defensor".
        if (this == FOGO && defensor == PLANTA) {
            return danoSueperEfetivo; 
        }
        if (this == FOGO && defensor == AGUA) {
            return danoPoucoEfetivo; 
        }

        if (this == AGUA && defensor == FOGO) {
            return danoSueperEfetivo; 
        }
        if (this == AGUA && defensor == PLANTA) {
            return danoPoucoEfetivo5; 
        }

        if (this == PLANTA && defensor == AGUA) {
            return danoSueperEfetivo; 
        }
        if (this == PLANTA && defensor == FOGO) {
            return danoPoucoEfetivo; 
        }

        // se não é nenhuma dessas combinações dano normal
        return danoNormal;
    }
}
}
