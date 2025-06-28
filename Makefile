# Caminhos
JFLEX_JAR = lib/jflex/jflex-full-1.8.2.jar
BEAVER_JAR = lib/beaver/beaver-cc-0.9.11.jar
BEAVER_RT = lib/beaver/beaver-rt-0.9.11.jar
SRC_DIR = src
BIN_DIR = bin

# Entradas
FLEX_FILE = $(SRC_DIR)/parser/lexer.flex
GRAMMAR_FILE = $(SRC_DIR)/parser/lang.grammar
MAIN_CLASS = Main
INPUT_FILE = input.txt

# Targets principais
all: lexer parser compile run

lexer:
	java -cp "$(JFLEX_JAR)" jflex.Main $(FLEX_FILE)

parser:
	java -jar $(BEAVER_JAR) -T $(GRAMMAR_FILE)

compile:
	javac -cp ".;$(BEAVER_RT)" -d $(BIN_DIR) $(SRC_DIR)/ast/*.java $(SRC_DIR)/visitors/*.java $(SRC_DIR)/parser/*.java $(SRC_DIR)/$(MAIN_CLASS).java

run:
	java -cp "$(BIN_DIR);$(BEAVER_RT)" $(MAIN_CLASS) $(INPUT_FILE)

clean:
	rm -rf $(BIN_DIR)/*.class
