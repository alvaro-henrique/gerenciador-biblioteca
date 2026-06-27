package br.com.biblioteca.exception;

public class LivroIndisponivelException extends OperacaoInvalidaException {
    public LivroIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
