#!/bin/bash

cyan='\e[96m'
none='\e[0m'

file=$1
cmd="java $JAVA_OPTS -jar $file"

if [ $MASTER_URL ]; then
  cmd="$cmd --master $MASTER_URL"
fi

if [ $TRAIN_DATASET_PATH ]; then
  cmd="$cmd --train-dataset $TRAIN_DATASET_PATH"
fi

if [ $TEST_DATASET_PATH ]; then
  cmd="$cmd --test-dataset $TEST_DATASET_PATH"
fi

if [ $VALIDATE_DATASET_PATH ]; then
  cmd="$cmd --validate-dataset $VALIDATE_DATASET_PATH"
fi

# execute command
echo "the command is: ${cyan}${cmd}${none}"
${cmd}
