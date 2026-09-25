package exception;

/**
 * Exceção lançada quando o treinador tenta usar mais itens
 * do que o limite permitido por batalha.
 **/
public class LimiteDeItensExcedidoException extends RuntimeException{
    /**
     * Método construtor.
     *
     * @param mensagem mensagem explicando o motivo da exceção.
     **/
    public LimiteDeItensExcedidoException(String mensagem) {
        super(mensagem);
    }
}
