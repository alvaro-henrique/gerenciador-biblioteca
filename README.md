# Sistema de Gerenciamento de Biblioteca

Projeto em Java com interface de terminal para demonstrar Programacao Orientada a Objetos, CRUD, heranca, polimorfismo, encapsulamento, regras de negocio, excecoes personalizadas, persistencia em arquivos e diagrama UML.

## Requisitos atendidos

- Classes obrigatorias presentes: `Pessoa`, `Usuario`, `Aluno`, `Professor`, `Funcionario`, `Livro`, `Emprestimo`, `Reserva`, `Multa`, `Biblioteca` e `Persistencia`.
- CRUD disponivel para usuarios, funcionarios, itens, reservas e multas; emprestimos podem ser criados, listados, renovados, devolvidos e cancelados.
- Heranca:
  `Pessoa -> Usuario -> Aluno/Professor`
  `Pessoa -> Funcionario`
  `ItemAcervo -> Livro/Revista`
- Polimorfismo:
  `Aluno` e `Professor` definem limites, prazos e calculo de multa diferentes.
  `Livro` e `Revista` definem regras diferentes de renovacao.
- Regras de negocio implementadas:
  usuario com multa pendente nao pega emprestimo;
  livro indisponivel nao pode ser emprestado;
  aluno pode ter ate 3 emprestimos;
  professor pode ter ate 5 emprestimos;
  atrasos geram multas;
  reservas possuem prioridade por ordem de cadastro.
- Estado dinamico de `Emprestimo`: `ATIVO`, `ATRASADO`, `DEVOLVIDO`, `CANCELADO`.
- Excecoes especificas do enunciado presentes no pacote `exception`.
- Persistencia em arquivos JSON na pasta `dados`.
- UML em `diagrama-classes.puml`.

## Estrutura

```text
src/br/com/biblioteca/
  Main.java
  app/
  exception/
  model/
  persistence/
  service/
```

## Persistencia

Os dados sao salvos automaticamente na pasta `dados` nos arquivos:

- `usuarios.json`
- `funcionarios.json`
- `livros.json`
- `revistas.json`
- `emprestimos.json`
- `reservas.json`
- `multas.json`

## Como compilar

Requisito: JDK 11 ou superior.

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

## Roteiro rapido de demonstracao

1. Use a opcao `25` para inserir dados de exemplo.
2. Cadastre ou liste usuarios, funcionarios e livros.
3. Registre um emprestimo.
4. Tente renovar um item com reserva ativa.
5. Devolva um emprestimo atrasado para gerar multa.
6. Liste multas e quite a multa.
7. Salve e reabra o sistema para validar a persistencia.
