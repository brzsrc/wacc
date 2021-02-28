#!/bin/bash

VALID_EXAMPLES=("/advanced"
                "/array"
                "/basic"
                "/expressions"
                "/function"
                "/if"
                "/IO"
                "/pairs"
                "/runtimeErr"
                "/scope"
                "/sequence"
                "/variables"
                "/while")

VALID_EXAMPLES_SRC_DIR="./src/test/examples/valid"
ASSEMBLY_OUTPUT_DIR="./log/assembly"
EXECUTE_OUTPUT_DIR="./log/output"

# counters to represent the total number of test files to be processed
TOTAL_COUNT=$(find "${VALID_EXAMPLES[@]/#/${VALID_EXAMPLES_SRC_DIR}}" -name "*.wacc" | wc -l)
COUNTER=0

for folder in ${VALID_EXAMPLES[@]}; do
  ASSEMBLY_OUTPUT_VALID_FOLDER="${ASSEMBLY_OUTPUT_DIR}${folder}"
  EXECUTE_OUTPUT_VALID_FOLDER="${EXECUTE_OUTPUT_DIR}${folder}"
  for file in $(find "${VALID_EXAMPLES_SRC_DIR}${folder}" -name "*.wacc")
  do
    FILE_NAME=$(basename "${file%.*}")
    EXECUTABLE_FILE_NAME="${ASSEMBLY_OUTPUT_VALID_FOLDER}/${FILE_NAME}"
    EXECUTABLE_OUTPUT_FILE="${EXECUTE_OUTPUT_VALID_FOLDER}/${FILE_NAME}"
    echo $file
    ./compile -a $file 1> "${EXECUTABLE_FILE_NAME}.s" 2> "${EXECUTABLE_FILE_NAME}.log"
    arm-linux-gnueabi-gcc -o $EXECUTABLE_FILE_NAME -mcpu=arm1176jzf-s -mtune=arm1176jzf-s "$EXECUTABLE_FILE_NAME.s"
    qemu-arm -L /usr/arm-linux-gnueabi/ $EXECUTABLE_FILE_NAME 1> "$EXECUTABLE_OUTPUT_FILE.output" 2> "$EXECUTABLE_OUTPUT_FILE.output.log"
    (( COUNTER += 1 ))
    echo "$COUNTER / $(($TOTAL_COUNT)) files have been executed"
    echo ""
  done

  echo "========================================================================================"
  echo "Test Folder" $folder "has been processed" "($COUNTER / $(($TOTAL_COUNT)))"
  echo "========================================================================================"
done

if [ $COUNTER -ne $TOTAL_COUNT ]; then
  echo "There are still " "$(($TOTAL_COUNT - $COUNTER)) / $(($TOTAL_COUNT))" " test cases failed to be assembled!"
  exit 1
fi

zip -r log.zip ./log/output