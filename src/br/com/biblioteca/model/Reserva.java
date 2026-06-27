package br.com.biblioteca.model;

import java.time.LocalDate;

import br.com.biblioteca.exception.TransicaoEstadoInvalidaException;
import br.com.biblioteca.exception.ValidacaoException;

public class Reserva {
    private final int id;
    private final Usuario usuario;
    private final ItemAcervo item;
    private final LocalDate dataReserva;
    private EstadoReserva estado;

    public Reserva(int id, Usuario usuario, ItemAcervo item, LocalDate dataReserva) throws ValidacaoException {
        if (id <= 0) {
            throw new ValidacaoException("ID da reserva deve ser maior que zero.");
        }
        if (usuario == null) {
            throw new ValidacaoException("Usuario da reserva e obrigatorio.");
        }
        if (item == null) {
            throw new ValidacaoException("Item da reserva e obrigatorio.");
        }
        if (dataReserva == null) {
            throw new ValidacaoException("Data da reserva e obrigatoria.");
        }
        this.id = id;
        this.usuario = usuario;
        this.item = item;
        this.dataReserva = dataReserva;
        this.estado = EstadoReserva.ATIVA;
    }

    public Reserva(int id, Usuario usuario, ItemAcervo item, LocalDate dataReserva, EstadoReserva estado) throws ValidacaoException {
        this(id, usuario, item, dataReserva);
        this.estado = estado;
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

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public boolean estaAtiva() {
        return estado == EstadoReserva.ATIVA;
    }

    public void atender() throws TransicaoEstadoInvalidaException {
        if (estado != EstadoReserva.ATIVA) {
            throw new TransicaoEstadoInvalidaException("Somente reservas ativas podem ser atendidas.");
        }
        if (!item.isDisponivel()) {
            throw new TransicaoEstadoInvalidaException("Reserva nao pode ser atendida porque o item ainda nao esta disponivel.");
        }
        estado = EstadoReserva.ATENDIDA;
    }

    public void marcarAtendida() throws TransicaoEstadoInvalidaException {
        if (estado != EstadoReserva.ATIVA) {
            throw new TransicaoEstadoInvalidaException("Somente reservas ativas podem ser atendidas.");
        }
        estado = EstadoReserva.ATENDIDA;
    }

    public void cancelar() throws TransicaoEstadoInvalidaException {
        if (estado != EstadoReserva.ATIVA) {
            throw new TransicaoEstadoInvalidaException("Somente reservas ativas podem ser canceladas.");
        }
        estado = EstadoReserva.CANCELADA;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s reservou '%s' em %s | estado: %s",
                id,
                usuario.getNome(),
                item.getTitulo(),
                dataReserva,
                estado);
    }
}
