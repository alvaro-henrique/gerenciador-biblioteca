package br.com.biblioteca.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import br.com.biblioteca.exception.TransicaoEstadoInvalidaException;
import br.com.biblioteca.exception.ValidacaoException;

public class Emprestimo {
    private final int id;
    private final Usuario usuario;
    private final ItemAcervo item;
    private final LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private LocalDate dataUltimaRenovacao;
    private EstadoEmprestimo estado;
    private int renovacoes;

    public Emprestimo(int id, Usuario usuario, ItemAcervo item, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao)
            throws ValidacaoException {
        if (id <= 0) {
            throw new ValidacaoException("ID do emprestimo deve ser maior que zero.");
        }
        if (usuario == null) {
            throw new ValidacaoException("Usuario do emprestimo e obrigatorio.");
        }
        if (item == null) {
            throw new ValidacaoException("Item do emprestimo e obrigatorio.");
        }
        if (dataEmprestimo == null || dataPrevistaDevolucao == null || dataPrevistaDevolucao.isBefore(dataEmprestimo)) {
            throw new ValidacaoException("Datas do emprestimo sao invalidas.");
        }
        this.id = id;
        this.usuario = usuario;
        this.item = item;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataUltimaRenovacao = null;
        this.estado = EstadoEmprestimo.ATIVO;
        this.renovacoes = 0;
    }

    public Emprestimo(int id, Usuario usuario, ItemAcervo item, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao,
            LocalDate dataDevolucao, EstadoEmprestimo estado, int renovacoes) throws ValidacaoException {
        this(id, usuario, item, dataEmprestimo, dataPrevistaDevolucao, dataDevolucao, estado, renovacoes, null);
    }

    public Emprestimo(int id, Usuario usuario, ItemAcervo item, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao,
            LocalDate dataDevolucao, EstadoEmprestimo estado, int renovacoes, LocalDate dataUltimaRenovacao) throws ValidacaoException {
        this(id, usuario, item, dataEmprestimo, dataPrevistaDevolucao);
        this.dataDevolucao = dataDevolucao;
        this.dataUltimaRenovacao = dataUltimaRenovacao;
        this.estado = estado;
        this.renovacoes = renovacoes;
        if (estado == EstadoEmprestimo.ATIVO || estado == EstadoEmprestimo.ATRASADO) {
            item.definirDisponibilidade(false);
        }
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public ItemAcervo getItem() {
        return item;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public LocalDate getDataUltimaRenovacao() {
        return dataUltimaRenovacao;
    }

    public EstadoEmprestimo getEstado() {
        return estado;
    }

    public int getRenovacoes() {
        return renovacoes;
    }

    public void atualizarEstadoPorData(LocalDate hoje) {
        if (estado == EstadoEmprestimo.ATIVO && hoje.isAfter(dataPrevistaDevolucao)) {
            estado = EstadoEmprestimo.ATRASADO;
        }
    }

    public void renovar(LocalDate hoje) throws TransicaoEstadoInvalidaException {
        atualizarEstadoPorData(hoje);
        if (estado != EstadoEmprestimo.ATIVO) {
            throw new TransicaoEstadoInvalidaException("Somente emprestimos ativos e dentro do prazo podem ser renovados.");
        }
        if (!item.permiteRenovacao()) {
            throw new TransicaoEstadoInvalidaException("Este tipo de item nao permite renovacao.");
        }
        if (renovacoes >= item.getMaximoRenovacoes()) {
            throw new TransicaoEstadoInvalidaException("Limite de renovacoes atingido para este item.");
        }
        renovacoes++;
        dataUltimaRenovacao = hoje;
        dataPrevistaDevolucao = dataPrevistaDevolucao.plusDays(usuario.getDiasEmprestimo());
    }

    public void devolver(LocalDate dataDevolucao) throws TransicaoEstadoInvalidaException {
        atualizarEstadoPorData(dataDevolucao);
        if (estado != EstadoEmprestimo.ATIVO && estado != EstadoEmprestimo.ATRASADO) {
            throw new TransicaoEstadoInvalidaException("Somente emprestimos ativos ou atrasados podem ser devolvidos.");
        }
        if (dataDevolucao.isBefore(dataEmprestimo)) {
            throw new TransicaoEstadoInvalidaException("Data de devolucao nao pode ser anterior a data de emprestimo.");
        }
        this.dataDevolucao = dataDevolucao;
        estado = EstadoEmprestimo.DEVOLVIDO;
        item.devolver();
    }

    public int calcularDiasAtraso(LocalDate dataReferencia) {
        if (dataReferencia == null || !dataReferencia.isAfter(dataPrevistaDevolucao)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dataPrevistaDevolucao, dataReferencia);
    }

    public double calcularMulta(LocalDate dataReferencia) {
        return usuario.calcularMulta(calcularDiasAtraso(dataReferencia));
    }

    public void cancelar() throws TransicaoEstadoInvalidaException {
        if (estado != EstadoEmprestimo.ATIVO && estado != EstadoEmprestimo.ATRASADO) {
            throw new TransicaoEstadoInvalidaException("Somente emprestimos ativos ou atrasados podem ser cancelados.");
        }
        estado = EstadoEmprestimo.CANCELADO;
        item.devolver();
    }

    public boolean estaEmAberto() {
        return estado == EstadoEmprestimo.ATIVO || estado == EstadoEmprestimo.ATRASADO;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s pegou '%s' | emprestimo: %s | previsao: %s | devolucao: %s | estado: %s | renovacoes: %d",
                id,
                usuario.getNome(),
                item.getTitulo(),
                dataEmprestimo,
                dataPrevistaDevolucao,
                dataDevolucao == null ? "-" : dataDevolucao,
                estado,
                renovacoes);
    }
}
