# Trabalho de Compiladores — Linguagem Lang

**UFJF - Universidade Federal de Juiz de Fora**  
**Departamento de Ciência da Computação**  
**Disciplina: Compiladores**

**Desenvolvido por:**  
- Gabriel Martins da Costa Medeiros — 201935032  
- Matheus Peron Resende Corrêa — 201965089C  

---

## ✅ Pré-requisitos

Para compilar e executar este projeto, é necessário:

- Ter o **Java SDK** instalado (recomenda-se Java 17 ou superior).
- Ter o utilitário `dot` instalado (para gerar imagens da AST).  
  Para instalar no Ubuntu/Debian, execute:

```bash
sudo apt install graphviz
```

---

## ⚙️ Compilação e Execução

O projeto utiliza um `Makefile` para automatizar a compilação e execução do compilador.

### Para compilar e rodar o interpretador com um arquivo

```bash
make run ACTION=-i FILE=caminho/para/arquivo
```

### ℹ️ Parâmetros disponíveis:

- `ACTION=-i`  
  Executa o **interpretador** da linguagem Lang, interpretando o código fonte.

- `ACTION=-dot`  
  Executa o parser e o **DotVisitor**, gerando o arquivo `ast.dot`.  
  Em seguida, o comando `dot` converte para a imagem `ast.png`.  
  > **Importante:** Requer o `graphviz` instalado (com o comando `dot` acessível no terminal).

- `ACTION=-syn`  
  Executa **apenas o analisador sintático**, verificando se o código fonte está sintaticamente correto.

- `FILE=...`  
  Caminho para o **arquivo de entrada** a ser analisado ou interpretado.

---

### 📌 Exemplos de uso:

```bash
make run ACTION=-syn FILE=tests/certo/exemplo
make run ACTION=-dot FILE=tests/interpretador/fibonacci
make run ACTION=-i FILE=input.txt
```

---

## 🧹 Limpeza

Para remover todos os arquivos gerados durante a compilação:

```bash
make clean
```

---

## 🔎 Verificações rápidas

- Verificar se o Java está corretamente instalado:

```bash
java -version
```

- Verificar se o `dot` está disponível (necessário para o modo `-dot`):

```bash
dot -V
```

---
