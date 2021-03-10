# # Makefile for the extension milestone for this compiler, a.k.a. optimization part of the compiler

ROOT_DIR    := ../..

SOURCE_OPTIMISE_DIR	:= $(ROOT_DIR)/src/optimize
OUTPUT_OPTIMISE_DIR	:= $(ROOT_DIR)/bin/optimize

# Tools
FIND	:= find
RM	    := rm -rf
MKDIR	:= mkdir -p
JAVA	:= java
JAVAC	:= javac

JFLAGS	:= -sourcepath $(SOURCE_OPTIMISE_DIR) -d $(OUTPUT_OPTIMISE_DIR) -cp $(ROOT_DIR)/lib/antlr-4.9.1-complete.jar

all: optimize

optimize:
	$(FIND) $(SOURCE_OPTIMISE_DIR) -name '*.java' > $@
	$(MKDIR) $(OUTPUT_OPTIMISE_DIR)
	$(JAVAC) $(JFLAGS) @$@
	$(RM) optimise

clean:
	$(RM) optimise $(OUTPUT_OPTIMISE_DIR) $(SOURCE_OPTIMISE_DIR)/antlr

PHONY: clean
