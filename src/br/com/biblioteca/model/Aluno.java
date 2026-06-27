package br.com.biblioteca.model;

import br.com.biblioteca.exception.ValidacaoException;

public class Aluno extends Usuario {
    private String matricula;
    private String curso;

    public Aluno(int id, String nome, String cpf, String email, String telefone, String matricula, String curso) throws ValidacaoException {
        super(id, nome, cpf, email, telefone);
        setMatricula(matricula);
        setCurso(curso);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) throws ValidacaoException {
        if (matricula == null || matricula.trim().length() < 4) {
            throw new ValidacaoException("Matrícula do aluno deve possuir pelo menos 4 caracteres.");
        }
        this.matricula = matricula.trim();
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) throws ValidacaoException {
        if (curso == null || curso.trim().isEmpty()) {
            throw new ValidacaoException("Curso do aluno é obrigatório.");
        }
        this.curso = curso.trim();
    }

    @Override
    public int getLimiteEmprestimos() {
        return 3;
    }

    @Override
    public int getDiasEmprestimo() {
        return 7;
    }

    @Override
    public double calcularMulta(int diasAtraso) {
        return Math.max(0, diasAtraso) * 1.00;
    }

    @Override
    public String getTipo() {
        return "Aluno";
    }

    @Override
    public String toString() {
        return dadosResumidos() + String.format(" | matrícula: %s | curso: %s | limite: %d | prazo: %d dias", matricula, curso, getLimiteEmprestimos(), getDiasEmprestimo());
    }
}
