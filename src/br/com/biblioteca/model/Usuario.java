package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public abstract class Usuario extends Pessoa {
    private String email;
    private String telefone;
    private boolean ativo;

    public Usuario(int id, String nome, String cpf, String email, String telefone) throws ValidacaoException {
        super(id, nome, cpf);
        setEmail(email);
        setTelefone(telefone);
        this.ativo = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ValidacaoException {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new ValidacaoException("E-mail inválido.");
        }
        this.email = email.trim().toLowerCase();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) throws ValidacaoException {
        if (telefone == null || telefone.replaceAll("\\D", "").length() < 8) {
            throw new ValidacaoException("Telefone deve possuir pelo menos 8 dígitos.");
        }
        this.telefone = telefone.replaceAll("\\D", "");
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public abstract int getLimiteEmprestimos();

    public abstract int getDiasEmprestimo();

    public abstract double calcularMulta(int diasAtraso);

    public String dadosResumidos() {
        return String.format("[%d] %s - %s - %s - %s", getId(), getTipo(), getNome(), email, ativo ? "ATIVO" : "INATIVO");
    }
}
