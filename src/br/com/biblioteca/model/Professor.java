package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public class Professor extends Usuario {
    private String siape;
    private String departamento;

    public Professor(int id, String nome, String cpf, String email, String telefone, String siape, String departamento) throws ValidacaoException {
        super(id, nome, cpf, email, telefone);
        setSiape(siape);
        setDepartamento(departamento);
    }

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) throws ValidacaoException {
        if (siape == null || siape.trim().length() < 4) {
            throw new ValidacaoException("SIAPE do professor deve possuir pelo menos 4 caracteres.");
        }
        this.siape = siape.trim();
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) throws ValidacaoException {
        if (departamento == null || departamento.trim().isEmpty()) {
            throw new ValidacaoException("Departamento do professor é obrigatório.");
        }
        this.departamento = departamento.trim();
    }

    @Override
    public int getLimiteEmprestimos() {
        return 5;
    }

    @Override
    public int getDiasEmprestimo() {
        return 14;
    }

    @Override
    public double getValorMultaPorDiaAtraso() {
        return 1.0;
    }

    @Override
    public String getTipo() {
        return "Professor";
    }

    @Override
    public String toString() {
        return dadosResumidos() + String.format(" | SIAPE: %s | departamento: %s | limite: %d | prazo: %d dias", siape, departamento, getLimiteEmprestimos(), getDiasEmprestimo());
    }
}
