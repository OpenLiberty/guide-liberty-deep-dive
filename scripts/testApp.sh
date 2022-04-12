#!/bin/bash
set -euxo pipefail




echo ===== Test module-getting-started =====
cd module-getting-started || exit
mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

mvn liberty:start

curl -s http://localhost:9080/inventory/api/systems | grep \\[\\]
if [ $? -gt 0 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems | grep \\[\\]
if [ $? -gt 0 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status=$status
if [ $status -ne 200 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems | grep localhost
if [ $? -gt 0 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status=$status
if [ $status -ne 400 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems/localhost | grep localhost
if [ $? -gt 0 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/localhost?osName=mac&javaVersion=17&heapSize=2")"
echo status=$status
if [ $status -ne 200 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/unknown?osName=mac&javaVersion=17&heapSize=2")"
echo status=$status
if [ $status -ne 400 ] ; then exit $?; fi

curl -X DELETE http://localhost:9080/inventory/api/systems/unknown | grep "does not exists"
if [ $? -gt 0 ] ; then exit $?; fi

curl -X DELETE http://localhost:9080/inventory/api/systems/localhost | grep removed
if [ $? -gt 0 ] ; then exit $?; fi

curl -X POST http://localhost:9080/inventory/api/systems/client/localhost | grep "not implemented"
if [ $? -gt 0 ] ; then exit $?; fi

mvn liberty:stop

echo ===== Test module-openapi =====
cd ../module-openapi || exit
mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

mvn liberty:start

curl -s http://localhost:9080/inventory/api/systems | grep \\[\\]
if [ $? -gt 0 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems | grep \\[\\]
if [ $? -gt 0 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status=$status
if [ $status -ne 200 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems | grep localhost
if [ $? -gt 0 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status=$status
if [ $status -ne 400 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems/localhost | grep localhost
if [ $? -gt 0 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/localhost?osName=mac&javaVersion=17&heapSize=2")"
echo status=$status
if [ $status -ne 200 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/unknown?osName=mac&javaVersion=17&heapSize=2")"
echo status=$status
if [ $status -ne 400 ] ; then exit $?; fi

curl -X DELETE http://localhost:9080/inventory/api/systems/unknown | grep "does not exists"
if [ $? -gt 0 ] ; then exit $?; fi

curl -X DELETE http://localhost:9080/inventory/api/systems/localhost | grep removed
if [ $? -gt 0 ] ; then exit $?; fi

curl -X POST http://localhost:9080/inventory/api/systems/client/localhost | grep "not implemented"
if [ $? -gt 0 ] ; then exit $?; fi

mvn liberty:stop




echo ===== Test module-config =====
cd ../module-config || exit
mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

mvn liberty:start

curl -s http://localhost:9080/inventory/api/systems | grep \\[\\]
if [ $? -gt 0 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems | grep \\[\\]
if [ $? -gt 0 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status=$status
if [ $status -ne 200 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems | grep localhost
if [ $? -gt 0 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status=$status
if [ $status -ne 400 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems/localhost | grep localhost
if [ $? -gt 0 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/localhost?osName=mac&javaVersion=17&heapSize=2")"
echo status=$status
if [ $status -ne 200 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/unknown?osName=mac&javaVersion=17&heapSize=2")"
echo status=$status
if [ $status -ne 400 ] ; then exit $?; fi

curl -X DELETE http://localhost:9080/inventory/api/systems/unknown | grep "does not exists"
if [ $? -gt 0 ] ; then exit $?; fi

curl -X DELETE http://localhost:9080/inventory/api/systems/localhost | grep removed
if [ $? -gt 0 ] ; then exit $?; fi

curl -X POST http://localhost:9080/inventory/api/systems/client/localhost | grep "5555"
if [ $? -gt 0 ] ; then exit $?; fi

mvn liberty:stop
