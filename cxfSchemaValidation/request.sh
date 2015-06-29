#!/usr/bin/env bash

echo "Saying hello..."
echo
curl -s -H "Content-Type: text/xml" -d "`cat goodRequest.xml`" http://localhost:8123/greeting | xmllint --format -
echo
echo "Saying goodbye..."
echo
curl -s -H "Content-Type: text/xml" -d "`cat badRequest.xml`" http://localhost:8123/greeting | xmllint --format -
