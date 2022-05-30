#!/bin/bash
COUNTER=0
TOTALCOUNTER=0
for file in $(ls data/lpse/parser-tests) ; do
  if [ "${file##*.}" == "lpse" ]; then
    echo $file :
    let TOTALCOUNTER=TOTALCOUNTER+1
    java lang.lpse.LPseParser data/lpse/parser-tests/$file /lpse/${file%.*}.lpse
    if [ $? -eq 0 ]; then
      let COUNTER=COUNTER+1
      echo "succeeded"
    fi
  fi
done
  echo "Correct Counter:" $COUNTER
  echo "Total Counter:" $TOTALCOUNTER