# Sample Makefile for the WACC Compiler lab: edit this to build your own comiler
# Locations

SOURCE_DIR = src

all: compiler

compiler:
	cd $(SOURCE_DIR) && make

clean:
	cd $(SOURCE_DIR) && make clean

.PHONY: all compiler clean


