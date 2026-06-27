package br.com.biblioteca.exception;

public class TransicaoEstadoInvalidaException extends OperacaoInvalidaException {
    public TransicaoEstadoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
