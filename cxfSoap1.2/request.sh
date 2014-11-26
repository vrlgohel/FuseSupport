request=`sed -e "s/\\$1/$1/" request.xml`
curl -s -H "Content-Type: text/xml" -d "$request" http://localhost:8181/cxf/RouteMetrics
echo
