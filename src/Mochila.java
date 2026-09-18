import java.util.ArrayList;
import java.util.List;

public class Mochila {

    private List<Item> itens;
    private int limitePorBatalha; 

    public Mochila() {
        this.itens = new ArrayList<Item>();
        this.limitePorBatalha = 2; 
    }

    public List<Item> getItens() {
        return itens;
    }

    public void adicionarItem(Item item) {
        itens.add(item);
    }

    public void removerItem(Item item) {
        itens.remove(item);
    }

    // Verifica se ainda dá pra usar item:
    public boolean podeUsarItem(int rodadaAtual, int itensUsadosNaBatalha) {
        if (rodadaAtual < 2) {
            System.out.println("Itens só podem ser usados a partir da 2ª rodada!");
            return false;
        }
        if (itensUsadosNaBatalha >= limitePorBatalha) {
            System.out.println("Limite de itens por batalha já foi atingido!");
            return false;
        }
        return true;
    }
}
