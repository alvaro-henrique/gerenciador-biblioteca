package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public class Categoria {
    private final int id;
    private String nome;
    private String descricao;

    public Categoria(int id, String nome, String descricao) throws ValidacaoException {
        if (id <= 0) {
            throw new ValidacaoException("ID da categoria deve ser maior que zero.");
        }
        this.id = id;
        setNome(nome);
        setDescricao(descricao);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws ValidacaoException {
        if (nome == null || nome.trim().length() < 2) {
            throw new ValidacaoException("Nome da categoria deve possuir pelo menos 2 caracteres.");
        }
        this.nome = nome.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) throws ValidacaoException {
        if (descricao == null) {
            throw new ValidacaoException("Descrição da categoria não pode ser nula.");
        }
        this.descricao = descricao.trim();
    }

    @Override
    public String toString() {
        return nome;
    }
}
