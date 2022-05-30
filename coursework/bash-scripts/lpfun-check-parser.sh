#!/bin/bash
COUNTER=0
TOTALCOUNTER=0
for file in $(ls data/lpfun/parser-tests) ; do
  if [ "${file##*.}" == "lpfun" ]; then
    echo $file :
    let TOTALCOUNTER=TOTALCOUNTER+1
    java lang.lpfun.LPfunParser data/lpfun/parser-tests/$file /lpfun/${file%.*}.lpfun
    if [ $? -eq 0 ]; then
      let COUNTER=COUNTER+1
      echo "succeeded"
    fi
  fi
done
  echo "Correct Counter:" $COUNTER
  echo "Total Counter:" $TOTALCOUNTER