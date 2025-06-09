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

### 1. Baixar o JFlex

- Faça o download do JFlex compilado, versão **1.8.2** (`jflex-full-1.8.2.jar`).
- Este arquivo pode ser encontrado no *Classroom* da disciplina.
- Salve o arquivo dentro da pasta `lib/` do projeto.

### 2. Gerar o analisador léxico

- Edite, se necessário, os seguintes arquivos em `src/`:
  - `Lexer.flex`
  - `Token.java`
  - `TOKEN_TYPE.java`

- Execute o comando abaixo para gerar o `Lexer.java`:

  ```bash
  java -jar lib/jflex-full-1.8.2.jar src/Lexer.flex
  ```

> Isso criará o arquivo `Lexer.java` automaticamente na pasta `src/`.

### 3. Compilar o projeto

Compile todos os arquivos Java, incluindo o `Lexer.java` recém-gerado:

```bash
javac -d bin src/TOKEN_TYPE.java src/Token.java src/Lexer.java src/Main.java
```

> Os arquivos `.class` serão gerados na pasta `bin/`.

### 4. Preparar o arquivo de entrada

- Crie um arquivo chamado `input.txt` com o conteúdo que deseja analisar.
- Coloque esse arquivo na **pasta raiz** do projeto.

### 5. Executar o programa

Rode o programa com o comando:

```bash
java -cp bin Main
```

---

## Observações

- O arquivo `Lexer.java` é gerado automaticamente pelo JFlex, por isso não está versionado no repositório.
- As pastas auxiliares (`.vscode`, `bin`, `lib`, `tools`, `leo`) também são ignoradas no Git.
- Certifique-se de ter o Java instalado e configurado corretamente no seu sistema (`java` e `javac` no PATH).

---

## Licença

Este projeto é distribuído apenas para fins educacionais, sem fins lucrativos.
