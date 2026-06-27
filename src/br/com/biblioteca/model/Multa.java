package br.com.biblioteca.model;

import java.time.LocalDate;

import br.com.biblioteca.exception.OperacaoInvalidaException;
import br.com.biblioteca.exception.ValidacaoException;

public class Multa {
    private final int id;
    private final Usuario usuario;
    private final Emprestimo emprestimo;
    private final LocalDate dataGeracao;
    private final int diasAtraso;
    private final double valor;
    private boolean paga;
    private LocalDate dataPagamento;

    public Multa(int id, Usuario usuario, Emprestimo emprestimo, LocalDate dataGeracao, int diasAtraso, double valor)
            throws ValidacaoException {
        if (id <= 0) {
            throw new ValidacaoException("ID da multa deve ser maior que zero.");
        }
        if (usuario == null) {
            throw new ValidacaoException("Usuario da multa e obrigatorio.");
        }
        if (emprestimo == null) {
            throw new ValidacaoException("Emprestimo da multa e obrigatorio.");
        }
        if (dataGeracao == null) {
            throw new ValidacaoException("Data de geracao da multa e obrigatoria.");
        }
        if (diasAtraso <= 0) {
            throw new ValidacaoException("Dias de atraso da multa devem ser maiores que zero.");
        }
        if (valor <= 0) {
            throw new ValidacaoException("Valor da multa deve ser maior que zero.");
        }
        this.id = id;
        this.usuario = usuario;
        this.emprestimo = emprestimo;
        this.dataGeracao = dataGeracao;
        this.diasAtraso = diasAtraso;
        this.valor = valor;
        this.paga = false;
    }

    public Multa(int id, Usuario usuario, Emprestimo emprestimo, LocalDate dataGeracao, int diasAtraso, double valor,
            boolean paga, LocalDate dataPagamento) throws ValidacaoException {
        this(id, usuario, emprestimo, dataGeracao, diasAtraso, valor);
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

    public LocalDate getDataGeracao() {
        return dataGeracao;
    }

    public int getDiasAtraso() {
        return diasAtraso;
    }

    public double getValor() {
        return valor;
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

    public void registrarPagamento(LocalDate dataPagamento) throws OperacaoInvalidaException {
        if (paga) {
            throw new OperacaoInvalidaException("Esta multa ja foi paga.");
        }
        if (dataPagamento == null || dataPagamento.isBefore(dataGeracao)) {
            throw new OperacaoInvalidaException("Data de pagamento da multa invalida.");
        }
        this.paga = true;
        this.dataPagamento = dataPagamento;
    }

    @Override
    public String toString() {
        return String.format("[%d] multa do usuario %s | emprestimo: %d | dias: %d | valor: R$ %.2f | status: %s | pagamento: %s",
                id,
                usuario.getNome(),
                emprestimo.getId(),
                diasAtraso,
                valor,
                paga ? "PAGA" : "PENDENTE",
                dataPagamento == null ? "-" : dataPagamento);
    }
}
