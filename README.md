# Projeto Compiladores

Este projeto contém a implementação de um analisador léxico utilizando [JFlex](https://jflex.de/) e Java. Ele faz parte do trabalho da disciplina de Teoria dos Compiladores.

## Estrutura do Projeto

- `src/`: arquivos fonte
  - `Lexer.flex`: especificação léxica
  - `Main.java`: ponto de entrada do programa
  - `Token.java`, `TOKEN_TYPE.java`: estruturas auxiliares
- `lib/`: bibliotecas utilizadas (ex: JFlex)
- `bin/`: arquivos compilados
- `input.txt`: arquivo de entrada que será analisado
- `.vscode/`, `tools/`: pastas auxiliares (ignoradas no repositório)

---

## Como Executar

Siga os passos abaixo para configurar e executar o projeto:

### 1. Baixar o JFlex e o Beaver

- Faça o download do JFlex compilado `jflex-full-1.8.2.jar`.
- Faça o download do runtime e do arquivo compilado do Beaver: `beaver-rt-0.9.11.jar` e `beaver-cc-0.9.11.jar`.
- Todos estão no *Classroom* da disciplina.
- Mova os arquivos para as pastas `/lib/jflex` e `/lib/beaver`.

### 2. Gerar o analisador léxico

- Execute o comando abaixo para gerar o `Lexer.java`:

  ```bash
  java -jar /lib/jflex/jflex-full-1.8.2.jar /src/parser/lexer.flex
  ```

> Isso criará o arquivo `Lexer.java` automaticamente na pasta `src/`.

Não é mais necessário as classes auxiliares. Isso é construído pelo Beaver;

### 2. Gerar o analisador sintático

- Execute o comando abaixo para gerar o `Parser.java` e o `Terminals.java`:

  ```bash
  java -jar /lib/beaver/beaver-cc-0.9.11.jar -d /src/parser /src/parser/lang.grammar
  ```

### 3. Compilar o projeto

Compile todos os arquivos Java, incluindo o `Lexer.java` recém-gerado:

```bash
javac -cp ".;lib/beaver/beaver-rt-0.9.11.jar" -d bin /src/parser/Terminals.java /src/parser/Parser.java /src/parser/Lexer.java /src/Main.java
```

> Os arquivos `.class` serão gerados na pasta `bin/`.

### 4. Executar o programa

Rode o programa com o comando:

```bash
java -cp bin Main <input>
```

---

## Observações

- As pastas auxiliares (`.vscode`, `bin`, `lib`, `tools`) também são ignoradas no Git.
- Certifique-se de ter o Java instalado e configurado corretamente no seu sistema (`java` e `javac` no PATH).

---