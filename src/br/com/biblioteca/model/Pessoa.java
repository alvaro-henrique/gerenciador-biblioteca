package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public abstract class Pessoa {
    private final int id;
    private String nome;
    private String cpf;

    public Pessoa(int id, String nome, String cpf) throws ValidacaoException {
        if (id <= 0) {
            throw new ValidacaoException("ID deve ser maior que zero.");
        }
        this.id = id;
        setNome(nome);
        setCpf(cpf);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws ValidacaoException {
        if (nome == null || nome.trim().length() < 3) {
            throw new ValidacaoException("Nome deve possuir pelo menos 3 caracteres.");
        }
        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) throws ValidacaoException {
        String cpfNormalizado = cpf == null ? "" : cpf.replaceAll("\\D", "");
        if (!isCpfValido(cpfNormalizado)) {
            throw new ValidacaoException("CPF invalido.");
        }
        this.cpf = cpfNormalizado;
    }

    private boolean isCpfValido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        int somaPrimeiroDigito = 0;
        for (int i = 0; i < 9; i++) {
            somaPrimeiroDigito += (cpf.charAt(i) - '0') * (10 - i);
        }
        int restoPrimeiroDigito = (somaPrimeiroDigito * 10) % 11;
        int primeiroDigitoVerificador = restoPrimeiroDigito == 10 ? 0 : restoPrimeiroDigito;
        if (primeiroDigitoVerificador != cpf.charAt(9) - '0') {
            return false;
        }

        int somaSegundoDigito = 0;
        for (int i = 0; i < 10; i++) {
            somaSegundoDigito += (cpf.charAt(i) - '0') * (11 - i);
        }
        int restoSegundoDigito = (somaSegundoDigito * 10) % 11;
        int segundoDigitoVerificador = restoSegundoDigito == 10 ? 0 : restoSegundoDigito;
        return segundoDigitoVerificador == cpf.charAt(10) - '0';
    }

    public abstract String getTipo();
}
