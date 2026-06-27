package br.com.biblioteca.persistence;

import br.com.biblioteca.exception.BibliotecaException;
import br.com.biblioteca.exception.PersistenciaException;
import br.com.biblioteca.service.Biblioteca;

public abstract class Persistencia {
    public abstract void salvar(Biblioteca biblioteca) throws PersistenciaException;

    public abstract void carregar(Biblioteca biblioteca) throws BibliotecaException;
}
