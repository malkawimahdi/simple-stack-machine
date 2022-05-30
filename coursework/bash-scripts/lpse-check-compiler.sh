#!/bin/bash
COUNTER=0
TOTALCOUNTER=0
for file in $(ls data/lpse/compiler-tests) ; do
  if [ "${file##*.}" == "lpse" ]; then
    echo $file :
    java lang.lpse.LPseCompiler data/lpse/compiler-tests/$file /tmp/${file%.*}.ssma
    java Run /tmp/${file%.*}.ssma > /tmp/${file%.*}-output.txt
    let TOTALCOUNTER=TOTALCOUNTER+1
    diff data/lpse/compiler-tests/${file%.*}-expected.txt /tmp/${file%.*}-output.txt
    if [ $? -eq 0 ]; then
      let COUNTER=COUNTER+1
      echo "succeeded"
    fi
  fi
done
  echo "Correct Counter:" $COUNTER
  echo "Total Counter:" $TOTALCOUNTER