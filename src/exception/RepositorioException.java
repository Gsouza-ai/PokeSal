package exception;

/**
 * Exceção lançada quando o repositório de resultados falha ao salvar ou listar.
 **/
public class RepositorioException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Método construtor.
     *
     * @param mensagem mensagem explicando o motivo da falha.
     **/
    public RepositorioException(String mensagem) {
        super(mensagem);
    }

    /**
     * Método construtor com causa.
     *
     * @param mensagem mensagem explicando o motivo da falha.
     * @param causa exceção original.
     **/
    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
