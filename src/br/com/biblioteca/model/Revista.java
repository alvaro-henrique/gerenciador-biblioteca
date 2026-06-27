package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public class Revista extends ItemAcervo {
    private String issn;
    private int numeroEdicao;
    private String periodicidade;

    public Revista(int id, String titulo, int anoPublicacao, Categoria categoria, String issn, int numeroEdicao, String periodicidade) throws ValidacaoException {
        super(id, titulo, anoPublicacao, categoria);
        setIssn(issn);
        setNumeroEdicao(numeroEdicao);
        setPeriodicidade(periodicidade);
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) throws ValidacaoException {
        String somenteNumeros = issn == null ? "" : issn.replaceAll("\\D", "");
        if (somenteNumeros.length() != 8) {
            throw new ValidacaoException("ISSN deve possuir 8 dígitos.");
        }
        this.issn = somenteNumeros;
    }

    public int getNumeroEdicao() {
        return numeroEdicao;
    }

    public void setNumeroEdicao(int numeroEdicao) throws ValidacaoException {
        if (numeroEdicao <= 0) {
            throw new ValidacaoException("Número da edição deve ser maior que zero.");
        }
        this.numeroEdicao = numeroEdicao;
    }

    public String getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(String periodicidade) throws ValidacaoException {
        if (periodicidade == null || periodicidade.trim().isEmpty()) {
            throw new ValidacaoException("Periodicidade da revista é obrigatória.");
        }
        this.periodicidade = periodicidade.trim();
    }

    @Override
    public String getTipo() {
        return "Revista";
    }

    @Override
    public boolean permiteRenovacao() {
        return false;
    }

    @Override
    public int getMaximoRenovacoes() {
        return 0;
    }

    @Override
    public String toString() {
        return dadosResumidos() + String.format(" | ISSN: %s | Edição: %d | Periodicidade: %s | Não permite renovação", issn, numeroEdicao, periodicidade);
    }
}
