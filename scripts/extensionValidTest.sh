#!/bin/bash

VALID_EXAMPLES=(
                 "/binOctHex"
                 "/bitwiseOperation"
                 "/do-while"
                 "/for"
                 "/import"
                 "/jump"
                 "/struct"
                 "/switch"
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
    ./compile $file 2> "${EXECUTABLE_FILE_NAME}.log.txt"
    echo "${EXECUTABLE_FILE_NAME}.s"
    echo "${FILE_NAME}.s"
    mv "${FILE_NAME}.s" "${EXECUTABLE_FILE_NAME}.s"

    arm-linux-gnueabi-gcc -o $EXECUTABLE_OUTPUT_FILE -mcpu=arm1176jzf-s -mtune=arm1176jzf-s "${EXECUTABLE_FILE_NAME}.s" > "${EXECUTABLE_OUTPUT_FILE}.log.txt"
    ret1=$?
    echo "assembler exit status" $ret1
    timeout 5 qemu-arm -L /usr/arm-linux-gnueabi/ $EXECUTABLE_OUTPUT_FILE > "${EXECUTABLE_OUTPUT_FILE}.output.txt"
    ret2=$?
    echo "execution exit status" $ret2

    ## altomatically generate output .log file
    # mv "${EXECUTABLE_OUTPUT_FILE}.output.txt" "${VALID_EXAMPLES_SRC_DIR}${folder}/${FILE_NAME}.log"

    if [ "$ret1" -eq 0 ] && [ "$ret2" -eq 0 ]; then 
    ## test if output is same as .log file
       diff "${EXECUTABLE_OUTPUT_FILE}.output.txt" "${VALID_EXAMPLES_SRC_DIR}${folder}/${FILE_NAME}.log"
      (( COUNTER += 1 ))
    fi 

    
    echo "$COUNTER / $(($TOTAL_COUNT)) files have been executed"
  done

  echo "========================================================================================"
  echo "Test Folder" $folder "has been processed" "($COUNTER / $(($TOTAL_COUNT)))"
  echo "========================================================================================"
done