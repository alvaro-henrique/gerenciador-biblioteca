package br.com.biblioteca.model;

import java.time.LocalDate;

import br.com.biblioteca.exception.ValidacaoException;

public class Multa {
    private final int id;
    private final Usuario usuario;
    private final Emprestimo emprestimo;
    private final double valor;
    private final LocalDate dataGeracao;
    private boolean paga;
    private LocalDate dataPagamento;

    public Multa(int id, Usuario usuario, Emprestimo emprestimo, double valor, LocalDate dataGeracao) throws ValidacaoException {
        if (id <= 0) {
            throw new ValidacaoException("ID da multa deve ser maior que zero.");
        }
        if (usuario == null) {
            throw new ValidacaoException("Usuario da multa e obrigatorio.");
        }
        if (emprestimo == null) {
            throw new ValidacaoException("Emprestimo da multa e obrigatorio.");
        }
        if (valor <= 0) {
            throw new ValidacaoException("Valor da multa deve ser maior que zero.");
        }
        if (dataGeracao == null) {
            throw new ValidacaoException("Data de geracao da multa e obrigatoria.");
        }
        this.id = id;
        this.usuario = usuario;
        this.emprestimo = emprestimo;
        this.valor = valor;
        this.dataGeracao = dataGeracao;
        this.paga = false;
    }

    public Multa(int id, Usuario usuario, Emprestimo emprestimo, double valor, LocalDate dataGeracao, boolean paga,
            LocalDate dataPagamento) throws ValidacaoException {
        this(id, usuario, emprestimo, valor, dataGeracao);
        this.paga = paga;
        this.dataPagamento = dataPagamento;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getDataGeracao() {
        return dataGeracao;
    }

    public boolean isPaga() {
        return paga;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public boolean estaPendente() {
        return !paga;
    }

    public void pagar(LocalDate dataPagamento) throws ValidacaoException {
        if (paga) {
            throw new ValidacaoException("Esta multa ja foi paga.");
        }
        if (dataPagamento == null || dataPagamento.isBefore(dataGeracao)) {
            throw new ValidacaoException("Data de pagamento da multa e invalida.");
        }
        this.paga = true;
        this.dataPagamento = dataPagamento;
    }

    @Override
    public String toString() {
        return String.format("[%d] usuario: %s | emprestimo: %d | valor: R$ %.2f | gerada em: %s | status: %s | pagamento: %s",
                id,
                usuario.getNome(),
                emprestimo.getId(),
                valor,
                dataGeracao,
                paga ? "PAGA" : "PENDENTE",
                dataPagamento == null ? "-" : dataPagamento);
    }
}
