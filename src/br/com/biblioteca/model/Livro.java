package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public class Livro extends ItemAcervo {
    private String isbn;
    private Autor autor;
    private String editora;

    public Livro(int id, String titulo, int anoPublicacao, Categoria categoria, String isbn, Autor autor, String editora) throws ValidacaoException {
        super(id, titulo, anoPublicacao, categoria);
        setIsbn(isbn);
        setAutor(autor);
        setEditora(editora);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) throws ValidacaoException {
        String somenteNumeros = isbn == null ? "" : isbn.replaceAll("\\D", "");
        if (!(somenteNumeros.length() == 10 || somenteNumeros.length() == 13)) {
            throw new ValidacaoException("ISBN deve possuir 10 ou 13 dígitos.");
        }
        this.isbn = somenteNumeros;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) throws ValidacaoException {
        if (autor == null) {
            throw new ValidacaoException("Autor é obrigatório para livros.");
        }
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) throws ValidacaoException {
        if (editora == null || editora.trim().length() < 2) {
            throw new ValidacaoException("Editora deve possuir pelo menos 2 caracteres.");
        }
        this.editora = editora.trim();
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
        return dadosResumidos() + String.format(" | ISBN: %s | Autor: %s | Editora: %s | Renovações permitidas: %d", isbn, autor, editora, getMaximoRenovacoes());
    }
}
