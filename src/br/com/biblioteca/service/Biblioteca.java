package br.com.biblioteca.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.biblioteca.exception.BibliotecaException;
import br.com.biblioteca.exception.EntidadeNaoEncontradaException;
import br.com.biblioteca.exception.LimiteEmprestimosException;
import br.com.biblioteca.exception.LivroIndisponivelException;
import br.com.biblioteca.exception.LivroNaoEncontradoException;
import br.com.biblioteca.exception.MultaPendenteException;
import br.com.biblioteca.exception.OperacaoInvalidaException;
import br.com.biblioteca.exception.TransicaoEstadoInvalidaException;
import br.com.biblioteca.exception.UsuarioNaoEncontradoException;
import br.com.biblioteca.exception.ValidacaoException;
import br.com.biblioteca.model.Aluno;
import br.com.biblioteca.model.Categoria;
import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.EstadoReserva;
import br.com.biblioteca.model.Funcionario;
import br.com.biblioteca.model.ItemAcervo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Multa;
import br.com.biblioteca.model.Professor;
import br.com.biblioteca.model.Reserva;
import br.com.biblioteca.model.Revista;
import br.com.biblioteca.model.Usuario;

public class Biblioteca {
    private final Map<Integer, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<Integer, Funcionario> funcionarios = new LinkedHashMap<>();
    private final Map<Integer, ItemAcervo> itens = new LinkedHashMap<>();
    private final List<Emprestimo> emprestimos = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();
    private final List<Multa> multas = new ArrayList<>();

    private int proximoUsuarioId = 1;
    private int proximoFuncionarioId = 1;
    private int proximoItemId = 1;
    private int proximoCategoriaId = 1;
    private int proximoEmprestimoId = 1;
    private int proximoReservaId = 1;
    private int proximaMultaId = 1;

    public Aluno cadastrarAluno(String nome, String cpf, String email, String telefone, String matricula, String curso)
            throws BibliotecaException {
        validarCpfUnico(cpf, null, null);
        Aluno aluno = new Aluno(proximoUsuarioId++, nome, cpf, email, telefone, matricula, curso);
        usuarios.put(aluno.getId(), aluno);
        return aluno;
    }

    public Professor cadastrarProfessor(String nome, String cpf, String email, String telefone, String siape, String departamento)
            throws BibliotecaException {
        validarCpfUnico(cpf, null, null);
        Professor professor = new Professor(proximoUsuarioId++, nome, cpf, email, telefone, siape, departamento);
        usuarios.put(professor.getId(), professor);
        return professor;
    }

    public Funcionario cadastrarFuncionario(String nome, String cpf, String email, String telefone, String registro, String cargo)
            throws BibliotecaException {
        validarCpfUnico(cpf, null, null);
        validarRegistroFuncionarioUnico(registro, 0);
        Funcionario funcionario = new Funcionario(proximoFuncionarioId++, nome, cpf, email, telefone, registro, cargo);
        funcionarios.put(funcionario.getId(), funcionario);
        return funcionario;
    }

    public Livro cadastrarLivro(String titulo, int ano, String categoriaNome, String categoriaDescricao, String isbn,
            String nomeAutor) throws BibliotecaException {
        validarItemComTituloUnico(titulo, 0);
        Categoria categoria = new Categoria(proximoCategoriaId++, categoriaNome, categoriaDescricao);
        Livro livro = new Livro(proximoItemId++, titulo, ano, categoria, isbn, nomeAutor);
        itens.put(livro.getId(), livro);
        return livro;
    }

    public Revista cadastrarRevista(String titulo, int ano, String categoriaNome, String categoriaDescricao, String issn,
            int numeroEdicao, String periodicidade) throws BibliotecaException {
        validarItemComTituloUnico(titulo, 0);
        Categoria categoria = new Categoria(proximoCategoriaId++, categoriaNome, categoriaDescricao);
        Revista revista = new Revista(proximoItemId++, titulo, ano, categoria, issn, numeroEdicao, periodicidade);
        itens.put(revista.getId(), revista);
        return revista;
    }

    public void alterarDadosUsuario(int id, String nome, String cpf, String email, String telefone) throws BibliotecaException {
        Usuario usuario = buscarUsuario(id);
        validarCpfUnico(cpf, id, null);
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
    }

    public void alterarDadosAluno(int id, String matricula, String curso) throws BibliotecaException {
        Usuario usuario = buscarUsuario(id);
        if (!(usuario instanceof Aluno)) {
            throw new OperacaoInvalidaException("Usuario informado nao e aluno.");
        }
        Aluno aluno = (Aluno) usuario;
        aluno.setMatricula(matricula);
        aluno.setCurso(curso);
    }

    public void alterarDadosProfessor(int id, String siape, String departamento) throws BibliotecaException {
        Usuario usuario = buscarUsuario(id);
        if (!(usuario instanceof Professor)) {
            throw new OperacaoInvalidaException("Usuario informado nao e professor.");
        }
        Professor professor = (Professor) usuario;
        professor.setSiape(siape);
        professor.setDepartamento(departamento);
    }

    public void alterarDadosFuncionario(int id, String nome, String cpf, String email, String telefone, String registro, String cargo)
            throws BibliotecaException {
        Funcionario funcionario = buscarFuncionario(id);
        validarCpfUnico(cpf, null, id);
        validarRegistroFuncionarioUnico(registro, id);
        funcionario.setNome(nome);
        funcionario.setCpf(cpf);
        funcionario.setEmail(email);
        funcionario.setTelefone(telefone);
        funcionario.setRegistro(registro);
        funcionario.setCargo(cargo);
    }

    public void alterarDadosItem(int id, String titulo, int ano, String categoriaNome, String categoriaDescricao)
            throws BibliotecaException {
        ItemAcervo item = buscarItem(id);
        validarItemComTituloUnico(titulo, id);
        item.setTitulo(titulo);
        item.setAnoPublicacao(ano);
        item.setCategoria(new Categoria(proximoCategoriaId++, categoriaNome, categoriaDescricao));
    }

    public void alterarDadosLivro(int id, String isbn, String nomeAutor) throws BibliotecaException {
        ItemAcervo item = buscarItem(id);
        if (!(item instanceof Livro)) {
            throw new OperacaoInvalidaException("Item informado nao e livro.");
        }
        Livro livro = (Livro) item;
        livro.setIsbn(isbn);
        livro.setNomeAutor(nomeAutor);
    }

    public void alterarDadosRevista(int id, String issn, int numeroEdicao, String periodicidade) throws BibliotecaException {
        ItemAcervo item = buscarItem(id);
        if (!(item instanceof Revista)) {
            throw new OperacaoInvalidaException("Item informado nao e revista.");
        }
        Revista revista = (Revista) item;
        revista.setIssn(issn);
        revista.setNumeroEdicao(numeroEdicao);
        revista.setPeriodicidade(periodicidade);
    }

    public void removerUsuario(int id) throws BibliotecaException {
        buscarUsuario(id);
        if (contarEmprestimosEmAberto(id) > 0) {
            throw new OperacaoInvalidaException("Usuario possui emprestimo em aberto e nao pode ser excluido.");
        }
        if (reservas.stream().anyMatch(r -> r.estaAtiva() && r.getUsuario().getId() == id)) {
            throw new OperacaoInvalidaException("Usuario possui reserva ativa e nao pode ser excluido.");
        }
        if (possuiMultaPendente(id)) {
            throw new OperacaoInvalidaException("Usuario possui multa pendente e nao pode ser excluido.");
        }
        usuarios.remove(id);
    }

    public void removerFuncionario(int id) throws BibliotecaException {
        buscarFuncionario(id);
        funcionarios.remove(id);
    }

    public void removerItem(int id) throws BibliotecaException {
        ItemAcervo item = buscarItem(id);
        if (!item.isDisponivel()) {
            throw new OperacaoInvalidaException("Item emprestado nao pode ser removido.");
        }
        if (reservas.stream().anyMatch(r -> r.estaAtiva() && r.getItem().getId() == id)) {
            throw new OperacaoInvalidaException("Item possui reserva ativa e nao pode ser removido.");
        }
        itens.remove(id);
    }

    public Emprestimo registrarEmprestimo(int usuarioId, int itemId) throws BibliotecaException {
        atualizarEstadosEmprestimos();
        Usuario usuario = buscarUsuario(usuarioId);
        ItemAcervo item = buscarItem(itemId);

        if (!usuario.isAtivo()) {
            throw new OperacaoInvalidaException("Usuario esta inativo e nao pode realizar emprestimos.");
        }
        if (possuiMultaPendente(usuarioId)) {
            throw new MultaPendenteException("Usuario com multa pendente nao pode realizar emprestimos.");
        }
        if (!item.isDisponivel()) {
            throw new LivroIndisponivelException("Livro indisponivel para emprestimo.");
        }
        if (possuiEmprestimoAtrasado(usuarioId)) {
            throw new MultaPendenteException("Usuario possui emprestimo atrasado e nao pode retirar novos livros.");
        }
        if (contarEmprestimosEmAberto(usuarioId) >= usuario.getLimiteEmprestimos()) {
            throw new LimiteEmprestimosException("Usuario atingiu o limite de emprestimos para o seu perfil.");
        }

        Reserva reservaPrioritaria = buscarReservaAtivaMaisAntiga(itemId);
        if (reservaPrioritaria != null && reservaPrioritaria.getUsuario().getId() != usuarioId) {
            throw new LivroIndisponivelException("Este livro possui reserva ativa com prioridade para outro usuario.");
        }

        LocalDate hoje = LocalDate.now();
        Emprestimo emprestimo = new Emprestimo(proximoEmprestimoId++, usuario, item, hoje, hoje.plusDays(usuario.getDiasEmprestimo()));
        item.emprestar();
        emprestimos.add(emprestimo);

        if (reservaPrioritaria != null && reservaPrioritaria.getUsuario().getId() == usuarioId) {
            reservaPrioritaria.marcarAtendida();
        }

        return emprestimo;
    }

    public double devolverEmprestimo(int emprestimoId) throws BibliotecaException {
        Emprestimo emprestimo = buscarEmprestimo(emprestimoId);
        double valorMulta = emprestimo.devolver(LocalDate.now());
        int diasAtraso = emprestimo.calcularDiasAtraso(emprestimo.getDataDevolucao());
        if (valorMulta > 0) {
            Multa multa = new Multa(proximaMultaId++, emprestimo.getUsuario(), emprestimo, LocalDate.now(), diasAtraso, valorMulta);
            multas.add(multa);
        }
        return valorMulta;
    }

    public void renovarEmprestimo(int emprestimoId) throws BibliotecaException {
        Emprestimo emprestimo = buscarEmprestimo(emprestimoId);
        boolean existeReservaAtivaParaItem = reservas.stream()
                .anyMatch(r -> r.estaAtiva() && r.getItem().getId() == emprestimo.getItem().getId());
        if (existeReservaAtivaParaItem) {
            throw new OperacaoInvalidaException("Emprestimo nao pode ser renovado porque existe reserva ativa para este item.");
        }
        emprestimo.renovar(LocalDate.now());
    }

    public void cancelarEmprestimo(int emprestimoId) throws BibliotecaException {
        buscarEmprestimo(emprestimoId).cancelar();
    }

    public Reserva reservarItem(int usuarioId, int itemId) throws BibliotecaException {
        Usuario usuario = buscarUsuario(usuarioId);
        ItemAcervo item = buscarItem(itemId);
        if (!usuario.isAtivo()) {
            throw new OperacaoInvalidaException("Usuario inativo nao pode realizar reservas.");
        }
        if (item.isDisponivel()) {
            throw new OperacaoInvalidaException("Item disponivel nao precisa ser reservado. Realize o emprestimo.");
        }
        boolean reservaDuplicada = reservas.stream()
                .anyMatch(r -> r.estaAtiva() && r.getUsuario().getId() == usuarioId && r.getItem().getId() == itemId);
        if (reservaDuplicada) {
            throw new OperacaoInvalidaException("Usuario ja possui reserva ativa para este item.");
        }
        Reserva reserva = new Reserva(proximoReservaId++, usuario, item, LocalDate.now());
        reservas.add(reserva);
        return reserva;
    }

    public Emprestimo atenderReserva(int reservaId) throws BibliotecaException {
        Reserva reserva = buscarReserva(reservaId);
        if (reserva.getEstado() != EstadoReserva.ATIVA) {
            throw new TransicaoEstadoInvalidaException("Reserva nao esta ativa.");
        }
        Reserva prioridade = buscarReservaAtivaMaisAntiga(reserva.getItem().getId());
        if (prioridade != null && prioridade.getId() != reserva.getId()) {
            throw new OperacaoInvalidaException("Existe outra reserva mais antiga com prioridade para este item.");
        }
        if (!reserva.getItem().isDisponivel()) {
            throw new OperacaoInvalidaException("Reserva nao pode ser atendida porque o item ainda nao esta disponivel.");
        }
        return registrarEmprestimo(reserva.getUsuario().getId(), reserva.getItem().getId());
    }

    public void cancelarReserva(int reservaId) throws BibliotecaException {
        buscarReserva(reservaId).cancelar();
    }

    public void pagarMulta(int multaId) throws BibliotecaException {
        buscarMulta(multaId).registrarPagamento(LocalDate.now());
    }

    public Usuario buscarUsuario(int id) throws UsuarioNaoEncontradoException {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException("Usuario nao encontrado.");
        }
        return usuario;
    }

    public Funcionario buscarFuncionario(int id) throws EntidadeNaoEncontradaException {
        Funcionario funcionario = funcionarios.get(id);
        if (funcionario == null) {
            throw new EntidadeNaoEncontradaException("Funcionario nao encontrado.");
        }
        return funcionario;
    }

    public ItemAcervo buscarItem(int id) throws LivroNaoEncontradoException {
        ItemAcervo item = itens.get(id);
        if (item == null) {
            throw new LivroNaoEncontradoException("Livro ou item do acervo nao encontrado.");
        }
        return item;
    }

    public Emprestimo buscarEmprestimo(int id) throws EntidadeNaoEncontradaException {
        return emprestimos.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Emprestimo nao encontrado."));
    }

    public Reserva buscarReserva(int id) throws EntidadeNaoEncontradaException {
        return reservas.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Reserva nao encontrada."));
    }

    public Multa buscarMulta(int id) throws EntidadeNaoEncontradaException {
        return multas.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Multa nao encontrada."));
    }

    public Collection<Usuario> listarUsuarios() {
        return usuarios.values();
    }

    public Collection<Funcionario> listarFuncionarios() {
        return funcionarios.values();
    }

    public Collection<ItemAcervo> listarItens() {
        return itens.values();
    }

    public List<Emprestimo> listarEmprestimos() {
        atualizarEstadosEmprestimos();
        return emprestimos;
    }

    public List<Reserva> listarReservas() {
        return reservas;
    }

    public List<Multa> listarMultas() {
        return multas;
    }

    public Map<Integer, Usuario> getUsuarios() {
        return usuarios;
    }

    public Map<Integer, Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public Map<Integer, ItemAcervo> getItens() {
        return itens;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public List<Multa> getMultas() {
        return multas;
    }

    public void adicionarUsuarioCarregado(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
        proximoUsuarioId = Math.max(proximoUsuarioId, usuario.getId() + 1);
    }

    public void adicionarFuncionarioCarregado(Funcionario funcionario) {
        funcionarios.put(funcionario.getId(), funcionario);
        proximoFuncionarioId = Math.max(proximoFuncionarioId, funcionario.getId() + 1);
    }

    public void adicionarItemCarregado(ItemAcervo item, int autorId, int categoriaId) {
        itens.put(item.getId(), item);
        proximoItemId = Math.max(proximoItemId, item.getId() + 1);
        proximoCategoriaId = Math.max(proximoCategoriaId, categoriaId + 1);
    }

    public void adicionarEmprestimoCarregado(Emprestimo emprestimo) {
        emprestimos.add(emprestimo);
        proximoEmprestimoId = Math.max(proximoEmprestimoId, emprestimo.getId() + 1);
    }

    public void adicionarReservaCarregada(Reserva reserva) {
        reservas.add(reserva);
        proximoReservaId = Math.max(proximoReservaId, reserva.getId() + 1);
    }

    public void adicionarMultaCarregada(Multa multa) {
        multas.add(multa);
        proximaMultaId = Math.max(proximaMultaId, multa.getId() + 1);
    }

    public void limparTudo() {
        usuarios.clear();
        funcionarios.clear();
        itens.clear();
        emprestimos.clear();
        reservas.clear();
        multas.clear();
        proximoUsuarioId = 1;
        proximoFuncionarioId = 1;
        proximoItemId = 1;
        proximoCategoriaId = 1;
        proximoEmprestimoId = 1;
        proximoReservaId = 1;
        proximaMultaId = 1;
    }

    public void atualizarEstadosEmprestimos() {
        emprestimos.forEach(e -> e.atualizarEstadoPorData(LocalDate.now()));
    }

    private void validarCpfUnico(String cpf, Integer usuarioIgnoradoId, Integer funcionarioIgnoradoId) throws ValidacaoException {
        String cpfNormalizado = cpf == null ? "" : cpf.replaceAll("\\D", "");
        for (Usuario usuario : usuarios.values()) {
            if ((usuarioIgnoradoId == null || usuario.getId() != usuarioIgnoradoId) && usuario.getCpf().equals(cpfNormalizado)) {
                throw new ValidacaoException("Ja existe usuario cadastrado com este CPF.");
            }
        }
        for (Funcionario funcionario : funcionarios.values()) {
            if ((funcionarioIgnoradoId == null || funcionario.getId() != funcionarioIgnoradoId)
                    && funcionario.getCpf().equals(cpfNormalizado)) {
                throw new ValidacaoException("Ja existe funcionario cadastrado com este CPF.");
            }
        }
    }

    private void validarRegistroFuncionarioUnico(String registro, int ignorarId) throws ValidacaoException {
        String normalizado = registro == null ? "" : registro.trim().toLowerCase();
        for (Funcionario funcionario : funcionarios.values()) {
            if (funcionario.getId() != ignorarId && funcionario.getRegistro().trim().toLowerCase().equals(normalizado)) {
                throw new ValidacaoException("Ja existe funcionario cadastrado com este registro.");
            }
        }
    }

    private void validarItemComTituloUnico(String titulo, int ignorarId) throws ValidacaoException {
        for (ItemAcervo item : itens.values()) {
            if (item.getId() != ignorarId && item.getTitulo().equalsIgnoreCase(titulo.trim())) {
                throw new ValidacaoException("Ja existe item cadastrado com este titulo.");
            }
        }
    }

    private boolean possuiEmprestimoAtrasado(int usuarioId) {
        atualizarEstadosEmprestimos();
        return emprestimos.stream()
                .anyMatch(e -> e.getUsuario().getId() == usuarioId && e.getEstado().name().equals("ATRASADO"));
    }

    private boolean possuiMultaPendente(int usuarioId) {
        return multas.stream().anyMatch(m -> m.getUsuario().getId() == usuarioId && m.estaPendente());
    }

    private long contarEmprestimosEmAberto(int usuarioId) {
        return emprestimos.stream()
                .filter(e -> e.getUsuario().getId() == usuarioId && e.estaEmAberto())
                .count();
    }

    private Reserva buscarReservaAtivaMaisAntiga(int itemId) {
        return reservas.stream()
                .filter(r -> r.estaAtiva() && r.getItem().getId() == itemId)
                .min(Comparator.comparing(Reserva::getDataReserva).thenComparing(Reserva::getId))
                .orElse(null);
    }
}
