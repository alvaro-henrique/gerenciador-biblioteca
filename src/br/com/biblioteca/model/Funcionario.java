package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public class Funcionario extends Pessoa {
    private String email;
    private String telefone;
    private String registro;
    private String cargo;
    private boolean ativo;

    public Funcionario(int id, String nome, String cpf, String email, String telefone, String registro, String cargo)
            throws ValidacaoException {
        super(id, nome, cpf);
        setEmail(email);
        setTelefone(telefone);
        setRegistro(registro);
        setCargo(cargo);
        this.ativo = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ValidacaoException {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new ValidacaoException("E-mail do funcionario invalido.");
        }
        this.email = email.trim().toLowerCase();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) throws ValidacaoException {
        if (telefone == null || telefone.replaceAll("\\D", "").length() < 8) {
            throw new ValidacaoException("Telefone do funcionario deve possuir pelo menos 8 digitos.");
        }
        this.telefone = telefone.replaceAll("\\D", "");
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) throws ValidacaoException {
        if (registro == null || registro.trim().length() < 3) {
            throw new ValidacaoException("Registro do funcionario deve possuir pelo menos 3 caracteres.");
        }
        this.registro = registro.trim();
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) throws ValidacaoException {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new ValidacaoException("Cargo do funcionario e obrigatorio.");
        }
        this.cargo = cargo.trim();
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

    @Override
    public String getTipo() {
        return "Funcionario";
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s - %s - cargo: %s - registro: %s - %s",
                getId(),
                getTipo(),
                getNome(),
                email,
                cargo,
                registro,
                ativo ? "ATIVO" : "INATIVO");
    }
}
