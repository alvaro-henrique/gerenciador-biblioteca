package br.com.biblioteca.exception;

public class PersistenciaException extends BibliotecaException {
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
