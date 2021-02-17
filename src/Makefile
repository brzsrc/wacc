# Makefile in src for compiling both frontend and backend, as well as optimization and Compile.java

JAVA	:= java
JAVAC	:= javac

ROOT_DIR     := ..
SOURCE_DIR   := .
OUTPUT_DIR   := $(ROOT_DIR)/bin
FRONTEND_DIR := frontend
BACKEND_DIR  := backend
OPTIMIZE_DIR := optimize
ENTRY_FILE   := $(SOURCE_DIR)/Compiler.java

JFLAGS	:= -sourcepath $(SOURCE_DIR) -d $(OUTPUT_DIR) -cp $(ROOT_DIR)/lib/antlr-4.9.1-complete.jar

# the make rules

all: frontend backend optimize main

frontend:
	cd $(FRONTEND_DIR) && make

backend:
	cd $(BACKEND_DIR) && make

optimize:
	cd $(OPTIMIZE_DIR) && make

main:
	$(JAVAC) $(JFLAGS) $(ENTRY_FILE)

clean:
	cd $(FRONTEND_DIR) && make clean
	cd $(BACKEND_DIR) && make clean
	cd $(OPTIMIZE_DIR) && make clean

.PHONY: all frontend backend optimize main clean


