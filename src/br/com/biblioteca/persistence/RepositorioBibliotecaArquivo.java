package br.com.biblioteca.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.biblioteca.exception.BibliotecaException;
import br.com.biblioteca.exception.PersistenciaException;
import br.com.biblioteca.exception.ValidacaoException;
import br.com.biblioteca.model.Aluno;
import br.com.biblioteca.model.Autor;
import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.EstadoEmprestimo;
import br.com.biblioteca.model.EstadoReserva;
import br.com.biblioteca.model.Funcionario;
import br.com.biblioteca.model.ItemAcervo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Multa;
import br.com.biblioteca.model.Professor;
import br.com.biblioteca.model.Reserva;
import br.com.biblioteca.model.Revista;
import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.service.Biblioteca;

public class RepositorioBibliotecaArquivo extends Persistencia {
    private final Path pastaDados;

    public RepositorioBibliotecaArquivo(String pastaDados) {
        this.pastaDados = Paths.get(pastaDados);
    }

    @Override
    public void salvar(Biblioteca biblioteca) throws PersistenciaException {
        try {
            Files.createDirectories(pastaDados);
            salvarUsuarios(biblioteca);
            salvarFuncionarios(biblioteca);
            salvarLivros(biblioteca);
            salvarRevistas(biblioteca);
            salvarEmprestimos(biblioteca);
            salvarReservas(biblioteca);
            salvarMultas(biblioteca);
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao salvar dados em arquivos.", e);
        }
    }

    @Override
    public void carregar(Biblioteca biblioteca) throws BibliotecaException {
        try {
            Files.createDirectories(pastaDados);
            biblioteca.limparTudo();
            carregarUsuarios(biblioteca);
            carregarFuncionarios(biblioteca);
            carregarLivros(biblioteca);
            carregarRevistas(biblioteca);
            carregarEmprestimos(biblioteca);
            carregarReservas(biblioteca);
            carregarMultas(biblioteca);
            biblioteca.atualizarEstadosEmprestimos();
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao carregar dados dos arquivos.", e);
        }
    }

    private void salvarUsuarios(Biblioteca biblioteca) throws IOException {
        List<Map<String, Object>> objetos = new ArrayList<>();
        for (Usuario usuario : biblioteca.getUsuarios().values()) {
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("tipo", usuario.getTipo());
            obj.put("id", usuario.getId());
            obj.put("nome", usuario.getNome());
            obj.put("cpf", usuario.getCpf());
            obj.put("email", usuario.getEmail());
            obj.put("telefone", usuario.getTelefone());
            obj.put("ativo", usuario.isAtivo());
            if (usuario instanceof Aluno) {
                Aluno aluno = (Aluno) usuario;
                obj.put("matricula", aluno.getMatricula());
                obj.put("curso", aluno.getCurso());
            } else if (usuario instanceof Professor) {
                Professor professor = (Professor) usuario;
                obj.put("siape", professor.getSiape());
                obj.put("departamento", professor.getDepartamento());
            }
            objetos.add(obj);
        }
        JsonUtil.escreverObjetos(pastaDados.resolve("usuarios.json"), objetos);
    }

    private void salvarFuncionarios(Biblioteca biblioteca) throws IOException {
        List<Map<String, Object>> objetos = new ArrayList<>();
        for (Funcionario funcionario : biblioteca.getFuncionarios().values()) {
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("id", funcionario.getId());
            obj.put("nome", funcionario.getNome());
            obj.put("cpf", funcionario.getCpf());
            obj.put("email", funcionario.getEmail());
            obj.put("telefone", funcionario.getTelefone());
            obj.put("registro", funcionario.getRegistro());
            obj.put("cargo", funcionario.getCargo());
            obj.put("ativo", funcionario.isAtivo());
            objetos.add(obj);
        }
        JsonUtil.escreverObjetos(pastaDados.resolve("funcionarios.json"), objetos);
    }

    private void salvarLivros(Biblioteca biblioteca) throws IOException {
        List<Map<String, Object>> objetos = new ArrayList<>();
        for (ItemAcervo item : biblioteca.getItens().values()) {
            if (!(item instanceof Livro)) {
                continue;
            }
            Livro livro = (Livro) item;
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("id", livro.getId());
            obj.put("titulo", livro.getTitulo());
            obj.put("anoPublicacao", livro.getAnoPublicacao());
            obj.put("categoriaId", livro.getCategoria().getId());
            obj.put("categoriaNome", livro.getCategoria().getNome());
            obj.put("categoriaDescricao", livro.getCategoria().getDescricao());
            obj.put("disponivel", livro.isDisponivel());
            obj.put("isbn", livro.getIsbn());
            obj.put("autorId", livro.getAutor().getId());
            obj.put("autorNome", livro.getAutor().getNome());
            obj.put("autorNacionalidade", livro.getAutor().getNacionalidade());
            obj.put("editora", livro.getEditora());
            objetos.add(obj);
        }
        JsonUtil.escreverObjetos(pastaDados.resolve("livros.json"), objetos);
    }

    private void salvarRevistas(Biblioteca biblioteca) throws IOException {
        List<Map<String, Object>> objetos = new ArrayList<>();
        for (ItemAcervo item : biblioteca.getItens().values()) {
            if (!(item instanceof Revista)) {
                continue;
            }
            Revista revista = (Revista) item;
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("id", revista.getId());
            obj.put("titulo", revista.getTitulo());
            obj.put("anoPublicacao", revista.getAnoPublicacao());
            obj.put("categoriaId", revista.getCategoria().getId());
            obj.put("categoriaNome", revista.getCategoria().getNome());
            obj.put("categoriaDescricao", revista.getCategoria().getDescricao());
            obj.put("disponivel", revista.isDisponivel());
            obj.put("issn", revista.getIssn());
            obj.put("numeroEdicao", revista.getNumeroEdicao());
            obj.put("periodicidade", revista.getPeriodicidade());
            objetos.add(obj);
        }
        JsonUtil.escreverObjetos(pastaDados.resolve("revistas.json"), objetos);
    }

    private void salvarEmprestimos(Biblioteca biblioteca) throws IOException {
        List<Map<String, Object>> objetos = new ArrayList<>();
        for (Emprestimo emprestimo : biblioteca.getEmprestimos()) {
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("id", emprestimo.getId());
            obj.put("usuarioId", emprestimo.getUsuario().getId());
            obj.put("itemId", emprestimo.getItem().getId());
            obj.put("dataEmprestimo", emprestimo.getDataEmprestimo().toString());
            obj.put("dataPrevistaDevolucao", emprestimo.getDataPrevistaDevolucao().toString());
            obj.put("dataDevolucao", emprestimo.getDataDevolucao() == null ? null : emprestimo.getDataDevolucao().toString());
            obj.put("estado", emprestimo.getEstado().name());
            obj.put("renovacoes", emprestimo.getRenovacoes());
            objetos.add(obj);
        }
        JsonUtil.escreverObjetos(pastaDados.resolve("emprestimos.json"), objetos);
    }

    private void salvarReservas(Biblioteca biblioteca) throws IOException {
        List<Map<String, Object>> objetos = new ArrayList<>();
        for (Reserva reserva : biblioteca.getReservas()) {
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("id", reserva.getId());
            obj.put("usuarioId", reserva.getUsuario().getId());
            obj.put("itemId", reserva.getItem().getId());
            obj.put("dataReserva", reserva.getDataReserva().toString());
            obj.put("estado", reserva.getEstado().name());
            objetos.add(obj);
        }
        JsonUtil.escreverObjetos(pastaDados.resolve("reservas.json"), objetos);
    }

    private void salvarMultas(Biblioteca biblioteca) throws IOException {
        List<Map<String, Object>> objetos = new ArrayList<>();
        for (Multa multa : biblioteca.getMultas()) {
            Map<String, Object> obj = new LinkedHashMap<>();
            obj.put("id", multa.getId());
            obj.put("usuarioId", multa.getUsuario().getId());
            obj.put("emprestimoId", multa.getEmprestimo().getId());
            obj.put("dataGeracao", multa.getDataGeracao().toString());
            obj.put("diasAtraso", multa.getDiasAtraso());
            obj.put("valor", multa.getValor());
            obj.put("paga", multa.isPaga());
            obj.put("dataPagamento", multa.getDataPagamento() == null ? null : multa.getDataPagamento().toString());
            objetos.add(obj);
        }
        JsonUtil.escreverObjetos(pastaDados.resolve("multas.json"), objetos);
    }

    private void carregarUsuarios(Biblioteca biblioteca) throws IOException, BibliotecaException {
        for (Map<String, String> obj : JsonUtil.lerObjetos(pastaDados.resolve("usuarios.json"))) {
            Usuario usuario;
            if ("Aluno".equals(obj.get("tipo"))) {
                usuario = new Aluno(intValue(obj, "id"), obj.get("nome"), obj.get("cpf"), obj.get("email"), obj.get("telefone"),
                        obj.get("matricula"), obj.get("curso"));
            } else {
                usuario = new Professor(intValue(obj, "id"), obj.get("nome"), obj.get("cpf"), obj.get("email"), obj.get("telefone"),
                        obj.get("siape"), obj.get("departamento"));
            }
            if (!boolValue(obj, "ativo")) {
                usuario.desativar();
            }
            biblioteca.adicionarUsuarioCarregado(usuario);
        }
    }

    private void carregarFuncionarios(Biblioteca biblioteca) throws IOException, PersistenciaException {
        for (Map<String, String> obj : JsonUtil.lerObjetos(pastaDados.resolve("funcionarios.json"))) {
            try {
                Funcionario funcionario = new Funcionario(intValue(obj, "id"), obj.get("nome"), obj.get("cpf"), obj.get("email"),
                        obj.get("telefone"), obj.get("registro"), obj.get("cargo"));
                if (!boolValue(obj, "ativo")) {
                    funcionario.desativar();
                }
                biblioteca.adicionarFuncionarioCarregado(funcionario);
            } catch (ValidacaoException e) {
                throw new PersistenciaException("Erro ao carregar funcionario.", e);
            }
        }
    }

    private void carregarLivros(Biblioteca biblioteca) throws IOException, PersistenciaException {
        for (Map<String, String> obj : JsonUtil.lerObjetos(pastaDados.resolve("livros.json"))) {
            try {
                Categoria categoria = new Categoria(intValue(obj, "categoriaId"), obj.get("categoriaNome"), obj.get("categoriaDescricao"));
                Autor autor = new Autor(intValue(obj, "autorId"), obj.get("autorNome"), obj.get("autorNacionalidade"));
                Livro livro = new Livro(intValue(obj, "id"), obj.get("titulo"), intValue(obj, "anoPublicacao"), categoria, obj.get("isbn"),
                        autor, obj.get("editora"));
                livro.definirDisponibilidade(boolValue(obj, "disponivel"));
                biblioteca.adicionarItemCarregado(livro, autor.getId(), categoria.getId());
            } catch (ValidacaoException e) {
                throw new PersistenciaException("Erro ao carregar livro.", e);
            }
        }
    }

    private void carregarRevistas(Biblioteca biblioteca) throws IOException, PersistenciaException {
        for (Map<String, String> obj : JsonUtil.lerObjetos(pastaDados.resolve("revistas.json"))) {
            try {
                Categoria categoria = new Categoria(intValue(obj, "categoriaId"), obj.get("categoriaNome"), obj.get("categoriaDescricao"));
                Revista revista = new Revista(intValue(obj, "id"), obj.get("titulo"), intValue(obj, "anoPublicacao"), categoria,
                        obj.get("issn"), intValue(obj, "numeroEdicao"), obj.get("periodicidade"));
                revista.definirDisponibilidade(boolValue(obj, "disponivel"));
                biblioteca.adicionarItemCarregado(revista, 0, categoria.getId());
            } catch (ValidacaoException e) {
                throw new PersistenciaException("Erro ao carregar revista.", e);
            }
        }
    }

    private void carregarEmprestimos(Biblioteca biblioteca) throws IOException, BibliotecaException {
        for (Map<String, String> obj : JsonUtil.lerObjetos(pastaDados.resolve("emprestimos.json"))) {
            LocalDate dataDevolucao = obj.get("dataDevolucao") == null ? null : LocalDate.parse(obj.get("dataDevolucao"));
            Emprestimo emprestimo = new Emprestimo(
                    intValue(obj, "id"),
                    biblioteca.buscarUsuario(intValue(obj, "usuarioId")),
                    biblioteca.buscarItem(intValue(obj, "itemId")),
                    LocalDate.parse(obj.get("dataEmprestimo")),
                    LocalDate.parse(obj.get("dataPrevistaDevolucao")),
                    dataDevolucao,
                    EstadoEmprestimo.valueOf(obj.get("estado")),
                    intValue(obj, "renovacoes"));
            biblioteca.adicionarEmprestimoCarregado(emprestimo);
        }
    }

    private void carregarReservas(Biblioteca biblioteca) throws IOException, BibliotecaException {
        for (Map<String, String> obj : JsonUtil.lerObjetos(pastaDados.resolve("reservas.json"))) {
            Reserva reserva = new Reserva(
                    intValue(obj, "id"),
                    biblioteca.buscarUsuario(intValue(obj, "usuarioId")),
                    biblioteca.buscarItem(intValue(obj, "itemId")),
                    LocalDate.parse(obj.get("dataReserva")),
                    EstadoReserva.valueOf(obj.get("estado")));
            biblioteca.adicionarReservaCarregada(reserva);
        }
    }

    private void carregarMultas(Biblioteca biblioteca) throws IOException, BibliotecaException {
        for (Map<String, String> obj : JsonUtil.lerObjetos(pastaDados.resolve("multas.json"))) {
            Multa multa = new Multa(
                    intValue(obj, "id"),
                    biblioteca.buscarUsuario(intValue(obj, "usuarioId")),
                    biblioteca.buscarEmprestimo(intValue(obj, "emprestimoId")),
                    LocalDate.parse(obj.get("dataGeracao")),
                    intValue(obj, "diasAtraso"),
                    doubleValue(obj, "valor"),
                    boolValue(obj, "paga"),
                    obj.get("dataPagamento") == null ? null : LocalDate.parse(obj.get("dataPagamento")));
            biblioteca.adicionarMultaCarregada(multa);
        }
    }

    private int intValue(Map<String, String> obj, String chave) {
        return Integer.parseInt(obj.get(chave));
    }

    private double doubleValue(Map<String, String> obj, String chave) {
        return Double.parseDouble(obj.get(chave));
    }

    private boolean boolValue(Map<String, String> obj, String chave) {
        return Boolean.parseBoolean(obj.get(chave));
    }
}
