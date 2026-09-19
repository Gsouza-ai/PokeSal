    public enum TipoElemental {
    AGUA,
    FOGO,
    PLANTA;

    private double danoSueperEfetivo= 2.0;
    private double danoPoucoEfetivo= 0.5;
    private double danoNormal= 1.0;
   
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
            return danoPoucoEfetivo;
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

