#!/bin/bash

VALID_EXAMPLE_DIR="./src/test/examples/valid/"
VALID_EXAMPLES=(
                 "/constantPropagation/evaluation/"
                )

VALID_EXAMPLE_LOG="./log/const_eval.log"
> $VALID_EXAMPLE_LOG

TEST_DIR="$VALID_EXAMPLE_DIR$VALID_EXAMPLES"
# counters to represent the total number of test files to be processed
TOTAL_COUNT=$(find "$VALID_EXAMPLE_DIR${VALID_EXAMPLES[@]}" -name "*.wacc" | wc -l)
COUNTER=0

for folder in "$VALID_EXAMPLE_DIR${VALID_EXAMPLES[@]}"; do
  for file in $(find "$folder" -name "*.wacc"); do
    FILE_NAME=$(basename "${file%.*}")
    ./compile -o1 -t $file 2> $VALID_EXAMPLE_LOG
    ret=$?
    echo "exit status: " $ret
    if [ $ret -ne 0 ]; then
      echo $file
      echo "status code incorrect: " $ret
      exit 1
    fi
    
    if diff "$TEST_DIR${FILE_NAME}.log" $VALID_EXAMPLE_LOG > temp; then
      echo $FILE_NAME
      echo "optimise differ from expected result: "
      
      exit 1
    fi
    (( COUNTER += 1 ))
    echo "$COUNTER / $(($TOTAL_COUNT)) finished"
    echo ""
  done

  echo "========================================================================================"
  echo "Test Folder" $folder "has been processed" "($COUNTER / $(($TOTAL_COUNT)))"
  echo "========================================================================================"
done