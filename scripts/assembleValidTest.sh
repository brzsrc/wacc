#!/bin/bash

VALID_EXAMPLES=(
                # "/advanced"
                # "/array"
                # "/basic"
                "/expressions"
                # "/function"
                # "/if"
                # "/IO"
                # "/pairs"
                # "/runtimeErr"
                # "/scope"
                # "/sequence"
                # "/variables"
                # "/while"
                )

VALID_EXAMPLES_SRC_DIR="./src/test/examples/valid"
ASSEMBLY_OUTPUT_DIR="./log/assemble"

# counters to represent the total number of test files to be processed
TOTAL_COUNT=$(find "${VALID_EXAMPLES[@]/#/${VALID_EXAMPLES_SRC_DIR}}" -name "*.wacc" | wc -l)
COUNTER=0

for folder in ${VALID_EXAMPLES[@]}; do
  ASSEMBLY_OUTPUT_VALID_FOLDER="${ASSEMBLY_OUTPUT_DIR}${folder}"
  mkdir $ASSEMBLY_OUTPUT_VALID_FOLDER
  for file in $(find "${VALID_EXAMPLES_SRC_DIR}${folder}" -name "*.wacc")
  do
    FILE_NAME=$(basename "${file%.*}")
    echo $file
    ASSEMBLY_OUTPUT_FILE_NAME="${ASSEMBLY_OUTPUT_VALID_FOLDER}/${FILE_NAME}"
    ./compile -a $file 1> "${ASSEMBLY_OUTPUT_FILE_NAME}.s" 2> "${ASSEMBLY_OUTPUT_FILE_NAME}.log"
    ret=$?
    echo "exit status: " $ret

    (( COUNTER += $((1-$ret)) ))
    echo "$COUNTER / $(($TOTAL_COUNT)) files have successfully been assembled"
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