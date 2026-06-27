package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public class Autor {
    private final int id;
    private String nome;
    private String nacionalidade;

    public Autor(int id, String nome, String nacionalidade) throws ValidacaoException {
        if (id <= 0) {
            throw new ValidacaoException("ID do autor deve ser maior que zero.");
        }
        this.id = id;
        setNome(nome);
        setNacionalidade(nacionalidade);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws ValidacaoException {
        if (nome == null || nome.trim().length() < 3) {
            throw new ValidacaoException("Nome do autor deve possuir pelo menos 3 caracteres.");
        }
        this.nome = nome.trim();
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) throws ValidacaoException {
        if (nacionalidade == null || nacionalidade.trim().isEmpty()) {
            throw new ValidacaoException("Nacionalidade do autor é obrigatória.");
        }
        this.nacionalidade = nacionalidade.trim();
    }

    @Override
    public String toString() {
        return nome + " (" + nacionalidade + ")";
    }
}
