# Trabalho de Compiladores — Linguagem Lang

**UFJF - Universidade Federal de Juiz de Fora**  
**Departamento de Ciência da Computação**  
**Disciplina: Compiladores**

**Desenvolvido por:**  
- Gabriel Martins da Costa Medeiros — 201935032  
- Matheus Peron Resende Corrêa — 201965089C  

---

## ✅ Pré‑requisitos

- **Java SDK** (recomendado: Java 17 ou superior).
- **GNU Make** (para usar o `Makefile`).
- **Graphviz** (para o modo `-dot`, comando `dot`).  
  Instalação (Ubuntu/Debian):
  ```bash
  sudo apt install graphviz
  ```

---

## ⚙️ Como usar (compilar/rodar)

O projeto usa um `Makefile` com a meta `run`. Você escolhe **o que fazer** via a variável `ACTION` e informa o arquivo fonte via `FILE`.

```bash
make run ACTION=<diretiva> FILE=caminho/para/arquivo.lang
```

### 📋 Diretivas disponíveis (ACTION)

- `-i` — **Interpretador**  
  Executa o interpretador da linguagem Lang sobre o arquivo fonte.

- `-syn` — **Somente análise sintática**  
  Roda apenas o parser; útil para checar se a entrada é sintaticamente válida.

- `-dot` — **Gera imagem da AST**  
  Gera `ast.dot` e, em seguida, `ast.png` usando `dot`.  
  > Requer o Graphviz instalado.

- `-t` — **Analisador semântico estático**  
  Roda o verificador de tipos. Imprime na tela todos os erros encontrados. 

- `-src` — **Gerador *source‑to‑source***  
  Gera o **código Java equivalente** ao programa Lang.  
  - Saída: `LangProgram.java` na **pasta raiz** do projeto.  
  - Para **compilar e executar** o Java gerado:  
    ```bash
    make java
    ```

- `-gen` — **Gerador *source‑to‑Jasmin***  
  Gera o **código Jasmin** (bytecode assembly da JVM) para o programa Lang.  
  - Saída: `ProgramaLang.j` na **pasta raiz** do projeto.  
  - Para **montar e executar** o Jasmin gerado:  
    ```bash
    make jasmin
    ```
---

## 🧪 Exemplos

Verificar apenas a sintaxe:
```bash
make run ACTION=-syn FILE=tests/certo/exemplo.lang
```

Verificar se um programa é bem tipado:
```bash
make run ACTION=-t FILE=input.lang
```

Gerar e visualizar a AST:
```bash
make run ACTION=-dot FILE=tests/interpretador/fibonacci.lang
# Resultado: ast.dot e ast.png na raiz
```

Interpretar um programa:
```bash
make run ACTION=-i FILE=tests/interpretador/fatorial.lang
```

Gerar **Java** a partir de Lang e executar:
```bash
make run ACTION=-src FILE=input.lang
make java
```

Gerar **Jasmin** a partir de Lang e executar:
```bash
make run ACTION=-gen FILE=input.lang
make jasmin
```