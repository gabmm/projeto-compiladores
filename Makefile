ifeq ($(OS),Windows_NT)
    MKDIR_P      = if not exist $(subst /,\,$(1)) mkdir $(subst /,\,$(1))
    RM           = rmdir /s /q $(subst /,\,$(1)) 2>nul || del /f /q $(subst /,\,$(1)) 2>nul || echo.
    CP_SEPARATOR = ;
    JAVA_SOURCES = $(shell dir /s /b $(subst /,\,$(SRC_DIR))\\*.java)
	REDIRECT_STDERR = 2>NUL
else
    MKDIR_P      = mkdir -p $(1)
    RM           = rm -rf $(1)
    CP_SEPARATOR = :
    JAVA_SOURCES = $(shell find $(SRC_DIR) -name '*.java')
	REDIRECT_STDERR = 2>/dev/null
endif

JFLEX_JAR  = lib/jflex/jflex-full-1.8.2.jar
BEAVER_JAR = lib/beaver/beaver-cc-0.9.11.jar
BEAVER_RT  = lib/beaver/beaver-rt-0.9.11.jar
ST_JAR     = lib/string_template/ST-4.3.1.jar
ANTLR_JAR  = lib/string_template/antlr-3.5.2-runtime.jar
SRC_DIR    = src
BIN_DIR    = bin

FLEX_FILE    = $(SRC_DIR)/parser/lexer.flex
GRAMMAR_FILE = $(SRC_DIR)/parser/lang.grammar
MAIN_CLASS   = Main

TEST_DIR_CORRECT  = tests/certo
TEST_DIR_WRONG    = tests/errado
TEST_DIR_INTERP   = tests/interpretador

TEST_FILES_CORRECT = $(wildcard $(TEST_DIR_CORRECT)/*.lan)
TEST_FILES_WRONG   = $(wildcard $(TEST_DIR_WRONG)/*.lan)
TEST_FILES_INTERP  = $(wildcard $(TEST_DIR_INTERP)/*.lan)

ACTION ?= -i
FILE   ?= input.txt

all: compile

lexer:
	java -cp "$(JFLEX_JAR)" jflex.Main $(FLEX_FILE)

parser:
	java -jar $(BEAVER_JAR) -T $(GRAMMAR_FILE) $(REDIRECT_STDERR)

compile: lexer parser
	@$(call MKDIR_P,$(BIN_DIR))
	@javac -cp "$(BEAVER_RT)$(CP_SEPARATOR)$(ST_JAR)$(CP_SEPARATOR)$(ANTLR_JAR)$(CP_SEPARATOR)." -d $(BIN_DIR) $(JAVA_SOURCES)




test-all: test-syn-correct test-syn-wrong test-interp

test-syn-correct: compile
	@echo "\n======================================================="
	@echo "  TESTANDO SINTAXE DOS ARQUIVOS CERTOS"
	@echo "======================================================="
	@for %%f in ($(TEST_FILES_CORRECT)) do ( \
		echo. && \
		echo "--- [PASSAR] Testando [%%f] ---" && \
		java -cp "$(BIN_DIR)$(CP_SEPARATOR)$(BEAVER_RT)" $(MAIN_CLASS) -syn %%f \
	)

# Testa a sintaxe dos ficheiros que DEVEM FALHAR
test-syn-wrong: compile
	@echo "\n======================================================="
	@echo "  TESTANDO SINTAXE DOS ARQUIVOS ERRADOS"
	@echo "======================================================="
	-@for %%f in ($(TEST_FILES_WRONG)) do ( \
		echo. && \
		echo "--- [FALHAR] Testando [%%f] ---" && \
		java -cp "$(BIN_DIR)$(CP_SEPARATOR)$(BEAVER_RT)" $(MAIN_CLASS) -syn %%f \
	)

# Testa o interpretador nos ficheiros designados
test-interp: compile
	@echo "\n======================================================="
	@echo "  TESTANDO INTERPRETADOR"
	@echo "======================================================="
	@for %%f in ($(TEST_FILES_INTERP)) do ( \
		echo. && \
		echo "--- [INTERPRET] Testando [%%f] ---" && \
		java -cp "$(BIN_DIR)$(CP_SEPARATOR)$(BEAVER_RT)" $(MAIN_CLASS) -i %%f \
	)
	@echo "================================================="
	@echo "        BATERIA DE TESTES CONCLUIDA        "
	@echo "================================================="

run: compile
	@echo "--- Executando Compilador (Acao: $(ACTION), Arquivo: $(FILE)) ---"
	@java -cp "$(BIN_DIR)$(CP_SEPARATOR)$(BEAVER_RT)$(CP_SEPARATOR)$(ST_JAR)$(CP_SEPARATOR)$(ANTLR_JAR)" $(MAIN_CLASS) $(ACTION) $(FILE)
ifeq ($(ACTION),-dot)
	@echo "--- Gerando imagem da AST ---"
	@dot -Tpng ast.dot -o ast.png || echo "Erro ao gerar imagem com dot."
endif

clean:
	$(call RM,$(BIN_DIR))
	$(call RM,$(SRC_DIR)/parser/Lexer.java)
	$(call RM,$(SRC_DIR)/parser/Parser.java)
	$(call RM,$(SRC_DIR)/parser/Parser.beaver.log)
	$(call RM,$(SRC_DIR)/parser/Parser.beaver.symbols)