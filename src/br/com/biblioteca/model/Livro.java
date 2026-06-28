package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public class Livro extends ItemAcervo {
    private String isbn;
    private String nomeAutor;

    public Livro(int id, String titulo, int anoPublicacao, Categoria categoria, String isbn, String nomeAutor) throws ValidacaoException {
        super(id, titulo, anoPublicacao, categoria);
        setIsbn(isbn);
        setNomeAutor(nomeAutor);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) throws ValidacaoException {
        String somenteNumeros = isbn == null ? "" : isbn.replaceAll("\\D", "");
        if (!(somenteNumeros.length() == 10 || somenteNumeros.length() == 13)) {
            throw new ValidacaoException("ISBN deve possuir 10 ou 13 digitos.");
        }
        this.isbn = somenteNumeros;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) throws ValidacaoException {
        if (nomeAutor == null || nomeAutor.trim().length() < 3) {
            throw new ValidacaoException("Nome do autor deve possuir pelo menos 3 caracteres.");
        }
        this.nomeAutor = nomeAutor.trim();
    }

    @Override
    public String getTipo() {
        return "Livro";
    }

    @Override
    public boolean permiteRenovacao() {
        return true;
    }

    @Override
    public int getMaximoRenovacoes() {
        return 2;
    }

    @Override
    public String toString() {
        return dadosResumidos() + String.format(" | ISBN: %s | Autor: %s | Renovacoes permitidas: %d", isbn, nomeAutor,
                getMaximoRenovacoes());
    }
}
