# Makefile for the backend milestone for this compiler

ROOT_DIR    := ../..

SOURCE_BACKEND_DIR	:= $(ROOT_DIR)/src/backend
OUTPUT_BACKEND_DIR	:= $(ROOT_DIR)/bin/backend
UTILS_DIR           := $(ROOT_DIR)/src/utils

# Notice that in order to `make backend`, frontend must be `make`d
SOURCE_FRONTEND_DIR :=$(ROOT_DIR)/src/frontend

# Tools
FIND	:= find
RM	    := rm -rf
MKDIR	:= mkdir -p
JAVA	:= java
JAVAC	:= javac

JFLAGS	:= -sourcepath $(SOURCE_BACKEND_DIR):$(SOURCE_FRONTEND_DIR) -d $(OUTPUT_BACKEND_DIR)

all: backend

backend:
	cd $(SOURCE_FRONTEND_DIR) && make
	$(FIND) $(SOURCE_BACKEND_DIR) $(UTILS_DIR) -name '*.java' > $@
	$(MKDIR) $(OUTPUT_BACKEND_DIR)
	$(JAVAC) $(JFLAGS) @$@
	$(RM) backend

clean:
	$(RM) backend $(OUTPUT_BACKEND_DIR)

PHONY: backend clean