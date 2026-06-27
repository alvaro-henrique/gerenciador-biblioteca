package br.com.biblioteca.exception;

public class LivroNaoEncontradoException extends EntidadeNaoEncontradaException {
    public LivroNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
