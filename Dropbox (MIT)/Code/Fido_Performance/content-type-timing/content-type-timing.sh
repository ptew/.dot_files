#!/bin/bash

for i in `seq 1 50`;
do
  curl -i -w "@curl-to-csv.txt" -o output/csv-output-$i.txt -s -H "Accept: text/plain" -H "Authorization: xO5Ae0FwNxXciC4XPzLH6kt3U0A=" -H "Connection: keep-alive" https://api-sandbox.lendingclub.com/api/investor/v1/loans/listing?showAll=true >> csv-compare.csv
  sleep 1
done

#for i in `seq 1 50`;
#do
#  curl -i -w "@curl-to-csv.txt" -o output/json-output-$i.txt -s -H "Accept: application/json" -H "Authorization: xO5Ae0FwNxXciC4XPzLH6kt3U0A=" -H "Connection: keep-alive" https://api-sandbox.lendingclub.com/api/investor/v1/loans/listing?showAll=true >> json-compare.csv
#  sleep 1
#done
#
#for i in `seq 1 50`;
#do
#  curl -i -w "@curl-to-csv.txt" -o output/xml-output-$i.txt -s -H "Accept: application/xml" -H "Authorization: xO5Ae0FwNxXciC4XPzLH6kt3U0A=" -H "Connection: keep-alive" https://api-sandbox.lendingclub.com/api/investor/v1/loans/listing?showAll=true >> xml-timing.csv
#  sleep 1
#done
