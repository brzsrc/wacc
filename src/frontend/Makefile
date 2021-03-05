# Makefile for the frontend milestone for this compiler

ROOT_DIR    := ../..

ANTLR_DIR	        := $(ROOT_DIR)/antlr_config
SOURCE_FRONTEND_DIR	:= $(ROOT_DIR)/src/frontend
OUTPUT_FRONTEND_DIR	:= $(ROOT_DIR)/bin/frontend
UTILS_DIR           := $(ROOT_DIR)/src/utils/frontend $(ROOT_DIR)/src/utils/*.java

# Tools

ANTLR	:= antlrBuild
FIND	:= find
RM	  := rm -rf
MKDIR	:= mkdir -p
JAVA	:= java
JAVAC	:= javac

JFLAGS	:= -sourcepath $(SOURCE_FRONTEND_DIR) -d $(OUTPUT_FRONTEND_DIR) -cp $(ROOT_DIR)/lib/antlr-4.9.1-complete.jar

# the make rule for frontend

all: frontend

# runs the antlr build script then attempts to compile all .java files within src/frontend
frontend:
	cd $(ANTLR_DIR) && ./$(ANTLR) 
	$(FIND) $(SOURCE_FRONTEND_DIR) $(UTILS_DIR) -name '*.java' > $@
	$(MKDIR) $(OUTPUT_FRONTEND_DIR)
	$(JAVAC) $(JFLAGS) @$@
	$(RM) frontend

clean:
	$(RM) frontend $(OUTPUT_FRONTEND_DIR) $(SOURCE_FRONTEND_DIR)/antlr

.PHONY: all frontend clean


