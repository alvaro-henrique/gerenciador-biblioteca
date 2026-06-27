package br.com.biblioteca.model;

import br.com.biblioteca.exception.LivroIndisponivelException;
import br.com.biblioteca.exception.ValidacaoException;

public abstract class ItemAcervo {
    private final int id;
    private String titulo;
    private int anoPublicacao;
    private Categoria categoria;
    private boolean disponivel;

    public ItemAcervo(int id, String titulo, int anoPublicacao, Categoria categoria) throws ValidacaoException {
        if (id <= 0) {
            throw new ValidacaoException("ID do item deve ser maior que zero.");
        }
        this.id = id;
        setTitulo(titulo);
        setAnoPublicacao(anoPublicacao);
        setCategoria(categoria);
        this.disponivel = true;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) throws ValidacaoException {
        if (titulo == null || titulo.trim().length() < 2) {
            throw new ValidacaoException("Titulo deve possuir pelo menos 2 caracteres.");
        }
        this.titulo = titulo.trim();
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) throws ValidacaoException {
        int anoAtual = java.time.LocalDate.now().getYear();
        if (anoPublicacao < 1400 || anoPublicacao > anoAtual) {
            throw new ValidacaoException("Ano de publicacao deve estar entre 1400 e " + anoAtual + ".");
        }
        this.anoPublicacao = anoPublicacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) throws ValidacaoException {
        if (categoria == null) {
            throw new ValidacaoException("Categoria e obrigatoria.");
        }
        this.categoria = categoria;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void definirDisponibilidade(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void emprestar() throws LivroIndisponivelException {
        if (!disponivel) {
            throw new LivroIndisponivelException("Item ja esta emprestado ou indisponivel.");
        }
        this.disponivel = false;
    }

    public void devolver() {
        this.disponivel = true;
    }

    public abstract String getTipo();

    public abstract boolean permiteRenovacao();

    public abstract int getMaximoRenovacoes();

    public String dadosResumidos() {
        return String.format("[%d] %s - %s (%d) | Categoria: %s | %s",
                id,
                getTipo(),
                titulo,
                anoPublicacao,
                categoria.getNome(),
                disponivel ? "DISPONIVEL" : "INDISPONIVEL");
    }
}
