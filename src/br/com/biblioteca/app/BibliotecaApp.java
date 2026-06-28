package br.com.biblioteca.app;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import br.com.biblioteca.exception.BibliotecaException;
import br.com.biblioteca.exception.EntidadeNaoEncontradaException;
import br.com.biblioteca.exception.UsuarioNaoEncontradoException;
import br.com.biblioteca.model.Aluno;
import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Funcionario;
import br.com.biblioteca.model.ItemAcervo;
import br.com.biblioteca.model.Livro;
import br.com.biblioteca.model.Multa;
import br.com.biblioteca.model.Professor;
import br.com.biblioteca.model.Reserva;
import br.com.biblioteca.model.Revista;
import br.com.biblioteca.model.Usuario;
import br.com.biblioteca.persistence.RepositorioBibliotecaArquivo;
import br.com.biblioteca.service.Biblioteca;

public class BibliotecaApp {
    private final Scanner scanner = new Scanner(System.in);
    private final Biblioteca biblioteca = new Biblioteca();
    private final RepositorioBibliotecaArquivo repositorio = new RepositorioBibliotecaArquivo("dados");

    public void executar() {
        carregarDados();
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcao = lerInteiro("Escolha uma opcao: ");
            try {
                switch (opcao) {
                    case 1:
                        cadastrarUsuario();
                        break;
                    case 2:
                        listarUsuariosComRetorno();
                        break;
                    case 3:
                        alterarUsuario();
                        break;
                    case 4:
                        removerUsuario();
                        break;
                    case 5:
                        cadastrarFuncionario();
                        break;
                    case 6:
                        listarFuncionariosComRetorno();
                        break;
                    case 7:
                        alterarFuncionario();
                        break;
                    case 8:
                        removerFuncionario();
                        break;
                    case 9:
                        cadastrarItem();
                        break;
                    case 10:
                        listarItensComRetorno();
                        break;
                    case 11:
                        alterarItem();
                        break;
                    case 12:
                        removerItem();
                        break;
                    case 13:
                        registrarEmprestimo();
                        break;
                    case 14:
                        devolverEmprestimo();
                        break;
                    case 15:
                        renovarEmprestimo();
                        break;
                    case 16:
                        cancelarEmprestimo();
                        break;
                    case 17:
                        listarEmprestimos();
                        break;
                    case 18:
                        reservarItem();
                        break;
                    case 19:
                        atenderReserva();
                        break;
                    case 20:
                        cancelarReserva();
                        break;
                    case 21:
                        listarReservas();
                        break;
                    case 22:
                        listarMultas();
                        break;
                    case 23:
                        pagarMulta();
                        break;
                    case 24:
                        inserirDadosExemplo();
                        break;
                    case 0:
                        salvarDados();
                        continuar = false;
                        System.out.println("Sistema encerrado.");
                        break;
                    default:
                        System.out.println("Opcao invalida.");
                        break;
                }
            } catch (BibliotecaException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n========== SISTEMA DE GERENCIAMENTO DE BIBLIOTECA ==========");
        System.out.println("1  - Cadastrar usuario");
        System.out.println("2  - Listar usuarios");
        System.out.println("3  - Alterar usuario");
        System.out.println("4  - Excluir usuario");
        System.out.println("5  - Cadastrar funcionario");
        System.out.println("6  - Listar funcionarios");
        System.out.println("7  - Alterar funcionario");
        System.out.println("8  - Excluir funcionario");
        System.out.println("9  - Cadastrar item");
        System.out.println("10 - Listar itens");
        System.out.println("11 - Alterar item");
        System.out.println("12 - Remover item");
        System.out.println("13 - Registrar emprestimo");
        System.out.println("14 - Devolver emprestimo");
        System.out.println("15 - Renovar emprestimo");
        System.out.println("16 - Cancelar emprestimo");
        System.out.println("17 - Listar emprestimos");
        System.out.println("18 - Reservar item emprestado");
        System.out.println("19 - Atender reserva");
        System.out.println("20 - Cancelar reserva");
        System.out.println("21 - Listar reservas");
        System.out.println("22 - Listar multas");
        System.out.println("23 - Pagar multa");
        System.out.println("24 - Inserir dados de exemplo");
        System.out.println("0  - Salvar e sair");
    }

    private void carregarDados() {
        try {
            repositorio.carregar(biblioteca);
            System.out.println("Dados carregados com sucesso.");
        } catch (BibliotecaException e) {
            System.out.println("Nao foi possivel carregar dados anteriores: " + e.getMessage());
        }
    }

    private void salvarDados() {
        try {
            repositorio.salvar(biblioteca);
            System.out.println("Dados salvos com sucesso na pasta 'dados'.");
        } catch (BibliotecaException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    private void cadastrarUsuario() throws BibliotecaException {
        System.out.println("\n1 - Aluno");
        System.out.println("2 - Professor");
        int tipo = lerInteiro("Tipo de usuario: ");
        String nome = lerTexto("Nome: ");
        String cpf = lerCpfValido();
        String email = lerEmailValido();
        String telefone = lerTelefoneValido();
        if (tipo == 1) {
            String matricula = lerTexto("Matricula: ");
            String curso = lerTexto("Curso: ");
            Aluno aluno = biblioteca.cadastrarAluno(nome, cpf, email, telefone, matricula, curso);
            System.out.println("Aluno cadastrado: " + aluno);
        } else if (tipo == 2) {
            String siape = lerTexto("SIAPE: ");
            String departamento = lerTexto("Departamento: ");
            Professor professor = biblioteca.cadastrarProfessor(nome, cpf, email, telefone, siape, departamento);
            System.out.println("Professor cadastrado: " + professor);
        } else {
            System.out.println("Tipo invalido.");
        }
    }

    private void listarUsuariosComRetorno() {
        limparTerminal();
        listarUsuarios();
        aguardarRetornoAoMenuPrincipal();
    }

    private void listarFuncionariosComRetorno() {
        limparTerminal();
        listarFuncionarios();
        aguardarRetornoAoMenuPrincipal();
    }

    private void listarItensComRetorno() {
        limparTerminal();
        listarItens();
        aguardarRetornoAoMenuPrincipal();
    }

    private void listarUsuarios() {
        Collection<Usuario> usuarios = biblioteca.listarUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuario cadastrado.");
            return;
        }

        List<Aluno> alunos = new LinkedList<>();
        List<Professor> professores = new LinkedList<>();

        for (Usuario usuario : usuarios) {
            if (usuario instanceof Aluno) {
                alunos.add((Aluno) usuario);
            } else if (usuario instanceof Professor) {
                professores.add((Professor) usuario);
            }
        }

        if (!alunos.isEmpty()) {
            exibirGrupoAlunos(alunos);
        }

        if (!professores.isEmpty()) {
            if (!alunos.isEmpty()) {
                System.out.println();
            }
            exibirGrupoProfessores(professores);
        }
    }

    private void exibirGrupoAlunos(List<Aluno> alunos) {
        Aluno referencia = alunos.get(0);
        System.out.println("ALUNOS");
        System.out.printf("Limite: %d | Prazo: %d dias%n", referencia.getLimiteEmprestimos(), referencia.getDiasEmprestimo());
        for (Aluno aluno : alunos) {
            System.out.printf("[%d] %s - %s - %s | matricula: %s | curso: %s%n",
                    aluno.getId(),
                    aluno.getNome(),
                    aluno.getEmail(),
                    aluno.isAtivo() ? "ATIVO" : "INATIVO",
                    aluno.getMatricula(),
                    aluno.getCurso());
        }
    }

    private void exibirGrupoProfessores(List<Professor> professores) {
        Professor referencia = professores.get(0);
        System.out.println("PROFESSORES");
        System.out.printf("Limite: %d | Prazo: %d dias%n", referencia.getLimiteEmprestimos(), referencia.getDiasEmprestimo());
        for (Professor professor : professores) {
            System.out.printf("[%d] %s - %s - %s | SIAPE: %s | departamento: %s%n",
                    professor.getId(),
                    professor.getNome(),
                    professor.getEmail(),
                    professor.isAtivo() ? "ATIVO" : "INATIVO",
                    professor.getSiape(),
                    professor.getDepartamento());
        }
    }

    private void aguardarRetornoAoMenuPrincipal() {
        while (true) {
            int opcao = lerInteiro("Digite 0 para voltar ao menu principal: ");
            if (opcao == 0) {
                return;
            }
            System.out.println("Opcao invalida. Digite 0 para voltar ao menu principal.");
        }
    }

    private void limparTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void alterarUsuario() throws BibliotecaException {
        listarUsuarios();
        int id;
        Usuario usuario;
        while (true) {
            id = lerInteiro("ID do usuario para alterar: ");
            try {
                usuario = biblioteca.buscarUsuario(id);
                break;
            } catch (UsuarioNaoEncontradoException e) {
                System.out.println("ID invalido. Digite um ID de usuario existente.");
            }
        }
        System.out.println("Alterando: " + usuario);
        String nome = lerTexto("Novo nome: ");
        String cpf = lerCpfValidoParaAlteracaoUsuario(id);
        String email = lerEmailValido("Novo e-mail: ");
        String telefone = lerTelefoneValido("Novo telefone: ");
        String confirmacao = lerTexto("Confirme a alteracao digitando S: ");
        if (!"S".equalsIgnoreCase(confirmacao)) {
            System.out.println("Alteracao cancelada.");
            return;
        }
        biblioteca.alterarDadosUsuario(id, nome, cpf, email, telefone);
        salvarDados();
        System.out.println("Usuario alterado com sucesso.");
    }

    private void removerUsuario() throws BibliotecaException {
        limparTerminal();
        listarUsuarios();
        int id;
        while (true) {
            id = lerInteiro("ID do usuario para excluir: ");
            try {
                biblioteca.buscarUsuario(id);
                break;
            } catch (UsuarioNaoEncontradoException e) {
                System.out.println("ID invalido. Digite um ID de usuario existente.");
            }
        }
        String confirmacao = lerTexto("Confirme a exclusao digitando S: ");
        if (!"S".equalsIgnoreCase(confirmacao)) {
            System.out.println("Exclusao cancelada.");
            return;
        }
        biblioteca.removerUsuario(id);
        salvarDados();
        System.out.println("Usuario excluido com sucesso.");
    }

    private void cadastrarFuncionario() throws BibliotecaException {
        String nome = lerTexto("Nome: ");
        String cpf = lerCpfValido();
        String email = lerEmailValido();
        String telefone = lerTelefoneValido();
        String registro = lerTexto("Registro: ");
        String cargo = lerTexto("Cargo: ");
        Funcionario funcionario = biblioteca.cadastrarFuncionario(nome, cpf, email, telefone, registro, cargo);
        salvarDados();
        System.out.println("Funcionario cadastrado: " + funcionario);
    }

    private void listarFuncionarios() {
        Collection<Funcionario> funcionarios = biblioteca.listarFuncionarios();
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionario cadastrado.");
            return;
        }
        funcionarios.forEach(System.out::println);
    }

    private void alterarFuncionario() throws BibliotecaException {
        listarFuncionarios();
        int id;
        Funcionario funcionario;
        while (true) {
            id = lerInteiro("ID do funcionario para alterar: ");
            try {
                funcionario = biblioteca.buscarFuncionario(id);
                break;
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("ID invalido. Digite um ID de funcionario existente.");
            }
        }
        System.out.println("Alterando: " + funcionario);
        String nome = lerTexto("Novo nome: ");
        String cpf = lerCpfValidoParaAlteracaoFuncionario(id);
        String email = lerEmailValido("Novo e-mail: ");
        String telefone = lerTelefoneValido("Novo telefone: ");
        String registro = lerTexto("Novo registro: ");
        String cargo = lerTexto("Novo cargo: ");
        String confirmacao = lerTexto("Confirme a alteracao digitando S: ");
        if (!"S".equalsIgnoreCase(confirmacao)) {
            System.out.println("Alteracao cancelada.");
            return;
        }
        biblioteca.alterarDadosFuncionario(id, nome, cpf, email, telefone, registro, cargo);
        salvarDados();
        System.out.println("Funcionario alterado com sucesso.");
    }

    private void removerFuncionario() throws BibliotecaException {
        limparTerminal();
        listarFuncionarios();
        int id;
        while (true) {
            id = lerInteiro("ID do funcionario para excluir: ");
            try {
                biblioteca.buscarFuncionario(id);
                break;
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("ID invalido. Digite um ID de funcionario existente.");
            }
        }
        String confirmacao = lerTexto("Confirme a exclusao digitando S: ");
        if (!"S".equalsIgnoreCase(confirmacao)) {
            System.out.println("Exclusao cancelada.");
            return;
        }
        biblioteca.removerFuncionario(id);
        salvarDados();
        System.out.println("Funcionario excluido com sucesso.");
    }

    private void cadastrarItem() throws BibliotecaException {
        System.out.println("\n1 - Livro");
        System.out.println("2 - Revista");
        int tipo = lerInteiro("Tipo de item: ");
        String titulo = lerTexto("Titulo: ");
        int ano = lerInteiro("Ano de publicacao: ");
        String categoriaNome = lerTexto("Categoria: ");
        String categoriaDescricao = lerTexto("Descricao da categoria: ");
        if (tipo == 1) {
            String isbn = lerIsbnValido("ISBN com 10 ou 13 digitos: ");
            String autorNome = lerTexto("Nome do autor: ");
            Livro livro = biblioteca.cadastrarLivro(titulo, ano, categoriaNome, categoriaDescricao, isbn, autorNome);
            salvarDados();
            limparTerminal();
            System.out.println("Livro cadastrado: " + livro);
        } else if (tipo == 2) {
            String issn = lerIssnValido("ISSN com 8 digitos: ");
            int edicao = lerInteiro("Numero da edicao: ");
            String periodicidade = lerTexto("Periodicidade: ");
            Revista revista = biblioteca.cadastrarRevista(titulo, ano, categoriaNome, categoriaDescricao, issn, edicao, periodicidade);
            salvarDados();
            limparTerminal();
            System.out.println("Revista cadastrada: " + revista);
        } else {
            System.out.println("Tipo invalido.");
        }
    }

    private void listarItens() {
        Collection<ItemAcervo> itens = biblioteca.listarItens();
        if (itens.isEmpty()) {
            System.out.println("Nenhum item cadastrado.");
            return;
        }

        List<Livro> livros = new LinkedList<>();
        List<Revista> revistas = new LinkedList<>();

        for (ItemAcervo item : itens) {
            if (item instanceof Livro) {
                livros.add((Livro) item);
            } else if (item instanceof Revista) {
                revistas.add((Revista) item);
            }
        }

        if (!livros.isEmpty()) {
            exibirGrupoLivros(livros);
        }

        if (!revistas.isEmpty()) {
            if (!livros.isEmpty()) {
                System.out.println();
            }
            exibirGrupoRevistas(revistas);
        }
    }

    private void exibirGrupoLivros(List<Livro> livros) {
        System.out.println("=== LISTAGEM DE LIVROS ===");
        livros.forEach(System.out::println);
    }

    private void exibirGrupoRevistas(List<Revista> revistas) {
        System.out.println("=== LISTAGEM DE REVISTAS ===");
        revistas.forEach(System.out::println);
    }

    private void alterarItem() throws BibliotecaException {
        listarItens();
        int id;
        ItemAcervo item;
        while (true) {
            id = lerInteiro("ID do item para alterar: ");
            try {
                item = biblioteca.buscarItem(id);
                break;
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("ID invalido. Digite um ID de item existente.");
            }
        }
        System.out.println("Alterando: " + item);
        String titulo = lerTexto("Novo titulo: ");
        int ano = lerInteiro("Novo ano de publicacao: ");
        String categoria = lerTexto("Nova categoria: ");
        String descricao = lerTexto("Nova descricao da categoria: ");
        biblioteca.alterarDadosItem(id, titulo, ano, categoria, descricao);
        if (item instanceof Livro) {
            String isbn = lerIsbnValido("Novo ISBN: ");
            String autor = lerTexto("Novo autor: ");
            biblioteca.alterarDadosLivro(id, isbn, autor);
        } else if (item instanceof Revista) {
            String issn = lerIssnValido("Novo ISSN: ");
            int edicao = lerInteiro("Nova edicao: ");
            String periodicidade = lerTexto("Nova periodicidade: ");
            biblioteca.alterarDadosRevista(id, issn, edicao, periodicidade);
        }
        salvarDados();
        limparTerminal();
        System.out.println("Item alterado com sucesso.");
    }

    private void removerItem() throws BibliotecaException {
        listarItens();
        int id = lerInteiro("ID do item para remover: ");
        biblioteca.removerItem(id);
        salvarDados();
        System.out.println("Item removido com sucesso.");
    }

    private void registrarEmprestimo() throws BibliotecaException {
        listarUsuarios();
        int usuarioId = lerInteiro("ID do usuario: ");
        listarItens();
        int itemId = lerInteiro("ID do item: ");
        Emprestimo emprestimo = biblioteca.registrarEmprestimo(usuarioId, itemId);
        System.out.println("Emprestimo registrado: " + emprestimo);
    }

    private void devolverEmprestimo() throws BibliotecaException {
        listarEmprestimos();
        int id = lerInteiro("ID do emprestimo para devolver: ");
        double multa = biblioteca.devolverEmprestimo(id);
        System.out.printf("Devolucao realizada. Multa calculada: R$ %.2f%n", multa);
    }

    private void renovarEmprestimo() throws BibliotecaException {
        listarEmprestimos();
        int id = lerInteiro("ID do emprestimo para renovar: ");
        biblioteca.renovarEmprestimo(id);
        System.out.println("Emprestimo renovado com sucesso.");
    }

    private void cancelarEmprestimo() throws BibliotecaException {
        listarEmprestimos();
        int id = lerInteiro("ID do emprestimo para cancelar: ");
        biblioteca.cancelarEmprestimo(id);
        System.out.println("Emprestimo cancelado com sucesso.");
    }

    private void listarEmprestimos() {
        List<Emprestimo> emprestimos = biblioteca.listarEmprestimos();
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum emprestimo registrado.");
            return;
        }
        emprestimos.forEach(System.out::println);
    }

    private void reservarItem() throws BibliotecaException {
        listarUsuarios();
        int usuarioId = lerInteiro("ID do usuario: ");
        listarItens();
        int itemId = lerInteiro("ID do item emprestado: ");
        Reserva reserva = biblioteca.reservarItem(usuarioId, itemId);
        System.out.println("Reserva registrada: " + reserva);
    }

    private void atenderReserva() throws BibliotecaException {
        listarReservas();
        int id = lerInteiro("ID da reserva para atender: ");
        Emprestimo emprestimo = biblioteca.atenderReserva(id);
        System.out.println("Reserva atendida e emprestimo criado: " + emprestimo);
    }

    private void cancelarReserva() throws BibliotecaException {
        listarReservas();
        int id = lerInteiro("ID da reserva para cancelar: ");
        biblioteca.cancelarReserva(id);
        System.out.println("Reserva cancelada com sucesso.");
    }

    private void listarReservas() {
        List<Reserva> reservas = biblioteca.listarReservas();
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva registrada.");
            return;
        }
        reservas.forEach(System.out::println);
    }

    private void listarMultas() {
        List<Multa> multas = biblioteca.listarMultas();
        if (multas.isEmpty()) {
            System.out.println("Nenhuma multa registrada.");
            return;
        }
        multas.forEach(System.out::println);
    }

    private void pagarMulta() throws BibliotecaException {
        listarMultas();
        int id = lerInteiro("ID da multa para pagar: ");
        biblioteca.pagarMulta(id);
        System.out.println("Multa paga com sucesso.");
    }

    private void inserirDadosExemplo() throws BibliotecaException {
        if (!biblioteca.listarUsuarios().isEmpty() || !biblioteca.listarItens().isEmpty() || !biblioteca.listarFuncionarios().isEmpty()) {
            System.out.println("Dados de exemplo so podem ser inseridos quando nao ha usuarios, itens nem funcionarios cadastrados.");
            return;
        }
        biblioteca.cadastrarAluno("Ana Souza", "11122233344", "ana@email.com", "84999990000", "20240001", "Sistemas de Informacao");
        biblioteca.cadastrarProfessor("Carlos Lima", "22233344455", "carlos@email.com", "84988887777", "SIAPE123", "Computacao");
        biblioteca.cadastrarFuncionario("Marina Costa", "33344455566", "marina@email.com", "84977776666", "REG001", "Bibliotecaria");
        biblioteca.cadastrarLivro("Programacao Orientada a Objetos", 2020, "Tecnologia", "Livros de computacao", "9788575220000",
                "Maria Oliveira");
        biblioteca.cadastrarRevista("Revista Ciencia Hoje", 2023, "Ciencia", "Revistas cientificas", "12345678", 45, "Mensal");
        System.out.println("Dados de exemplo cadastrados.");
    }

    private String lerCpfValido() {
        return lerCpfValido("CPF: ", null, null);
    }

    private String lerCpfValidoParaAlteracaoUsuario(int ignorarId) {
        return lerCpfValido("Novo CPF: ", ignorarId, null);
    }

    private String lerCpfValidoParaAlteracaoFuncionario(int ignorarId) {
        return lerCpfValido("Novo CPF: ", null, ignorarId);
    }

    private String lerCpfValido(String mensagem, Integer usuarioIgnoradoId, Integer funcionarioIgnoradoId) {
        while (true) {
            String cpf = lerTexto(mensagem);
            String cpfNormalizado = cpf == null ? "" : cpf.replaceAll("\\D", "");
            if (!cpfNormalizado.matches("\\d{11}")) {
                System.out.println("CPF invalido. Digite exatamente 11 digitos.");
                continue;
            }
            if (!isCpfValido(cpfNormalizado)) {
                System.out.println("CPF invalido. Digite um CPF valido.");
                continue;
            }
            if (cpfJaCadastrado(cpfNormalizado, usuarioIgnoradoId, funcionarioIgnoradoId)) {
                System.out.println("CPF ja cadastrado. Digite um CPF diferente.");
                continue;
            }
            return cpfNormalizado;
        }
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

    private boolean cpfJaCadastrado(String cpf, Integer usuarioIgnoradoId, Integer funcionarioIgnoradoId) {
        return biblioteca.listarUsuarios().stream()
                .anyMatch(usuario -> (usuarioIgnoradoId == null || usuario.getId() != usuarioIgnoradoId) && usuario.getCpf().equals(cpf))
                || biblioteca.listarFuncionarios().stream()
                        .anyMatch(funcionario -> (funcionarioIgnoradoId == null || funcionario.getId() != funcionarioIgnoradoId)
                                && funcionario.getCpf().equals(cpf));
    }

    private String lerEmailValido() {
        return lerEmailValido("E-mail: ");
    }

    private String lerEmailValido(String mensagem) {
        while (true) {
            String email = lerTexto(mensagem);
            if (email != null && email.contains("@") && email.contains(".")) {
                return email;
            }
            System.out.println("E-mail invalido. Digite um e-mail valido.");
        }
    }

    private String lerTelefoneValido() {
        return lerTelefoneValido("Telefone: ");
    }

    private String lerTelefoneValido(String mensagem) {
        while (true) {
            String telefone = lerTexto(mensagem);
            if (telefone != null && telefone.replaceAll("\\D", "").length() >= 8) {
                return telefone;
            }
            System.out.println("Telefone invalido. Digite pelo menos 8 digitos.");
        }
    }

    private String lerIsbnValido(String mensagem) {
        while (true) {
            String isbn = lerTexto(mensagem);
            String isbnNormalizado = isbn == null ? "" : isbn.replaceAll("\\D", "");
            if (isbnNormalizado.length() == 10 || isbnNormalizado.length() == 13) {
                return isbnNormalizado;
            }
            System.out.println("ISBN invalido. Digite apenas 10 ou 13 digitos.");
        }
    }

    private String lerIssnValido(String mensagem) {
        while (true) {
            String issn = lerTexto(mensagem);
            String issnNormalizado = issn == null ? "" : issn.replaceAll("\\D", "");
            if (issnNormalizado.length() == 8) {
                return issnNormalizado;
            }
            System.out.println("ISSN invalido. Digite exatamente 8 digitos.");
        }
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }
}
