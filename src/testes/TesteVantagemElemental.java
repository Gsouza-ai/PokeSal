package testes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import main.TipoElemental;

public class TesteVantagemElemental {
	
	// Substitua pelos valores reais que seu sistema usa
    private static final float SUPER_EFETIVO = 2.0f; 
    private static final float POUCO_EFETIVO = 0.5f;
    private static final float NORMAL = 1.0f;
    

    @Test
    @DisplayName("Deve aplicar dano Super Efetivo quando o atacante tem vantagem")
    public void testDanoSuperEfetivo() {
        assertEquals(SUPER_EFETIVO, TipoElemental.FOGO.calcularMultiplicador(TipoElemental.PLANTA));
        assertEquals(SUPER_EFETIVO, TipoElemental.AGUA.calcularMultiplicador(TipoElemental.FOGO));
        assertEquals(SUPER_EFETIVO, TipoElemental.PLANTA.calcularMultiplicador(TipoElemental.AGUA));
    }

    @Test
    @DisplayName("Deve aplicar dano Pouco Efetivo quando o atacante tem desvantagem")
    public void testDanoPoucoEfetivo() {
        assertEquals(POUCO_EFETIVO, TipoElemental.FOGO.calcularMultiplicador(TipoElemental.AGUA));
        assertEquals(POUCO_EFETIVO, TipoElemental.AGUA.calcularMultiplicador(TipoElemental.PLANTA));
        assertEquals(POUCO_EFETIVO, TipoElemental.PLANTA.calcularMultiplicador(TipoElemental.FOGO));
    }

    @Test
    @DisplayName("Deve aplicar dano Normal quando os tipos forem iguais")
    public void testDanoNormalTiposIguais() {
        assertEquals(NORMAL, TipoElemental.FOGO.calcularMultiplicador(TipoElemental.FOGO));
        assertEquals(NORMAL, TipoElemental.AGUA.calcularMultiplicador(TipoElemental.AGUA));
        assertEquals(NORMAL, TipoElemental.PLANTA.calcularMultiplicador(TipoElemental.PLANTA));
    }
	

}
