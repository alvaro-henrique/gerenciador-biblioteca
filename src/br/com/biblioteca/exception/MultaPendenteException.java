package br.com.biblioteca.exception;

public class MultaPendenteException extends OperacaoInvalidaException {
    public MultaPendenteException(String mensagem) {
        super(mensagem);
    }
}
