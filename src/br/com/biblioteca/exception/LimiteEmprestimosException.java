package br.com.biblioteca.exception;

public class LimiteEmprestimosException extends OperacaoInvalidaException {
    public LimiteEmprestimosException(String mensagem) {
        super(mensagem);
    }
}
