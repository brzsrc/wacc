#!/bin/bash

VALID_EXAMPLES=(
                 "/constantPropagation/propagation"
                 "/constantPropagation/evaluation"
                )

VALID_EXAMPLES_SRC_DIR="./src/test/custom/valid"
ASSEMBLY_OUTPUT_DIR="./log/assembly"
EXECUTE_OUTPUT_DIR="./log/output"

mkdir log
mkdir $ASSEMBLY_OUTPUT_DIR
mkdir $EXECUTE_OUTPUT_DIR

# counters to represent the total number of test files to be processed
TOTAL_COUNT=$(find "${VALID_EXAMPLES[@]/#/${VALID_EXAMPLES_SRC_DIR}}" -name "*.wacc" | wc -l)
COUNTER=0

for folder in ${VALID_EXAMPLES[@]}; do
  ASSEMBLY_OUTPUT_VALID_FOLDER="${ASSEMBLY_OUTPUT_DIR}${folder}"
  EXECUTE_OUTPUT_VALID_FOLDER="${EXECUTE_OUTPUT_DIR}${folder}"
  mkdir $EXECUTE_OUTPUT_VALID_FOLDER
  mkdir $ASSEMBLY_OUTPUT_VALID_FOLDER
  for file in $(find "${VALID_EXAMPLES_SRC_DIR}${folder}" -name "*.wacc")
  do
    FILE_NAME=$(basename "${file%.*}")
    EXECUTABLE_FILE_NAME="${ASSEMBLY_OUTPUT_VALID_FOLDER}/${FILE_NAME}"
    EXECUTABLE_OUTPUT_FILE="${EXECUTE_OUTPUT_VALID_FOLDER}/${FILE_NAME}"
    echo $file
    ./compile -t -o1 $file > "${EXECUTABLE_FILE_NAME}.log.txt"

    if diff "${EXECUTABLE_FILE_NAME}.log.txt" "${VALID_EXAMPLES_SRC_DIR}${folder}/${FILE_NAME}.log" -I scope; then
      (( COUNTER += 1 ))
    fi


    echo "$COUNTER / $(($TOTAL_COUNT)) files have been executed"
  done

  echo "========================================================================================"
  echo "Test Folder" $folder "has been processed" "($COUNTER / $(($TOTAL_COUNT)))"
  echo "========================================================================================"
done