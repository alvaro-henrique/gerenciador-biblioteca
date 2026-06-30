# Sistema de Gerenciamento de Biblioteca

Projeto academico em Java com interface de terminal, foco em Programacao Orientada a Objetos, persistencia em arquivos JSON e regras de negocio para usuarios, funcionarios, itens do acervo, emprestimos, reservas e multas.

## Integrantes

- Alvaro Henrique Soares da Silva
- Helder Pereira Sa Paixao
- Jose Francisco de Souza Fonseca
- Matheus Marcos da Silva
- Thiago Eduardo Bezerra de Castro

## Visao geral

O sistema foi construido para demonstrar:

- POO com heranca, polimorfismo, abstracao e encapsulamento
- CRUD de usuarios, funcionarios e itens
- Fluxos de emprestimo, devolucao, renovacao, cancelamento e reserva
- Regras de negocio com excecoes personalizadas
- Persistencia local em arquivos JSON
- Estado dinamico de emprestimos e reservas
- Documentacao UML em `diagrama-classes.puml`

## Tecnologias usadas

- Java
- JDK 11 ou superior
- Interface via terminal
- Persistencia em arquivos JSON
- Parser JSON proprio do projeto, sem bibliotecas externas
- Compilacao para a pasta `out`

## Como compilar

No PowerShell:

```powershell
$dest = "out"
New-Item -ItemType Directory -Force -Path $dest | Out-Null
$sources = Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d $dest $sources
```

## Como executar

```powershell
java -cp out br.com.biblioteca.Main
```

## Estrutura do projeto

```text
gestao-biblioteca-poo/
├─ dados/
│  ├─ usuarios.json
│  ├─ funcionarios.json
│  ├─ livros.json
│  ├─ revistas.json
│  ├─ emprestimos.json
│  ├─ reservas.json
│  └─ multas.json
├─ out/
├─ src/
│  └─ br/com/biblioteca/
│     ├─ Main.java
│     ├─ app/
│     ├─ exception/
│     ├─ model/
│     ├─ persistence/
│     └─ service/
├─ diagrama-classes.puml
├─ run.bat
├─ run.sh
└─ README.md
```

## O que cada pasta faz

- `src/`: codigo-fonte do sistema
- `dados/`: base persistida em JSON
- `out/`: classes compiladas
- `diagrama-classes.puml`: diagrama UML do projeto
- `run.bat` e `run.sh`: atalhos simples para execucao

## O que cada arquivo fonte faz

### Arquivo raiz

- `src/br/com/biblioteca/Main.java`
  ponto de entrada da aplicacao; instancia `BibliotecaApp` e inicia o menu.

### Pacote `app`

- `src/br/com/biblioteca/app/BibliotecaApp.java`
  interface de terminal do sistema; contem o menu principal, entrada de dados, mensagens ao usuario, listagens, validacoes de selecao e chamadas para o servico.

### Pacote `service`

- `src/br/com/biblioteca/service/Biblioteca.java`
  camada central de regra de negocio; controla cadastros, alteracoes, remocoes, emprestimos, reservas, multas, limites, prioridades e integridade do historico.

### Pacote `persistence`

- `src/br/com/biblioteca/persistence/Persistencia.java`
  contrato abstrato de persistencia.
- `src/br/com/biblioteca/persistence/RepositorioBibliotecaArquivo.java`
  implementacao concreta da persistencia em JSON; salva e carrega usuarios, funcionarios, livros, revistas, emprestimos, reservas e multas.
- `src/br/com/biblioteca/persistence/JsonUtil.java`
  utilitario interno para escrever e ler JSON sem dependencia externa.

### Pacote `model`

- `src/br/com/biblioteca/model/Pessoa.java`
  classe base com `id`, `nome` e `cpf`.
- `src/br/com/biblioteca/model/Usuario.java`
  classe abstrata de usuario com `email`, `telefone`, `ativo`, limite, prazo e calculo de multa.
- `src/br/com/biblioteca/model/Aluno.java`
  usuario aluno; define matricula, curso, limite 3, prazo 7 dias e multa de R$ 2,00 por dia.
- `src/br/com/biblioteca/model/Professor.java`
  usuario professor; define SIAPE, departamento, limite 5, prazo 14 dias e multa de R$ 1,00 por dia.
- `src/br/com/biblioteca/model/Funcionario.java`
  representa funcionario da biblioteca.
- `src/br/com/biblioteca/model/Categoria.java`
  categoria usada por itens do acervo.
- `src/br/com/biblioteca/model/ItemAcervo.java`
  classe abstrata para itens do acervo, com titulo, ano, categoria e disponibilidade.
- `src/br/com/biblioteca/model/Livro.java`
  item emprestavel com ISBN e nome do autor.
- `src/br/com/biblioteca/model/Revista.java`
  item nao emprestavel do fluxo de emprestimo, com ISSN, numero de edicao e periodicidade.
- `src/br/com/biblioteca/model/Emprestimo.java`
  representa emprestimos; controla datas, estado, renovacoes, atraso e calculo de multa.
- `src/br/com/biblioteca/model/Reserva.java`
  representa reservas; controla usuario, item, data e estado.
- `src/br/com/biblioteca/model/Multa.java`
  representa multa gerada por atraso; controla valor, data de geracao, pagamento e pendencia.
- `src/br/com/biblioteca/model/EstadoEmprestimo.java`
  enum com `ATIVO`, `ATRASADO`, `DEVOLVIDO` e `CANCELADO`.
- `src/br/com/biblioteca/model/EstadoReserva.java`
  enum com `ATIVA`, `ATENDIDA` e `CANCELADA`.

### Pacote `exception`

- `src/br/com/biblioteca/exception/BibliotecaException.java`
  excecao base do projeto.
- `src/br/com/biblioteca/exception/ValidacaoException.java`
  erros de validacao de dados.
- `src/br/com/biblioteca/exception/OperacaoInvalidaException.java`
  erros de regra de negocio.
- `src/br/com/biblioteca/exception/EntidadeNaoEncontradaException.java`
  base para erros de busca.
- `src/br/com/biblioteca/exception/UsuarioNaoEncontradoException.java`
  usuario nao encontrado.
- `src/br/com/biblioteca/exception/LivroNaoEncontradoException.java`
  item ou livro nao encontrado.
- `src/br/com/biblioteca/exception/LivroIndisponivelException.java`
  tentativa de emprestimo de livro indisponivel.
- `src/br/com/biblioteca/exception/LimiteEmprestimosException.java`
  limite de emprestimos excedido.
- `src/br/com/biblioteca/exception/MultaPendenteException.java`
  bloqueio por multa pendente.
- `src/br/com/biblioteca/exception/TransicaoEstadoInvalidaException.java`
  mudanca invalida de estado em emprestimos ou reservas.
- `src/br/com/biblioteca/exception/PersistenciaException.java`
  falhas de leitura e escrita dos arquivos.

## Arquivos de dados

- `dados/usuarios.json`: alunos e professores
- `dados/funcionarios.json`: funcionarios
- `dados/livros.json`: livros
- `dados/revistas.json`: revistas
- `dados/emprestimos.json`: historico e emprestimos em aberto
- `dados/reservas.json`: reservas ativas, atendidas e canceladas
- `dados/multas.json`: multas pagas e nao pagas

## Menu principal e fluxos do sistema

### 1. Cadastrar usuario

- Permite escolher entre aluno e professor
- Valida CPF, email e telefone
- Nao permite CPF duplicado
- Salva nome, dados comuns e dados especificos do tipo

### 2. Listar usuarios

- Limpa o terminal
- Agrupa em `ALUNOS` e `PROFESSORES`
- Exibe limite e prazo uma vez por grupo
- Exige `0` para voltar ao menu principal

### 3. Alterar usuario

- Valida se o ID existe
- Altera apenas campos permitidos do usuario
- Valida CPF, email e telefone
- Exige confirmacao antes de salvar

### 4. Excluir usuario

- Limpa o terminal
- Valida se o ID existe
- Exige confirmacao
- Salva apos excluir

### 5. Cadastrar funcionario

- Valida CPF, email e telefone
- Valida registro unico
- Salva imediatamente

### 6. Listar funcionarios

- Limpa o terminal
- Exige `0` para voltar ao menu principal

### 7. Alterar funcionario

- Valida se o ID existe
- Replica as validacoes do cadastro
- Exige confirmacao
- Salva ao final

### 8. Excluir funcionario

- Limpa o terminal
- Valida se o ID existe
- Exige confirmacao
- Salva ao final

### 9. Cadastrar item

- Permite escolher entre livro e revista
- Livro valida ISBN com 10 ou 13 digitos
- Revista valida ISSN com 8 digitos
- Salva e limpa a tela ao final

### 10. Listar itens

- Limpa o terminal
- Separa em `LISTAGEM DE LIVROS` e `LISTAGEM DE REVISTAS`
- Exige `0` para voltar ao menu principal

### 11. Alterar item

- Valida se o ID existe
- Replica a logica do cadastro
- Salva ao final

### 12. Remover item

- Remove apenas itens sem emprestimo ativo, sem reserva ativa e sem historico vinculado
- Salva ao final

### 13. Registrar emprestimo

- Mostra usuarios por tipo
- Exibe quantidade de emprestimos por usuario
- Bloqueia usuario com multa pendente
- Bloqueia usuario com emprestimo atrasado
- Mostra apenas livros no fluxo de emprestimo
- Separa livros disponiveis e indisponiveis
- So conclui com livro disponivel
- Salva ao final

### 14. Registrar devolucao

- Lista livros emprestados
- Devolve o livro
- Gera multa se houver atraso
- Salva ao final

### 15. Renovar emprestimo

- Lista livros emprestados
- So renova emprestimos ativos e dentro do prazo
- Bloqueia renovacao quando existe reserva ativa para o item
- Registra data da ultima renovacao
- Salva ao final

### 16. Cancelar emprestimo

- Lista livros emprestados
- Cancela apenas emprestimos validos

### 17. Listar livros emprestados

- Lista apenas emprestimos em aberto
- Exibe livro, usuario, datas e renovacoes

### 18. Listar livros emprestados por usuario

- Limpa o terminal
- Agrupa por usuario
- Exige `0` para voltar ao menu principal

### 19. Reservar item emprestado

- Lista usuarios em formato resumido
- Orienta a selecao do usuario
- Lista apenas livros emprestados
- Usa ID do livro, nao ID do emprestimo
- Valida ID de usuario e livro
- Salva ao final

### 20. Atender reserva

- Lista apenas reservas ativas
- Valida ID da reserva
- So atende se o item estiver disponivel
- Respeita a prioridade da reserva mais antiga
- Salva ao final
- Se der erro de item ainda indisponivel, tambem pede `0` para voltar

### 21. Cancelar reserva

- Lista reservas
- Orienta a selecao
- Valida ID da reserva
- Salva ao final
- Exige `0` para voltar ao menu principal

### 22. Listar reservas

- Exibe cabecalho proprio
- Separa em reservas ativas, atendidas e canceladas
- Exige `0` para voltar ao menu principal

### 23. Listar multas

- Exibe cabecalho proprio
- Separa em `Multas Nao Pagas` e `Multas Pagas`
- Exige `0` para voltar ao menu principal

### 24. Pagar multa

- Lista multas
- Paga apenas multa pendente
- Salva ao final

### 25. Inserir dados de exemplo

- Popula o sistema com exemplos basicos
- So funciona quando nao ha usuarios, itens nem funcionarios cadastrados

## Regras de negocio implementadas

- Usuario com multa pendente nao pode realizar emprestimos
- Usuario com emprestimo atrasado nao pode realizar novos emprestimos
- Aluno pode possuir ate 3 emprestimos em aberto
- Professor pode possuir ate 5 emprestimos em aberto
- Apenas livros podem ser emprestados
- Livros indisponiveis nao podem ser emprestados
- Revistas nao entram no fluxo de emprestimo
- Atrasos geram multas na devolucao
- Reserva ativa mais antiga tem prioridade
- Livro disponivel nao precisa de reserva
- Apenas livros emprestados podem ser reservados
- Nao e permitido remover usuario ou item com historico vinculado

## Validacoes implementadas

### Usuario

- CPF obrigatorio, numerico e valido
- CPF unico entre usuarios e funcionarios
- Email obrigatorio com formato basico valido
- Telefone com pelo menos 8 digitos
- Matricula minima para aluno
- SIAPE minimo para professor
- Curso obrigatorio para aluno
- Departamento obrigatorio para professor

### Funcionario

- CPF obrigatorio, valido e unico
- Email obrigatorio
- Telefone com pelo menos 8 digitos
- Registro obrigatorio e unico
- Cargo obrigatorio

### Livro

- Titulo minimo
- Ano entre 1400 e ano atual
- Categoria obrigatoria
- ISBN com 10 ou 13 digitos
- Nome do autor com pelo menos 3 caracteres
- Titulo unico no acervo

### Revista

- ISSN com 8 digitos
- Numero de edicao maior que zero
- Periodicidade obrigatoria
- Titulo unico no acervo

### Emprestimo e reserva

- IDs precisam existir
- So aceita transicoes validas de estado
- So aceita renovacao quando o item permite
- Limite de renovacoes respeitado
- Reserva duplicada para mesmo usuario e item nao e permitida

## Estados dinamicos

### Emprestimo

- `ATIVO`
- `ATRASADO`
- `DEVOLVIDO`
- `CANCELADO`

### Reserva

- `ATIVA`
- `ATENDIDA`
- `CANCELADA`

## Como a multa funciona

- O atraso e calculado pelo `Emprestimo`
- O valor final depende do tipo de usuario
- Aluno: `R$ 2,00` por dia
- Professor: `R$ 1,00` por dia
- A multa e criada na devolucao atrasada
- Enquanto a multa estiver pendente, o usuario fica bloqueado para novos emprestimos

## Persistencia

O sistema carrega automaticamente os dados ao iniciar e salva em pontos importantes do fluxo, como:

- cadastro
- alteracao
- exclusao
- emprestimo
- devolucao
- renovacao
- reserva
- atendimento de reserva
- pagamento de multa

## UML

O diagrama do projeto esta em:

- `diagrama-classes.puml`

## Estado atual do projeto

- Compila na pasta `out`
- Usa apenas persistencia em `dados/`
- Possui regras de multa, reserva, devolucao e renovacao implementadas
- Foi validado com bateria de testes de integracao nos fluxos principais durante a manutencao
