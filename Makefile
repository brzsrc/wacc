# Sample Makefile for the WACC Compiler lab: edit this to build your own comiler
# Locations

SOURCE_DIR := src
BIN_DIR    := bin
RM         := rm -rf

all: compiler

compiler:
	cd $(SOURCE_DIR) && make

clean:
	cd $(SOURCE_DIR) && make clean && $(RM) $(BIN_DIR)

.PHONY: all compiler clean


